package me.mmtr.noted;

import me.mmtr.noted.controller.AccountDeleteController;
import me.mmtr.noted.data.User;
import me.mmtr.noted.repository.UserRepository;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountDeleteController.class)
public class AccountDeleteControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void accountDeleteSuccessTest() throws Exception {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new User()));

        mockMvc.perform(post("/delete").param("id", "1"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"));

    }

    @Test
    public void accountDeleteUserNotFoundTest() throws Exception {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(post("/delete").param("id", "1"))
                .andExpect(status().isNotFound());
    }

    @TestConfiguration
    static class TestSecurityConfiguration {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                            .requestMatchers("error/404").permitAll()
                            .requestMatchers("/delete").permitAll()
                            .requestMatchers("/admin").permitAll())
                    .csrf(AbstractHttpConfigurer::disable);
            return http.build();
        }
    }
}
