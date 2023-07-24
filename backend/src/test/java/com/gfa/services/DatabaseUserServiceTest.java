package com.gfa.services;

import com.gfa.config.JwtTokenProvider;
import com.gfa.dtos.UserProfileRequestDto;
import com.gfa.dtos.UserProfileResponseDto;
import com.gfa.dtos.UserRequestDto;
import com.gfa.models.User;
import com.gfa.repositories.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.mail.MessagingException;
import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {DatabaseUserService.class})
@ExtendWith(SpringExtension.class)
class DatabaseUserServiceTest {
    @MockBean
    private AuthenticationManager authenticationManager;

    @Autowired
    private DatabaseUserService databaseUserService;

    @MockBean
    private EmailService emailService;

    @MockBean
    private HttpServletRequest httpServletRequest;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserRepository userRepository;

    private static UserService userService;

    @BeforeAll
    static void init() {
        userService = Mockito.mock(UserService.class);
    }

    @Test
    void test_createUser() {
        User user = new User("john123", "john123@mail.com", "123");

        Mockito.when(userService.createUser(user)).thenReturn(user);

        User result = userService.createUser(user);

        assertEquals("john123", result.getUsername());
        assertEquals("john123@mail.com", result.getEmail());
        assertEquals("123", result.getPassword());
    }

    @Test
    void test_updateUser() throws MessagingException {
        User user = new User("john123", "john123@mail.com", "123");
        UserRequestDto userRequestDto = new UserRequestDto("john123", "john123@mail.com", "321");
        user.setPassword("321");

        Mockito.when(userService.updateUser(user.getId(), userRequestDto)).thenReturn(user);

        User result = userService.updateUser(user.getId(), userRequestDto);

        assertEquals("john123", result.getUsername());
        assertEquals("john123@mail.com", result.getEmail());
        assertEquals("321", result.getPassword());
    }

    /**
     * Method under test: {@link DatabaseUserService#addUser(UserRequestDto)}
     */
    @Test
    void testAddUser() throws MessagingException, AuthenticationException {
        when(userRepository.existsByUsername(Mockito.<String>any())).thenReturn(true);
        assertThrows(AuthenticationException.class,
                () -> databaseUserService.addUser(new UserRequestDto("janedoe", "jane.doe@example.org", "iloveyou")));
        verify(userRepository).existsByUsername(Mockito.<String>any());
    }

    /**
     * Method under test: {@link DatabaseUserService#addUser(UserRequestDto)}
     */
    @Test
    void testAddUser2() throws MessagingException, AuthenticationException {
        when(userRepository.existsByEmail(Mockito.<String>any())).thenReturn(true);
        when(userRepository.existsByUsername(Mockito.<String>any())).thenReturn(false);
        assertThrows(AuthenticationException.class,
                () -> databaseUserService.addUser(new UserRequestDto("janedoe", "jane.doe@example.org", "iloveyou")));
        verify(userRepository).existsByEmail(Mockito.<String>any());
        verify(userRepository).existsByUsername(Mockito.<String>any());
    }

    /**
     * Method under test: {@link DatabaseUserService#addUser(UserRequestDto)}
     */
    @Test
    void testAddUser3() throws MessagingException, AuthenticationException {
        assertThrows(IllegalArgumentException.class,
                () -> databaseUserService.addUser(new UserRequestDto(null, "jane.doe@example.org", "iloveyou")));
    }

    /**
     * Method under test: {@link DatabaseUserService#addUser(UserRequestDto)}
     */
    @Test
    void testAddUser4() throws MessagingException, AuthenticationException {
        assertThrows(IllegalArgumentException.class,
                () -> databaseUserService.addUser(new UserRequestDto("42", "jane.doe@example.org", "iloveyou")));
    }

    /**
     * Method under test: {@link DatabaseUserService#addUser(UserRequestDto)}
     */
    @Test
    void testAddUser5() throws MessagingException, AuthenticationException {
        assertThrows(IllegalArgumentException.class,
                () -> databaseUserService.addUser(new UserRequestDto("", "jane.doe@example.org", "iloveyou")));
    }

    /**
     * Method under test: {@link DatabaseUserService#addUser(UserRequestDto)}
     */
    @Test
    void testAddUser6() throws MessagingException, AuthenticationException {
        when(userRepository.existsByUsername(Mockito.<String>any())).thenReturn(true);
        assertThrows(AuthenticationException.class,
                () -> databaseUserService.addUser(new UserRequestDto("janedoe", "john.smith@example.org", "iloveyou")));
        verify(userRepository).existsByUsername(Mockito.<String>any());
    }

    /**
     * Method under test: {@link DatabaseUserService#addUser(UserRequestDto)}
     */
    @Test
    void testAddUser7() throws MessagingException, AuthenticationException {
        when(userRepository.existsByUsername(Mockito.<String>any())).thenReturn(true);
        assertThrows(AuthenticationException.class,
                () -> databaseUserService.addUser(new UserRequestDto("janedoe", "prof.einstein@example.org", "iloveyou")));
        verify(userRepository).existsByUsername(Mockito.<String>any());
    }

    /**
     * Method under test: {@link DatabaseUserService#addUser(UserRequestDto)}
     */
    @Test
    void testAddUser8() throws MessagingException, AuthenticationException {
        when(userRepository.existsByUsername(Mockito.<String>any())).thenReturn(true);
        assertThrows(AuthenticationException.class,
                () -> databaseUserService.addUser(new UserRequestDto("janedoe", "U+U+U@U-U-U.UUUU", "iloveyou")));
        verify(userRepository).existsByUsername(Mockito.<String>any());
    }

    /**
     * Method under test: {@link DatabaseUserService#addUser(UserRequestDto)}
     */
    @Test
    void testAddUser9() throws MessagingException, AuthenticationException {
        assertThrows(IllegalArgumentException.class, () -> databaseUserService.addUser(new UserRequestDto("janedoe",
                "([a-zA-Z0-9]+(?:[._+-][a-zA-Z0-9]+)*)@([a-zA-Z0-9]+(?:[.-][a-zA-Z0-9]+)*[.][a-zA-Z]{2,})", "iloveyou")));
    }

    /**
     * Method under test: {@link DatabaseUserService#addUser(UserRequestDto)}
     */
    @Test
    void testAddUser10() throws MessagingException, AuthenticationException {
        assertThrows(IllegalArgumentException.class,
                () -> databaseUserService.addUser(new UserRequestDto("janedoe", null, "iloveyou")));
    }

    /**
     * Method under test: {@link DatabaseUserService#addUser(UserRequestDto)}
     */
    @Test
    void testAddUser11() throws MessagingException, AuthenticationException {
        assertThrows(IllegalArgumentException.class,
                () -> databaseUserService.addUser(new UserRequestDto("janedoe", "", "iloveyou")));
    }

    /**
     * Method under test: {@link DatabaseUserService#addUser(UserRequestDto)}
     */
    @Test
    void testAddUser12() throws MessagingException, AuthenticationException {
        assertThrows(IllegalArgumentException.class,
                () -> databaseUserService.addUser(new UserRequestDto("janedoe", "jane.doe@example.org", null)));
    }

    /**
     * Method under test: {@link DatabaseUserService#addUser(UserRequestDto)}
     */
    @Test
    void testAddUser13() throws MessagingException, AuthenticationException {
        assertThrows(IllegalArgumentException.class,
                () -> databaseUserService.addUser(new UserRequestDto("janedoe", "jane.doe@example.org", "42")));
    }

    /**
     * Method under test: {@link DatabaseUserService#addUser(UserRequestDto)}
     */
    @Test
    void testAddUser14() throws MessagingException, AuthenticationException {
        assertThrows(IllegalArgumentException.class,
                () -> databaseUserService.addUser(new UserRequestDto("janedoe", "jane.doe@example.org", "")));
    }

    /**
     * Method under test: {@link DatabaseUserService#addUser(UserRequestDto)}
     */
    @Test
    void testAddUser15() throws MessagingException, AuthenticationException {
        assertThrows(IllegalArgumentException.class, () -> databaseUserService.addUser(null));
    }

    /**
     * Method under test: {@link DatabaseUserService#addUser(UserRequestDto)}
     */
    @Test
    @Disabled
    void testAddUser16() throws MessagingException, AuthenticationException {
        UserRequestDto newUserDTO = mock(UserRequestDto.class);
        when(newUserDTO.getUsername()).thenThrow(new IllegalArgumentException("foo"));
        databaseUserService.addUser(newUserDTO);
        verify(newUserDTO).getUsername();
    }

    @Test
    void test_listUsers() {
        List<User> users = new ArrayList<>();
        User user1 = new User("john123", "john123@mail.com", "123");
        User user2 = new User("jazzy-jeff", "some@mail.com", "password");
        User user3 = new User("expecttheunexpected", "ihavenomail@mail.com", "0000");
        users.add(user1);
        users.add(user2);
        users.add(user3);

        Mockito.when(userService.listUsers()).thenReturn(users);

        List<User> result = userService.listUsers();

        assertEquals(users, result);
    }

    @Test
    void test_listUsers_noUsers() {
        List<User> users = new ArrayList<>();

        Mockito.when(userService.listUsers()).thenReturn(users);

        List<User> result = userService.listUsers();

        assertEquals(users, result);
    }

    @Test
    void test_getUserById() {
        User user = new User("john123", "john123@mail.com", "123");
        Long id = user.getId();

        Mockito.when(userService.getUserById(id)).thenReturn(user);

        User result = userService.getUserById(id);

        assertEquals("john123", result.getUsername());
        assertEquals("john123@mail.com", result.getEmail());
        assertEquals("123", result.getPassword());
    }

    @Test
    void test_isUsernameInDatabase_true() {
        User user = new User("john123", "john123@mail.com", "123");

        Mockito.when(userService.isUsernameInDatabase(user.getUsername())).thenReturn(true);

        boolean result = userService.isUsernameInDatabase(user.getUsername());

        assertTrue(result);
    }

    @Test
    void test_isUsernameInDatabase_false() {
        User user = new User("john123", "john123@mail.com", "123");

        Mockito.when(userService.isUsernameInDatabase(user.getUsername())).thenReturn(false);

        boolean result = userService.isUsernameInDatabase(user.getUsername());

        assertFalse(result);
    }

    @Test
    void test_isEmailInDatabase_true() {
        User user = new User("john123", "john123@mail.com", "123");

        Mockito.when(userService.isEmailInDatabase(user.getEmail())).thenReturn(true);

        boolean result = userService.isEmailInDatabase(user.getEmail());

        assertTrue(result);
    }

    @Test
    void test_isEmailInDatabase_false() {
        User user = new User("john123", "john123@mail.com", "123");

        Mockito.when(userService.isEmailInDatabase(user.getEmail())).thenReturn(false);

        boolean result = userService.isEmailInDatabase(user.getEmail());

        assertFalse(result);
    }
  
    @Test
    void test_updateUserProfile() throws AuthenticationException {
        when(httpServletRequest.getHeader(Mockito.<String>any())).thenReturn("helloworld@a.sk");
        assertThrows(AuthenticationException.class, () -> databaseUserService
                .updateUserProfile(new UserProfileRequestDto("name", "helloworld@a.sk", "123456789")));
        verify(httpServletRequest).getHeader(Mockito.<String>any());
    }

    @Test
    void test_updateUserProfile2() throws AuthenticationException {
        when(jwtTokenProvider.getUsernameFromToken(Mockito.<String>any())).thenReturn("pokemon");

        User user = new User();
        user.setEmail("helloworld@a.sk");
        user.setId(1L);
        user.setPassword("123456789");
        user.setUsername("pokemon");
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.existsByUsername(Mockito.<String>any())).thenReturn(true);
        when(userRepository.findByUsername(Mockito.<String>any())).thenReturn(ofResult);
        when(httpServletRequest.getHeader(Mockito.<String>any())).thenReturn("Bearer ");
        assertThrows(IllegalArgumentException.class, () -> databaseUserService
                .updateUserProfile(new UserProfileRequestDto("name", "helloworld@a.sk", "123456789")));
        verify(jwtTokenProvider).getUsernameFromToken(Mockito.<String>any());
        verify(userRepository).existsByUsername(Mockito.<String>any());
        verify(userRepository).findByUsername(Mockito.<String>any());
        verify(httpServletRequest).getHeader(Mockito.<String>any());
    }

    @Test
    void test_updateUserProfile_validInput() throws AuthenticationException {
        when(httpServletRequest.getHeader(Mockito.<String>any())).thenReturn("Bearer valid_token");

        User user = new User();
        user.setEmail("helloworld@a.sk");
        user.setId(1L);
        user.setPassword("123456789");
        user.setUsername("pokemon");
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findByUsername(Mockito.<String>any())).thenReturn(ofResult);

        UserProfileRequestDto validInput = new UserProfileRequestDto("new_username", "new_email@a.sk", "new_password");
        UserProfileResponseDto result = databaseUserService.updateUserProfile(validInput);

        assertEquals(user.getId(), result.getId());
        assertEquals(validInput.getName(), result.getName());
        assertEquals(validInput.getEmail(), result.getEmail());
        verify(httpServletRequest).getHeader(Mockito.<String>any());
    }
  
    @Test
    void test_updateUserProfile_invalidToken() {
        when(httpServletRequest.getHeader(Mockito.<String>any())).thenReturn(null);

        assertThrows(AuthenticationException.class, () -> databaseUserService
                .updateUserProfile(new UserProfileRequestDto("name", "helloworld@a.sk", "123456789")));

        verify(httpServletRequest).getHeader(Mockito.<String>any());
    }
}