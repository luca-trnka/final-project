package com.gfa.services;

import com.gfa.dtos.UserRequestDto;
import com.gfa.models.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseUserServiceTest {
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
    void test_updateUser() {
        User user = new User("john123", "john123@mail.com", "123");
        UserRequestDto userRequestDto = new UserRequestDto("john123", "john123@mail.com", "321");
        user.setPassword("321");

        Mockito.when(userService.updateUser(user.getId(), userRequestDto)).thenReturn(user);

        User result = userService.updateUser(user.getId(), userRequestDto);

        assertEquals("john123", result.getUsername());
        assertEquals("john123@mail.com", result.getEmail());
        assertEquals("321", result.getPassword());
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
}