package me.mmtr.noted;

import me.mmtr.noted.controller.NoteController;
import me.mmtr.noted.data.Note;
import me.mmtr.noted.data.dao.NoteDAO;
import me.mmtr.noted.service.NoteService;
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

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NoteController.class)
public class NoteControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoteService noteService;

    @MockBean
    private NoteDAO noteDAO;

    @Test
    public void shouldReturnAViewWithAttribute() throws Exception {
        mockMvc.perform(get("/note/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("note"))
                .andExpect(model().attributeExists("note"))
                .andExpect(model().attribute("note", new Note()));
    }

    @Test
    public void shouldSaveNewNoteAndRedirectToHome() throws Exception {
        Mockito.when(noteDAO.getById(Mockito.anyLong())).thenReturn(null);

        mockMvc.perform(post("/note/add")
                        .param("id", "1")
                        .param("title", "Title")
                        .param("text", "Text")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));

        Mockito.verify(noteService).saveOrUpdate(Mockito.any(Note.class));
    }

    @Test
    public void shouldUpdateNoteSuccessfully() throws Exception {
        Note newNote = new Note(1L, "Title", "Text");

        Mockito.when(noteService.getById(Mockito.anyLong())).thenReturn(Optional.of(newNote));

        mockMvc.perform(get("/note/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("note"))
                .andExpect(model().attributeExists("note"))
                .andExpect(model().attribute("note", newNote));
    }

    @Test
    public void shouldRedirectToHomeWhenNoteDoesNotExist() throws Exception {
        Mockito.when(noteService.getById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/note/update/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    public void shouldUpdateNoteAndRedirectToHome() throws Exception {
        Mockito.doNothing().when(noteService).saveOrUpdate(Mockito.any(Note.class));

        mockMvc.perform(post("/note/update/1")
                        .param("title", "Updated Title")
                        .param("text", "Updated Content"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));

        Mockito.verify(noteService).saveOrUpdate(Mockito.any(Note.class));
    }

    @Test
    public void shouldDeleteNoteSuccessfullyAndRedirectToHome() throws Exception {
        mockMvc.perform(post("/note/delete").param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));

        Mockito.verify(noteService).delete(Mockito.eq(1L));
    }

    @TestConfiguration
    static class TestSecurityConfiguration {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                            .requestMatchers("/note/**").permitAll()
                    )
                    .csrf(AbstractHttpConfigurer::disable);
            return http.build();
        }
    }
}
