package me.mmtr.noted.controller;

import me.mmtr.noted.data.Note;
import me.mmtr.noted.service.NoteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping(path = "/note")
public class NoteController {
    private final NoteService NOTE_SERVICE;

    public NoteController(NoteService noteService) {
        this.NOTE_SERVICE = noteService;
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("note", new Note());
        return "add-note";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute Note note) {
        this.NOTE_SERVICE.saveOrUpdate(note);
        return "redirect:/home";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id, Model model) {
        Optional<Note> noteOptional = NOTE_SERVICE.getById(id);
        if (noteOptional.isEmpty()) {
            return "redirect:/home";
        }
        model.addAttribute("note", noteOptional.get());
        return "add-note";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Note note) {
        this.NOTE_SERVICE.saveOrUpdate(note);
        return "redirect:/home";
    }


    @PostMapping("delete/{id}")
    public String delete(@PathVariable Long id) {
        this.NOTE_SERVICE.delete(id);
        return "redirect:/home";
    }

}
