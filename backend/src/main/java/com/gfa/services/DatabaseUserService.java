package com.gfa.services;

import com.gfa.config.JwtTokenProvider;
import com.gfa.dtos.*;
import com.gfa.models.User;
import com.gfa.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import javax.mail.MessagingException;
import javax.naming.AuthenticationException;
import java.security.SecureRandom;
import java.util.*;

@Service
public class DatabaseUserService implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    @Lazy
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    @Autowired
    private HttpServletRequest request;

    public DatabaseUserService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @Override
    public boolean verifyUser(String username, String password) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NoSuchElementException("User not found"));
        return new BCryptPasswordEncoder().matches(password, user.getPassword());
    }

    public boolean checkUserNameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public String generateToken(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return tokenProvider.generateToken(authentication);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, UserRequestDto userRequestDto) throws MessagingException {
        User user = getUserById(id);
        if (!user.getUsername().equals(userRequestDto.getUsername())) {
            user.setUsername(userRequestDto.getUsername());
        }
        if (!user.getEmail().equals(userRequestDto.getEmail())) {
            user.setVerified(false);
            user.setVerifiedAt(null);
            SecureRandom secureRandom = new SecureRandom();
            Long token = secureRandom.nextLong();
            user.setVerificationToken(token);
            user.setVerificationTokenExpiresAt((System.currentTimeMillis() / 1000) + 3600);
            emailService.sendVerificationEmail(user.getEmail(), token);
            user.setEmail(userRequestDto.getEmail());
        }
        if (!user.getPassword().equals(userRequestDto.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    @Override
    public RegisterResponseDto addUser(UserRequestDto newUserDTO) throws AuthenticationException, MessagingException {
        if (newUserDTO == null)
            throw new IllegalArgumentException("Request body is empty");
        validateInputData(newUserDTO);
        User user = new User(newUserDTO.getUsername(), newUserDTO.getEmail(), newUserDTO.getPassword());
        user.setVerified(false);
        user.setVerifiedAt(null);
        SecureRandom secureRandom = new SecureRandom();
        Long token = secureRandom.nextLong();
        user.setVerificationToken(token);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setVerificationTokenExpiresAt((System.currentTimeMillis() / 1000) + 3600);
        userRepository.save(user);
        emailService.sendVerificationEmail(user.getEmail(), token);
        return new RegisterResponseDto("OK");
    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    @Override
    public List<User> listUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public User getUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public boolean isUsernameInDatabase(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean isEmailInDatabase(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserProfileResponseDto updateUserProfile(UserProfileRequestDto updatedUser) throws AuthenticationException {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            String username = tokenProvider.getUsernameFromToken(token);
            User currentUser = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            if (isEmptyRequest(updatedUser)) {
                throw new IllegalArgumentException("A field is required.");
            }

            if (updatedUser.getName() != null && !updatedUser.getName().isEmpty()) {
                validateUsername(updatedUser.getName());
                currentUser.setUsername(updatedUser.getName());
            }

            if (updatedUser.getEmail() != null && !updatedUser.getEmail().isEmpty()) {
                validateEmail(updatedUser.getEmail());
                currentUser.setEmail(updatedUser.getEmail());
            }

            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                validatePassword(updatedUser.getPassword());
                currentUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }

            userRepository.save(currentUser);
            return new UserProfileResponseDto(currentUser.getId(), currentUser.getUsername(), currentUser.getEmail());
        }

        throw new AuthenticationException("Invalid JWT token");
    }

    private boolean isEmptyRequest(UserProfileRequestDto updatedUser) {
        return updatedUser.getName() == null && updatedUser.getEmail() == null && updatedUser.getPassword() == null;
    }

    private void validateUsername(String username) {
        if (isUsernameInDatabase(username)) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (username.length() < 4) {
            throw new IllegalArgumentException("Username must be at least 4 characters long");
        }
    }

    private void validateEmail(String email) {
        if (isEmailInDatabase(email)) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (!isEmailValid(email)) {
            throw new IllegalArgumentException("Invalid email");
        }
    }

    private void validatePassword(String password) {
        if (password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }
    }

    private boolean isEmailValid(String email) {
        return email.matches("([a-zA-Z0-9]+(?:[._+-][a-zA-Z0-9]+)*)@([a-zA-Z0-9]+(?:[.-][a-zA-Z0-9]+)*[.][a-zA-Z]{2,})");
    }


    @Override
    public void validateId(Long id) {
        if (!(id > 0)) {
            throw new IllegalArgumentException("Invalid id");
        }
    }

    @Override
    public void validateUser(Long id) {
        try {
            getUserById(id);
        } catch (Exception e) {
            throw new NoSuchElementException("User not found");
        }
    }

    @Override
    public void validateInputData(UserRequestDto userRequestDto) throws AuthenticationException {
        if (userRequestDto.getUsername() == null || userRequestDto.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (userRequestDto.getUsername().length() < 4)
            throw new IllegalArgumentException("Username must be at least 4 characters long");

        if (userRequestDto.getEmail() == null || userRequestDto.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (!userRequestDto.getEmail().matches("([a-zA-Z0-9]+(?:[._+-][a-zA-Z0-9]+)*)@([a-zA-Z0-9]+(?:[.-][a-zA-Z0-9]+)*[.][a-zA-Z]{2,})")) {
            throw new IllegalArgumentException("Invalid email");
        }
        if (userRequestDto.getPassword() == null || userRequestDto.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
        if (userRequestDto.getPassword().length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }
        if (isUsernameInDatabase(userRequestDto.getUsername())) {
            throw new AuthenticationException("Username is already taken");
        }
        if (isEmailInDatabase(userRequestDto.getEmail())) {
            throw new AuthenticationException("Email already exists");
        }
    }

    @Override
    public void validateUpdatedInputData(UserRequestDto userRequestDto) {
        if (!userRequestDto.getEmail().contains("@") ||
                !userRequestDto.getEmail().contains(".") ||
                //need to solve validation of username
                userRequestDto.getPassword().length() < 6) {
            throw new IllegalArgumentException("Invalid data");
        }
    }

    public LoginResponseDto login(LoginRequestDto loginDetails) {

        String username = loginDetails.getUsername();
        String password = loginDetails.getPassword();

        //Data Validation
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username is required.");
        }
        if (username.length() < 4) {
            throw new IllegalArgumentException("Invalid username.");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password is required.");
        }

        User user = getUserByUsername(username); //if the user is verified(through email)
        if (!user.getVerified() || user.getVerified() == null) {
            // throw new BadCredentialsException("Invalid username or password!");
            throw new BadCredentialsException("Username is not verified!");
        }

        if (!isUsernameInDatabase(username)) {  //if the username is registered
          //  throw new BadCredentialsException("Invalid username or password!");
            throw new BadCredentialsException("Unregistered username!");
        }

        if (!verifyUser(username, password)) { //if the password is right for the username
           // throw new BadCredentialsException("Invalid username or password!");
            throw new BadCredentialsException("Password is not matching for this username!");
       }

        Map<String, Object> data = new HashMap<>();
        data.put("token", generateToken(loginDetails.getUsername(), loginDetails.getPassword()));
        return new LoginResponseDto("success", data);
    }
}