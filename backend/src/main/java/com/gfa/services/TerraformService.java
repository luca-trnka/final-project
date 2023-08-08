package com.gfa.services;

import com.gfa.config.JwtTokenProvider;
import com.gfa.dtos.TfDto;
import com.gfa.dtos.TfRequestDto;
import com.gfa.dtos.TfResponseDto;
import com.gfa.models.Instance;
import com.gfa.repositories.InstanceRepository;
import com.gfa.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

@Service
public class TerraformService {
    private static final Logger logger = LoggerFactory.getLogger(TerraformService.class);
    private final InstanceRepository instanceRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private JwtTokenProvider tokenProvider;

    public TerraformService(InstanceRepository instanceRepository, UserRepository userRepository, EmailService emailService) {
        this.instanceRepository = instanceRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    private static void captureAndSaveTerraformOutput(String outputVariable, String outputFile) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("terraform", "-chdir=terraform", "output", outputVariable);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append(System.lineSeparator());
            }
            int exitCode = process.waitFor();
            logger.info("terraform output command exited with code: {}", exitCode);
//            System.out.println("terraform output command exited with code: " + exitCode);

            // write the output to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
                writer.write(output.toString());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public TfResponseDto saveInstance(TfRequestDto dto) {
        logger.info("Processing post request for storing EC2 instance...");
        logger.debug("... os: {}, region: {}, size: {}, count: {}", dto.getOs(), dto.getRegion(), dto.getSize(), dto.getCount());

        // transfer os to ami
        String os = null;
        if (dto.getOs().equals("Amazon Linux")) os = "ami-0b8b5288592eca360";
        if (dto.getOs().equals("Ubuntu")) os = "ami-05b5a865c3579bbc4";

        // create TF variable file
        Path varFilePath = Paths.get("terraform/terraform.tfvars");
        List<String> content = new ArrayList<>();
        content.add("ec2_count=\"" + dto.getCount() + "\"");
        content.add("ec2_os=\"" + os + "\"");
        content.add("ec2_region=\"" + dto.getRegion() + "\"");
        content.add("ec2_type=\"" + dto.getSize() + "\"");
        try {
            Files.write(varFilePath, content, StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // send Terraform commands to Terminal
        sendCommand("terraform", "-chdir=terraform", "init");
        File file = new File("terraform/terraform.tfstate");
        if (file.delete())
            logger.debug("terraform/terraform.tfstate deleted successfully.");
        else
            logger.debug("Failed to delete terraform/terraform.tfstate.");
        sendCommand("terraform", "-chdir=terraform", "apply", "-auto-approve");
        captureAndSaveTerraformOutput("instance_public_ips", "terraform/instance-public-ips.txt");

        // get email from JWT token
        String token = request.getHeader("Authorization");
        token = token.substring(7);
        String username = tokenProvider.getUsernameFromToken(token);
        String email = userRepository.findByUsername(username).get().getEmail();

        // get public IPs from file and send email
        Path ipsFilePath = Paths.get("terraform/instance-public-ips.txt");
        try {
            List<String> ips;
            ips = Files.readAllLines(ipsFilePath);
            emailService.sendConfirmationEmail(email, ips, dto);
        } catch (IOException | MessagingException e) {
            throw new RuntimeException(e);
        }

        // generate response
        List<TfDto> instances = new ArrayList<>();
        for (int i = 1; i <= dto.getCount(); i++) {
            Instance instance = instanceRepository.save(new Instance("instance-" + i, dto.getRegion(), dto.getOs(), dto.getSize()));
            instances.add(new TfDto(instance.getId(), instance.getName(), instance.getRegion(), instance.getOperatingSystem(), instance.getSize()));
        }
        return new TfResponseDto(instances);
    }

    private void sendCommand(String... command) {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);
        try {
            Process process = processBuilder.start();

            // read the process output
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            int exitCode = process.waitFor();
            System.out.println("command exited with code: " + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
