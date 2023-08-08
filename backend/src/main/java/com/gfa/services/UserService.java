package com.gfa.services;

import com.gfa.dtos.*;
import com.gfa.models.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.mail.MessagingException;
import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService {
    User createUser(User user);

    User updateUser(Long id, UserRequestDto userRequestDto) throws MessagingException;

    void deleteUser(User user);

    List<User> listUsers();

    boolean verifyUser(String email, String password);
    String generateToken(String email, String password);
    User getUserById(Long id);

    User getUserByUsername(String username) throws UsernameNotFoundException;

    boolean isUsernameInDatabase(String username);

    boolean isEmailInDatabase(String email);

    void validateId(Long id);

    void validateUser(Long id);

    void validateInputData(UserRequestDto userRequestDto) throws AuthenticationException;

    void validateUpdatedInputData(UserRequestDto userRequestDto);

    Object login(LoginRequestDto loginDetails);

    RegisterResponseDto addUser(UserRequestDto newUserDTO) throws AuthenticationException, MessagingException;
  
    UserProfileResponseDto updateUserProfile(UserProfileRequestDto updatedUser) throws AuthenticationException;
}
