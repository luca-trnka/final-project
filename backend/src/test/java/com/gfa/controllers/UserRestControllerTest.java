package com.gfa.controllers;

import com.gfa.models.User;
import com.gfa.repositories.UserRepository;
import com.gfa.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
class UserRestControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
    }

    @Test
    void test_index() throws Exception {
        mvc.perform(get("/users"))
                .andExpect(status().is(200));
    }

    @Test
    void test_index_withUsers() throws Exception {
        userService.createUser(new User("john123", "john123@mail.com", "123"));
        userService.createUser(new User("jazzy-jeff", "some@mail.com", "password"));
        userService.createUser(new User("expecttheunexpected", "ihavenomail@mail.com", "0000"));

        mvc.perform(get("/users"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$[0].username", is("john123")))
                .andExpect(jsonPath("$[0].email", is("john123@mail.com")))
                .andExpect(jsonPath("$[1].username", is("jazzy-jeff")))
                .andExpect(jsonPath("$[1].email", is("some@mail.com")))
                .andExpect(jsonPath("$[2].username", is("expecttheunexpected")))
                .andExpect(jsonPath("$[2].email", is("ihavenomail@mail.com")))
        ;
    }

    @Test
    void test_store_noUsername() throws Exception {
        mvc.perform(post("/users/")
                        .content("{\"email\":\"mikesmith@gmail.com\",\"password\":\"P4$$word\"}")
                        .contentType("application/json")
                )
                .andExpect(status().is(400))
                .andExpect(jsonPath("error", is("Username is required")))
        ;
    }

    @Test
    void test_store_emptyUsername() throws Exception {
        mvc.perform(post("/users/")
                        .content("{\"username\":\"\",\"email\":\"mikesmith@gmail.com\",\"password\":\"P4$$word\"}")
                        .contentType("application/json")
                )
                .andExpect(status().is(400))
                .andExpect(jsonPath("error", is("Username is required")))
        ;
    }

    @Test
    void test_store_noEmail() throws Exception {
        mvc.perform(post("/users/")
                        .content("{\"username\":\"mikesmith\",\"password\":\"P4$$word\"}")
                        .contentType("application/json")
                )
                .andExpect(status().is(400))
                .andExpect(jsonPath("error", is("Email is required")))
        ;
    }

    @Test
    void test_store_emptyEmail() throws Exception {
        mvc.perform(post("/users/")
                        .content("{\"username\":\"mikesmith\",\"email\":\"\",\"password\":\"P4$$word\"}")
                        .contentType("application/json")
                )
                .andExpect(status().is(400))
                .andExpect(jsonPath("error", is("Email is required")))
        ;
    }

    @Test
    void test_store_invalidEmail() throws Exception {
        mvc.perform(post("/users/")
                        .content("{\"username\":\"mikesmith\",\"email\":\"thisisnotvalidmail\",\"password\":\"P4$$word\"}")
                        .contentType("application/json")
                )
                .andExpect(status().is(400))
                .andExpect(jsonPath("error", is("Invalid email")))
        ;
    }

    @Test
    void test_store_noPassword() throws Exception {
        mvc.perform(post("/users/")
                        .content("{\"username\":\"mikesmith\",\"email\":\"mikesmith@gmail.com\"}")
                        .contentType("application/json")
                )
                .andExpect(status().is(400))
                .andExpect(jsonPath("error", is("Password is required")))
        ;
    }

    @Test
    void test_store_emptyPassword() throws Exception {
        mvc.perform(post("/users/")
                        .content("{\"username\":\"mikesmith\",\"email\":\"mikesmith@gmail.com\",\"password\":\"\"}")
                        .contentType("application/json")
                )
                .andExpect(status().is(400))
                .andExpect(jsonPath("error", is("Password is required")))
        ;
    }

    @Test
    void test_store_shortPassword() throws Exception {
        mvc.perform(post("/users/")
                        .content("{\"username\":\"mikesmith\",\"email\":\"mikesmith@gmail.com\",\"password\":\"P4$$\"}")
                        .contentType("application/json")
                )
                .andExpect(status().is(400))
                .andExpect(jsonPath("error", is("Password too short")))
        ;
    }

    @Test
    void test_store_usernameAlreadyExists() throws Exception {
        userService.createUser(new User("mikesmith", "mikesmith@gmail.com", "P4$$word"));

        mvc.perform(post("/users/")
                        .content("{\"username\":\"mikesmith\",\"email\":\"mikesmith2@gmail.com\",\"password\":\"P4$$word\"}")
                        .contentType("application/json")
                )
                .andExpect(status().is(409))
                .andExpect(jsonPath("error", is("Username already exists")))
        ;
    }

    @Test
    void test_store_emailAlreadyExists() throws Exception {
        userService.createUser(new User("mikesmith", "mikesmith@gmail.com", "P4$$word"));

        mvc.perform(post("/users/")
                        .content("{\"username\":\"mikesmith2\",\"email\":\"mikesmith@gmail.com\",\"password\":\"password\"}")
                        .contentType("application/json")
                )
                .andExpect(status().is(409))
                .andExpect(jsonPath("error", is("Email already exists")))
        ;
    }

    @Test
    void test_store_success() throws Exception {
        mvc.perform(post("/users/")
                        .content("{\"username\":\"mikesmith\",\"email\":\"mikesmith@gmail.com\",\"password\":\"P4$$word\"}")
                        .contentType("application/json")
                )
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.username", is("mikesmith")))
                .andExpect(jsonPath("$.email", is("mikesmith@gmail.com")))
        ;
    }

    @Test
    void test_show_invalidId() throws Exception {
        mvc.perform(get("/users/0"))
                .andExpect(status().is(400))
                .andExpect(jsonPath("error", is("Invalid id")))
        ;
    }

    @Test
    void test_show_userNotFound() throws Exception {
        String address = "/users/" + userRepository.findAll().size() + 1;

        mvc.perform(get(address))
                .andExpect(status().is(404))
                .andExpect(jsonPath("error", is("User not found")))
        ;
    }

    @Test
    void test_show_success() throws Exception {
        userService.createUser(new User("mikesmith", "mikesmith@gmail.com", "P4$$word"));

        mvc.perform(get("/users/1"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is("mikesmith")))
                .andExpect(jsonPath("$.email", is("mikesmith@gmail.com")))
        //.andExpect(jsonPath("$.verifiedAt").exists())
        ;
    }

    @Test
    void test_update_invalidId() throws Exception {
        mvc.perform(patch("/users/0")
                .content("{\"username\":\"mikesmith\",\"email\":\"mikesmith@gmail.com\",\"password\":\"P4$$word\"}")
                .contentType("application/json")
                )
                .andExpect(status().is(400))
                .andExpect(jsonPath("error", is("Invalid id")))
        ;
    }

    @Test
    void test_update_userNotFound() throws Exception {
        String address = "/users/" + userRepository.findAll().size() + 1;

        mvc.perform(patch(address)
                .content("{\"username\":\"mikesmith\",\"email\":\"mikesmith@gmail.com\",\"password\":\"P4$$word\"}")
                .contentType("application/json")
                )
                .andExpect(status().is(404))
                .andExpect(jsonPath("error", is("User not found")))
        ;
    }

    @Test
    void test_update_invalidData() throws Exception {
        int id = Math.toIntExact(userService.createUser(new User("mikesmith", "mikesmith@gmail.com", "P4$$word")).getId());
        String address = "/users/" + id;

        mvc.perform(patch(address)
                        .content("{\"username\":\"mikesmith\",\"email\":\"mikesmith@gmail.com\",\"password\":\"P4$$\"}")
                        .contentType("application/json")
                )
                .andExpect(status().is(400))
                .andExpect(jsonPath("error", is("Invalid data")))
        ;
    }

    @Test
    void test_update_success() throws Exception {
        int id = Math.toIntExact(userService.createUser(new User("mikesmith", "mikesmith@gmail.com", "P4$$word")).getId());
        String address = "/users/" + id;

        mvc.perform(patch(address)
                        .content("{\"username\":\"mikesmith2\",\"email\":\"mikesmith@gmail.com\",\"password\":\"P4$$word\"}")
                        .contentType("application/json")
                )
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.username", is("mikesmith2")))
                .andExpect(jsonPath("$.email", is("mikesmith@gmail.com")))
        ;
    }

    @Test
    void test_destroy_invalidId() throws Exception {
        mvc.perform(delete("/users/0"))
                .andExpect(status().is(400))
                .andExpect(jsonPath("error", is("Invalid id")))
        ;
    }

    @Test
    void test_destroy_userNotFound() throws Exception {
        String address = "/users/" + userRepository.findAll().size() + 1;

        mvc.perform(delete(address))
                .andExpect(status().is(404))
                .andExpect(jsonPath("error", is("User not found")))
        ;
    }

    @Test
    void test_destroy_success() throws Exception {
        User user = new User("mikesmith", "mikesmith@gmail.com", "P4$$word");
        userService.createUser(user);
        String address = "/users/" + user.getId();

        mvc.perform(delete(address))
                .andExpect(status().is(201))
        ;
    }
}