package edu.duke.ece568.mini_ups.controller;

import edu.duke.ece568.mini_ups.dto.UserDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam String username, @RequestParam String password) {

        if ("admin".equals(username) && "password".equals(password)) {
            return "redirect:/home";
        } else {
            return "login";
        }
    }

    @PostMapping("/register")
    public String handleRegistration(@ModelAttribute UserDto userDto) {
        return "login";
    }
}
