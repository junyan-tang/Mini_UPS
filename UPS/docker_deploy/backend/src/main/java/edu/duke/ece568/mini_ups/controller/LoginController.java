package edu.duke.ece568.mini_ups.controller;

import edu.duke.ece568.mini_ups.dto.UserDto;
import edu.duke.ece568.mini_ups.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {
    private final UserService UserService;

    @Autowired
    public LoginController(UserService UserService) {
        this.UserService = UserService;
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/register")
    public String handleRegistration(@ModelAttribute UserDto userDto) {
        UserService.registerNewUser(userDto);
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    @GetMapping("/logout")
    public String logout() {
        return "home";
    }
}
