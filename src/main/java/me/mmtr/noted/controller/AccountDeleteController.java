package me.mmtr.noted.controller;

import me.mmtr.noted.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

@Controller
public class AccountDeleteController {
    private final UserRepository USER_REPOSITORY;

    public AccountDeleteController(UserRepository USER_REPOSITORY) {
        this.USER_REPOSITORY = USER_REPOSITORY;
    }

    @PostMapping("/delete")
    public String delete(@RequestParam Long id) {
        this.USER_REPOSITORY.delete(USER_REPOSITORY.findById(id).orElseThrow());
        return "redirect:/admin";
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNoSuchElementException(NoSuchElementException e) {
        return "redirect:/admin";
    }
}
