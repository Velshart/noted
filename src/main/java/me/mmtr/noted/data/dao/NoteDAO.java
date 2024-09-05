package me.mmtr.noted.data.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import me.mmtr.noted.data.Note;
import me.mmtr.noted.data.dao.interfaces.INoteDAO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class NoteDAO implements INoteDAO {

    @PersistenceContext
    private EntityManager entityManager;
    private final String GET_ALL_JPQL = "FROM me.mmtr.noted.data.Note";
    private final String GET_BY_ID_JPQL = "SELECT n FROM me.mmtr.noted.data.Note n WHERE n.id = :id";

    public NoteDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Note> getById(Long id) {
        TypedQuery<Note> query = entityManager.createQuery(GET_BY_ID_JPQL, Note.class);
        query.setParameter("id", id);

        try {
            return Optional.of(query.getSingleResult());
        }catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    @Override
    public List<Note> getAll() {
        TypedQuery<Note> query = entityManager.createQuery(GET_ALL_JPQL, Note.class);
        return query.getResultList();
    }

    @Override
    public void saveOrUpdate(Note note) {
        if (getById(note.getId()).isEmpty()) {
            entityManager.persist(note);
        }else {
            entityManager.merge(note);
        }
    }

    @Override
    public void delete(Long id) {
        getById(id).ifPresent(note -> entityManager.remove(note));

    }
}
