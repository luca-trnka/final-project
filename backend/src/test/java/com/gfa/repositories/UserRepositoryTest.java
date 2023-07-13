package com.gfa.repositories;

import com.gfa.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


class UserRepositoryTest {
    private User user;
    private UserRepository userRepository;
    
    @BeforeEach
    void init() {
        user = new User();
        user.setUsername("username");
        user.setEmail("email");
        user.setPassword("password");
        userRepository = Mockito.mock(UserRepository.class);
    }

    @Test
    void test_saveUser() {
        Mockito.when(userRepository.save(user)).thenReturn(user);

        User result = userRepository.save(user);

        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getPassword(), result.getPassword());
    }

    @Test
    void test_retrieveUser() {
        user.setId(1L);
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        Optional<User> result = userRepository.findById(user.getId());

        assertTrue(result.isPresent());
        assertNotNull(result.get().getId());
        assertEquals(user.getUsername(), result.get().getUsername());
        assertEquals(user.getEmail(), result.get().getEmail());
        assertEquals(user.getPassword(), result.get().getPassword());
    }
}