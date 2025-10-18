package org.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class ApplicationController {

    @GetMapping("/")
    public String root(Principal principal, Model model) {
        if (principal != null) {
            model.addAttribute("username", principal.getName());
            return "redirect:/discussions";
        }
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}