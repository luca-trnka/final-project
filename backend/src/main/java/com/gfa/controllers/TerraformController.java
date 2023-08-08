package com.gfa.controllers;

import com.gfa.dtos.TfRequestDto;
import com.gfa.dtos.TfResponseDto;
import com.gfa.services.TerraformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class TerraformController {
    private final TerraformService terraformService;

    @Autowired
    public TerraformController(TerraformService terraformService) {
        this.terraformService = terraformService;
    }

    @PostMapping("/ec2/instances")
    public ResponseEntity<TfResponseDto> saveAwsEc2(@RequestBody @Valid TfRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(terraformService.saveInstance(dto));
    }
}
