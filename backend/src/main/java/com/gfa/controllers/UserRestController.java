package com.gfa.controllers;

import com.gfa.dtos.*;
import com.gfa.models.User;
import com.gfa.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.naming.AuthenticationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserRestController {
    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponseDto> index() {
        return userService.listUsers().stream()
                //need to solve 'verified_at'
                .map(user -> new UserResponseDto(user.getId(), user.getUsername(), user.getEmail(), LocalDateTime.now().toString()))
                .collect(Collectors.toList());
    }

    @PostMapping("/")
    public ResponseEntity<UserResponseDto> store(@RequestBody UserRequestDto userRequestDto) throws AuthenticationException {
        userService.validateInputData(userRequestDto);
        User user = new User(userRequestDto.getUsername(), userRequestDto.getEmail(), userRequestDto.getPassword());
        userService.createUser(user);
        //need to solve 'verified_at'
        return ResponseEntity.status(201).body(new UserResponseDto(user.getId(), user.getUsername(), user.getEmail(), LocalDateTime.now().toString()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> show(@PathVariable Long id) {
        userService.validateId(id);
        userService.validateUser(id);
        User user = userService.getUserById(id);
        //need to solve 'verified_at'
        return ResponseEntity.status(200).body(new UserResponseDto(user.getId(), user.getUsername(), user.getEmail(), LocalDateTime.now().toString()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDto> update(@PathVariable Long id, @RequestBody UserRequestDto userRequestDto) throws AuthenticationException {
        userService.validateId(id);
        userService.validateUser(id);
        userService.validateUpdatedInputData(userRequestDto);
        User user = userService.updateUser(id, userRequestDto);
        //need to solve 'verified_at'
        return ResponseEntity.status(200).body(new UserResponseDto(user.getId(), user.getUsername(), user.getEmail(), LocalDateTime.now().toString()));
    } 
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> destroy(@PathVariable Long id) {
        userService.validateId(id);
        userService.validateUser(id);
        User user = userService.getUserById(id);
        userService.deleteUser(user);
        return ResponseEntity.status(201).build();
    }
  
    @PatchMapping
    public ResponseEntity<UserProfileResponseDto> updateUserProfile(@RequestBody UserProfileRequestDto updatedUser) throws AuthenticationException {
        return ResponseEntity.ok(userService.updateUserProfile(updatedUser));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> illegalArgumentExceptionHandler(Exception e){
        ErrorResponseDto response = new ErrorResponseDto(e.getMessage());
        return ResponseEntity.status(400).body(response);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseDto> authenticationExceptionHandler(Exception e){
        ErrorResponseDto response = new ErrorResponseDto(e.getMessage());
        return ResponseEntity.status(409).body(response);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponseDto> noSuchElementExceptionExceptionHandler(Exception e){
        ErrorResponseDto response = new ErrorResponseDto(e.getMessage());
        return ResponseEntity.status(404).body(response);

    }
}
