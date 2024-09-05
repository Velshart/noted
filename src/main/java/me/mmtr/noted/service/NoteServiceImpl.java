package me.mmtr.noted.service;

import jakarta.transaction.Transactional;
import me.mmtr.noted.data.Note;
import me.mmtr.noted.data.dao.interfaces.INoteDAO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoteServiceImpl implements NoteService {
    private final INoteDAO NOTE_DAO;

    public NoteServiceImpl(INoteDAO noteDAO) {
        this.NOTE_DAO = noteDAO;
    }

    @Override
    @Transactional
    public void saveOrUpdate(Note note) {
        this.NOTE_DAO.saveOrUpdate(note);
    }

    @Override
    @Transactional
    public Optional<Note> getById(Long id) {
        return this.NOTE_DAO.getById(id);
    }

    @Override
    @Transactional
    public List<Note> getAll() {
        return this.NOTE_DAO.getAll();
    }

    @Override
    @Transactional
    public void delete(Long id) {
       this.NOTE_DAO.delete(id);
    }
}
