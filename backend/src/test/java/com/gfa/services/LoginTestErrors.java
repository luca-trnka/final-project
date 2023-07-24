package com.gfa.services;

import com.gfa.dtos.LoginRequestDto;
import com.gfa.dtos.LoginResponseDto;
import com.gfa.models.User;
import com.gfa.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


public class LoginTestErrors {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    /*@Mock
    private AuthenticationManager manager;*/

    @InjectMocks
    private DatabaseUserService userService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /*@Test
    void login_success()  {
        String username = "sanyi";
        String password = "password1234";
        LoginRequestDto loginRequestDto = new LoginRequestDto(username, password);
        User user = new User(username, "sanyi@gfa.com", password);
        user.setVerified(true);
        user.setPassword("$2a$04$cT4bpWj0O98wmzJ51sHihu9GPDUC9qdsjFBE9wvn711sYxY92QQL.");

        when(userRepository.existsByUsername(loginRequestDto.getUsername())).thenReturn(true);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);
       // when(manager.authenticate(new UsernamePasswordAuthenticationToken(username, password))).thenReturn(new Authentication);
       // when(userService.generateToken(username, password));
       System.out.println("User's Password: " + user.getPassword()+ " password: "+ password);
        Map<String, Object> expectedData = new HashMap<>();
        expectedData.put("token", "someGeneratedToken");

        LoginResponseDto expectedResponse = new LoginResponseDto("success", expectedData);
        LoginResponseDto actualResponse = userService.login(loginRequestDto);

        Assertions.assertEquals(expectedResponse.getMessage(), actualResponse.getMessage());
    }*/


    @Test
    void login_no_username_error() throws Exception {
        LoginRequestDto loginRequestDto = new LoginRequestDto(null, "password");

        try {
            userService.login(loginRequestDto);
        } catch (IllegalArgumentException e) {
            assertEquals("Username is required.", e.getMessage());
        }
    }

    @Test
    void login_invalid_username_error() throws Exception {
        LoginRequestDto loginRequestDto = new LoginRequestDto("nin", "password");

        try {
            userService.login(loginRequestDto);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid username.", e.getMessage());
        }
    }

    @Test
    void login_no_password_error() throws Exception {
        LoginRequestDto loginRequestDto = new LoginRequestDto("ninja6", "");

        try {
            userService.login(loginRequestDto);
        } catch (IllegalArgumentException e) {
            assertEquals("Password is required.", e.getMessage());
        }
    }

    @Test
    void login_unregistered_username_error() throws Exception {
        LoginRequestDto loginRequestDto = new LoginRequestDto("ninja123", "password");
        User user = new User(loginRequestDto.getUsername(), "ninja123@email.com", loginRequestDto.getPassword());
        user.setVerified(true);
        when(userRepository.findByUsername(loginRequestDto.getUsername())).thenReturn(Optional.of(user));
        when(userRepository.existsByUsername(loginRequestDto.getUsername())).thenReturn(false);
        try {
            userService.login(loginRequestDto);
        } catch (BadCredentialsException e) {
           // assertEquals("Invalid username or password!", e.getMessage());
            assertEquals("Unregistered username!", e.getMessage());
        }
    }

    @Test
    void login_valid_username_invalid_password_error() throws Exception {
        LoginRequestDto loginRequestDto = new LoginRequestDto("ninja123", "password123");
        when(userRepository.existsByUsername(loginRequestDto.getUsername())).thenReturn(true);
        User user = new User(loginRequestDto.getUsername(), "ninja123@email.com", loginRequestDto.getPassword());
        user.setVerified(true);
        user.setPassword("$2a$04$cT4bpWj0O98wmzJ51sHihu9GPDUC9qdsjFBE9wvn711sYxY92QQL.");
        when(userRepository.findByUsername(loginRequestDto.getUsername())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())).thenReturn(false);

        try {
            userService.login(loginRequestDto);
        } catch (BadCredentialsException e) {
          //  assertEquals("Invalid username or password!", e.getMessage());
            assertEquals("Password is not matching for this username!", e.getMessage());
        }
    }

    @Test
    void login_user_is_not_verified_error() throws Exception {
        LoginRequestDto loginRequestDto = new LoginRequestDto("ninja123", "password");
        User user = new User(loginRequestDto.getUsername(), "ninja123@email.com", loginRequestDto.getPassword());
        user.setVerified(false);
        when(userRepository.findByUsername(loginRequestDto.getUsername())).thenReturn(Optional.of(user));
        when(userRepository.existsByUsername(loginRequestDto.getUsername())).thenReturn(true);
        when(passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())).thenReturn(true);
        try {
            userService.login(loginRequestDto);
        } catch (BadCredentialsException e) {
        // assertEquals("Invalid username or password!", e.getMessage());
            assertEquals("Username is not verified!", e.getMessage());
        }
    }


}
