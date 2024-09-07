package me.mmtr.noted.controller;

import me.mmtr.noted.service.NoteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final NoteService NOTE_SERVICE;

    public HomeController(NoteService noteService) {
        this.NOTE_SERVICE = noteService;
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("notes", this.NOTE_SERVICE.getAll());
        return "home";
    }
}
