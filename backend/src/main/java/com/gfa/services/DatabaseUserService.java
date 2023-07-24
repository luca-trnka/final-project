package com.gfa.services;

import com.gfa.config.JwtTokenProvider;
import com.gfa.dtos.*;
import com.gfa.models.User;
import com.gfa.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import javax.mail.MessagingException;
import javax.naming.AuthenticationException;
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
    private EmailService emailService;
    @Autowired
    private HttpServletRequest request;

    public DatabaseUserService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, UserRequestDto userRequestDto) {
        User user = getUserById(id);

        if (!user.getEmail().contains(userRequestDto.getEmail())) {
            //repeat email verification process
            user.setVerified(null);
        }

        user.setUsername(userRequestDto.getUsername());
        user.setEmail(userRequestDto.getEmail());
        user.setPassword(userRequestDto.getPassword());

        return userRepository.save(user);
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
        if (userRequestDto.getUsername() == null || userRequestDto.getUsername().length() == 0) {
            throw new IllegalArgumentException("Username is required");
        }
        if (userRequestDto.getEmail() == null || userRequestDto.getEmail().length() == 0) {
            throw new IllegalArgumentException("Email is required");
        }
        if (!userRequestDto.getEmail().contains("@") || !userRequestDto.getEmail().contains(".")) {
            throw new IllegalArgumentException("Invalid email");
        }
        if (userRequestDto.getPassword() == null || userRequestDto.getPassword().length() == 0) {
            throw new IllegalArgumentException("Password is required");
        }
        if (userRequestDto.getPassword().length() < 6 ) {
            throw new IllegalArgumentException("Password too short");
        }
        if (isUsernameInDatabase(userRequestDto.getUsername())) {
            throw new AuthenticationException("Username already exists");
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
                userRequestDto.getPassword().length() < 6 ) {
            throw new IllegalArgumentException("Invalid data");
        }

    }
}
