package com.gfa.controllers;

import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gfa.dtos.LoginRequestDto;
import com.gfa.dtos.UserRequestDto;
import com.gfa.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {AuthController.class})
@ExtendWith(SpringExtension.class)
class AuthControllerTest {
    @Autowired
    private AuthController authController;

    @MockBean
    private UserService userService;

    @Test
    void login_success() throws Exception {
        when(userService.login(Mockito.<LoginRequestDto>any())).thenReturn("Login");
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.post("/login")
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content(objectMapper.writeValueAsString(new LoginRequestDto()));
        MockMvcBuilders.standaloneSetup(authController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Login"));
    }

    /**
     * Method under test: {@link AuthController#loginUser(LoginRequestDto)}
     */
    @Test
    void testLoginUser() throws Exception {
        // Arrange
        // TODO: Populate arranged inputs
        Object[] uriVars = new Object[]{};
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.post("/login", uriVars)
                .contentType(MediaType.APPLICATION_JSON);
        LoginRequestDto loginRequestDto = new LoginRequestDto("janedoe", "iloveyou");

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content(objectMapper.writeValueAsString(loginRequestDto));
        Object[] controllers = new Object[]{authController};
        MockMvc buildResult = MockMvcBuilders.standaloneSetup(controllers).build();

        // Act
        ResultActions actualPerformResult = buildResult.perform(requestBuilder);

        // Assert
        // TODO: Add assertions on result
    }

    /**
     * Method under test: {@link AuthController#registerUser(UserRequestDto)}
     */
    @Test
    void testRegisterUser() throws Exception {
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content(objectMapper.writeValueAsString(new UserRequestDto("janedoe", "jane.doe@example.org", "iloveyou")));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(authController).build().perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"error\":\"Type definition error: [simple type, class com.gfa.dtos.UserRequestDto]; nested exception"
                                        + " is com.fasterxml.jackson.databind.exc.InvalidDefinitionException: Cannot construct instance of"
                                        + " `com.gfa.dtos.UserRequestDto` (no Creators, like default constructor, exist): cannot deserialize from"
                                        + " Object value (no delegate- or property-based Creator)\\n at [Source: (org.springframework.util.StreamUtils"
                                        + "$NonClosingInputStream); line: 1, column: 2]\"}"));
    }

    /**
     * Method under test: {@link AuthController#registerUser(UserRequestDto)}
     */
    @Test
    void testRegisterUser2() throws Exception {
        // Arrange
        // TODO: Populate arranged inputs
        Object[] uriVars = new Object[]{};
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.post("/register", uriVars)
                .contentType(MediaType.APPLICATION_JSON);
        UserRequestDto userRequestDto = new UserRequestDto("janedoe", "jane.doe@example.org", "iloveyou");

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content(objectMapper.writeValueAsString(userRequestDto));
        Object[] controllers = new Object[]{authController};
        MockMvc buildResult = MockMvcBuilders.standaloneSetup(controllers).build();

        // Act
        ResultActions actualPerformResult = buildResult.perform(requestBuilder);

        // Assert
        // TODO: Add assertions on result
    }
}
