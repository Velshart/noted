package me.mmtr.noted.controller;

import me.mmtr.noted.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminPanelController {

    private final UserRepository USER_REPOSITORY;

    public AdminPanelController(UserRepository userRepository) {
        this.USER_REPOSITORY = userRepository;
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("users", USER_REPOSITORY.findAll());
        return "admin";
    }
}
