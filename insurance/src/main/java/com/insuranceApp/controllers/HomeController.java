package com.insuranceApp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String getHome(Model model) {
        model.addAttribute("activePage", "home");
        return "/index";
    }

    @GetMapping("/aboutApp")
    public String getAboutApp(Model model) {
        model.addAttribute("activePage", "aboutApp");
        return "/aboutApp";
    }

    @GetMapping("/contact")
    public String getContact(Model model) {
        model.addAttribute("activePage", "contact");
        return "/contact";
    }
}
