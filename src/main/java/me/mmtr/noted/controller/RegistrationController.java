package me.mmtr.noted.controller;

import me.mmtr.noted.data.User;
import me.mmtr.noted.data.dto.UserDTO;
import me.mmtr.noted.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {
    private final UserService USER_SERVICE;

    public RegistrationController(UserService userService) {
        this.USER_SERVICE = userService;
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new UserDTO());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute(name = "user") UserDTO userDTO,
                           Model model,
                           BindingResult bindingResult) {

        User user = USER_SERVICE.findUserByUsername(userDTO.getUsername());

        if (user != null && user.getUsername() != null && !user.getUsername().isEmpty()) {
            bindingResult.rejectValue("username",
                    "exists",
                    "This username is already in use");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("user", userDTO);
            return "register";
        }

        USER_SERVICE.saveUser(userDTO);

        return "redirect:/admin";
    }
}
