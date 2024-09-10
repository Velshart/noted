package me.mmtr.noted;

import me.mmtr.noted.controller.LoginController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoginController.class)
public class LoginControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnLoginViewWithoutParameters() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeDoesNotExist("error", "logout"));
    }

    @Test
    public void shouldReturnLoginViewWithError() throws Exception {
        mockMvc.perform(get("/login").param("error", "error"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "Incorrect username or password provided"));
    }

    @Test
    public void shouldReturnLoginViewWithLogoutMessage() throws Exception {
        mockMvc.perform(get("/login").param("logout", "logout"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("logout"))
                .andExpect(model().attribute("logout", "logout"));
    }

    @TestConfiguration
    static class TestSecurityConfiguration {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.authorizeHttpRequests(authorizeRequests -> {
            }).formLogin(formLogin -> formLogin.loginPage("/login").permitAll());

            return http.build();
        }
    }
}
