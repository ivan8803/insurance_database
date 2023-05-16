package com.insuranceApp.controllers;

import com.insuranceApp.dto.LoginDto;
import com.insuranceApp.dto.RegistrationDto;
import com.insuranceApp.models.UserEntity;
import com.insuranceApp.services.impl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserServiceImpl userService;

    @GetMapping("/register")
    public String getRegisterForm(Model model) {
        RegistrationDto user = new RegistrationDto();
        model.addAttribute("activePage", "registration");
        model.addAttribute("user", user);
        return "/register";
    }

    @GetMapping("/login")
    public String loginPage(@ModelAttribute("user") LoginDto user, Model model) {

        model.addAttribute("activePage", "login");
        return "/login";
    }

    @PostMapping("/register/save")
    public String register(@Valid @ModelAttribute("user") RegistrationDto user,
                           BindingResult result, Model model) {

        UserEntity existingUsername = userService.getByUsername(user.getUsername());
        if (existingUsername != null && existingUsername.getUsername() != null && !existingUsername.getUsername().isEmpty()) {
            return "redirect:/register?fail";
        }

        UserEntity existingUserEmail = userService.getByEmail(user.getEmail());
        if (existingUserEmail != null && existingUserEmail.getEmail() != null && !existingUserEmail.getEmail().isEmpty()) {
            return "redirect:/register?fail";
        }

        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "/register";
        }

        userService.saveUser(user);
        return "redirect:/?register";
    }
}
