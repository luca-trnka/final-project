package com.gfa.controllers;

import com.gfa.models.User;
import com.gfa.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EmailControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserRepository userRepository;

    @Test
    void test_store_emptyEmail() throws Exception {
        User user = new User();
        user.setUsername("user");
        user.setEmail("user@mail.com");
        user.setPassword("password");
        user.setVerified(false);
        user.setVerificationToken(0000L);
        user.setVerificationTokenExpiresAt(0L);
        userRepository.save(user);

        mvc.perform(get("/email/verify/0000")                )
                .andExpect(status().is(400))
                .andExpect(jsonPath("error", is("Token expired!")))
        ;
    }
}