package me.mmtr.noted.controller;

import me.mmtr.noted.data.User;
import me.mmtr.noted.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AccountDeleteController {
    private final UserRepository USER_REPOSITORY;

    public AccountDeleteController(UserRepository USER_REPOSITORY) {
        this.USER_REPOSITORY = USER_REPOSITORY;
    }

    @GetMapping("/delete")
    public String delete(Model model) {
        model.addAttribute("user", new User());
        return "delete";
    }

    @PostMapping("/delete")
    public String delete(@ModelAttribute(name = "user") User user,
                         Model model,
                         BindingResult bindingResult) {

        User userToDelete = USER_REPOSITORY.findByUsername(user.getUsername());

        if (userToDelete == null || userToDelete.getUsername().isEmpty()) {
            bindingResult.rejectValue("username",
                    "does not exist",
                    "User with provided username does not exist");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            return "delete";
        }

        if (userToDelete != null) {
            USER_REPOSITORY.delete(userToDelete);
        }

        return "redirect:/admin";
    }
}
