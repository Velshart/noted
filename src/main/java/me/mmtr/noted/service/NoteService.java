package me.mmtr.noted.service;

import me.mmtr.noted.data.Note;

import java.util.List;
import java.util.Optional;

public interface NoteService {
    void saveOrUpdate(Note note);
    Optional<Note> getById(Long id);
    List<Note> getAll();
    void delete(Long id);
}
