package me.mmtr.noted.data.dao.interfaces;

import me.mmtr.noted.data.Note;

import java.util.List;
import java.util.Optional;

public interface INoteDAO {
    Optional<Note> getById(Long id);
    List<Note> getAll();
    void saveOrUpdate(Note note);
    void delete(Long id);
}
