package me.mmtr.noted;

import me.mmtr.noted.controller.RegistrationController;
import me.mmtr.noted.data.User;
import me.mmtr.noted.data.dto.UserDTO;
import me.mmtr.noted.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RegistrationController.class)
public class RegistrationControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void shouldReturnViewWithAttribute() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", new UserDTO()));
    }

    @Test
    public void shouldReturnAnErrorWhenUserAlreadyExists() throws Exception {
        Mockito.when(userService.findUserByUsername(Mockito.anyString()))
                .thenReturn(new User(1L,
                        "username",
                        "password",
                        Collections.emptyList(),
                        Collections.emptyList()));

        mockMvc.perform(post("/register")
                        .param("username", "username")
                        .param("password", "password")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeHasFieldErrors("user", "username"));
    }

    @Test
    public void shouldSuccessfullyRegisterUserAndRedirectToAdminPanel() throws Exception {
        Mockito.when(userService.findUserByUsername(Mockito.anyString())).thenReturn(null);

        mockMvc.perform(post("/register")
                        .param("username", "username")
                        .param("password", "password")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"));
    }

    @TestConfiguration
    static class TestSecurityConfiguration {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                            .requestMatchers("/register").permitAll()
                    )
                    .csrf(AbstractHttpConfigurer::disable);
            return http.build();
        }
    }
}
