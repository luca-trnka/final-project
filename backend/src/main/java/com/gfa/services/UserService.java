package com.gfa.services;

import com.gfa.dtos.UserRequestDto;
import com.gfa.models.User;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User createUser(User user);

    User updateUser(Long id, UserRequestDto userRequestDto);

    void deleteUser(User user);

    List<User> listUsers();

    User getUserById(Long id);

    boolean isUsernameInDatabase(String username);

    boolean isEmailInDatabase(String email);

    void validateId(Long id);

    void validateUser(Long id);

    void validateInputData(UserRequestDto userRequestDto) throws AuthenticationException;

    void validateUpdatedInputData(UserRequestDto userRequestDto);
}
