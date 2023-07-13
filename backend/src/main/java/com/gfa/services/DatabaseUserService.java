package com.gfa.services;

import com.gfa.dtos.UserRequestDto;
import com.gfa.models.User;
import com.gfa.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class DatabaseUserService implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public DatabaseUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
