package com.gfa.controllers;

import com.gfa.models.Role;
import com.gfa.repositories.RoleRepository;
import com.gfa.services.RoleService;
import org.junit.jupiter.api.BeforeEach;
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
class RoleRestControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void beforeEach() {
        roleRepository.deleteAll();
    }

    @Test
    void test_index() throws Exception {
        mvc.perform(get("/roles"))
                .andExpect(status().is(200));
    }

    @Test
    public void test_index_withRoles() throws Exception {
       roleRepository.save(new Role("admin"));
        roleRepository.save(new Role("supervisor"));
        mvc.perform(get("/roles"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].role", is("admin")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].role", is("supervisor")));
    }

    @Test
    public void test_store_ok() throws Exception {
        mvc.perform(post("/roles/")
                        .content("{\"role\":\"admin\"}")
                        .contentType("application/json"))
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.role", is("admin")));
    }

    @Test
    public void test_store_emptyRole() throws Exception {
        mvc.perform(post("/roles/")
                        .content("{\"role\":\"\"}")
                        .contentType("application/json"))
                .andExpect(status().is(400))
                .andExpect(jsonPath("error", is("Role is required")));
    }

    @Test
    public void test_store_noRole() throws Exception {
        mvc.perform(post("/roles/")
                        .content("{}")
                        .contentType("application/json"))
                .andExpect(status().is(400))
                .andExpect(jsonPath("error", is("Role is required")));
    }

    @Test
    void test_store_roleAlreadyExists() throws Exception {
        roleService.createRole(new Role("admin"));
        mvc.perform(post("/roles/")
                        .content("{\"role\":\"admin\"}")
                        .contentType("application/json"))
                .andExpect(status().is(409))
                .andExpect(jsonPath("error", is("Role already exists")));
    }

    @Test
    public void test_show_ok() throws Exception {
        int id = Math.toIntExact(roleService.createRole(new Role("admin")).getId());
        mvc.perform(get("/roles/" + id))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.role", is("admin")));
    }

    @Test
    public void test_show_invalidId() throws Exception {
        mvc.perform(get("/roles/-1"))
                .andExpect(status().is(400))
                .andExpect(jsonPath("error", is("Invalid id")));
    }

    @Test
    public void test_show_noRoleFound() throws Exception {
        mvc.perform(get("/roles/1"))
                .andExpect(status().is(404))
                .andExpect(jsonPath("error", is("Role not found")));
    }

    @Test
    public void test_update_ok() throws Exception {
        int id = Math.toIntExact(roleService.createRole(new Role("admin")).getId());

        mvc.perform(patch("/roles/" + id)
                        .content("{\"role\":\"updatedAdmin\"}")
                        .contentType("application/json"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.role", is("updatedAdmin")));
    }

    @Test
    public void test_update_invalidId() throws Exception {
        mvc.perform(patch("/roles/-1")
                        .content("{\"role\":\"updatedAdmin\"}")
                        .contentType("application/json"))
                .andExpect(status().is(400))
                .andExpect(jsonPath("error", is("Invalid id")));
    }

    @Test
    public void test_update_noRoleFound() throws Exception {
        mvc.perform(patch("/roles/1")
                        .content("{\"role\":\"updatedAdmin\"}")
                        .contentType("application/json"))
                .andExpect(status().is(404))
                .andExpect(jsonPath("error", is("Role not found")));
    }

    @Test
    public void test_destroy_ok() throws Exception {
        Role role = new Role("admin");
        roleService.createRole(role);
        mvc.perform(delete("/roles/" + role.getId()))
                .andExpect(status().is(201));
    }

    @Test
    public void test_destroy_invalidId() throws Exception {
        mvc.perform(delete("/roles/-1")
                        .contentType("application/json"))
                .andExpect(status().is(400))
                .andExpect(jsonPath("error", is("Invalid id")));
    }

    @Test
    public void test_destroy_noRoleFound() throws Exception {
        mvc.perform(delete("/roles/1")
                        .contentType("application/json"))
                .andExpect(status().is(404))
                .andExpect(jsonPath("error", is("Role not found")));
    }
}
