package com.insuranceApp.controllers;

import com.insuranceApp.dto.*;
import com.insuranceApp.mappers.InsuredPersonMapper;
import com.insuranceApp.models.UserEntity;
import com.insuranceApp.security.SecurityUtil;
import com.insuranceApp.services.UserService;
import com.insuranceApp.services.impl.InsuranceServiceImpl;
import com.insuranceApp.services.impl.InsuredPersonServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/insuredPersons")
@RequiredArgsConstructor
public class InsuredPersonController {

    // admin získá všechny uživatele - po kliknutí je přesměrován na seznam pojistníků uživatele
    // uživatel získá všechny pojistníky, kteří mají sjednané pojištění skrze jeho účet

    private final InsuredPersonServiceImpl insuredPersonService;
    private final InsuranceServiceImpl insuranceService;
    private final UserService userService;
    private final int PAGE_SIZE = 5;

    @GetMapping("/{pageNo}")
    public String getAllInsuredPersons(@PathVariable int pageNo,
                                       Model model) {
        UserEntity user = new UserEntity();
        String username = SecurityUtil.getSessionUser();
        if (username != null) {
            user = userService.getByUsername(username);
        }

        InsuredPersonResponseDto page = new InsuredPersonResponseDto();
        if (user.getRoles().get(0).getName().equals("ADMIN")) {
            page = insuredPersonService.getAllInsuredPersons(pageNo, PAGE_SIZE);
        } else {
            page = insuredPersonService.getAllInsuredPersonsCreatedById(user.getId(), pageNo, PAGE_SIZE);
        }

        model.addAttribute("role", user.getRoles().get(0).getName());
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("pageSize", PAGE_SIZE);
        model.addAttribute("insuredPersons", page.getContent());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("activePage", "insured_persons");

        return "/insuredPersons";
    }

    @GetMapping("/createInsuredPerson")
    public String createInsuredPersonForm(Model model) {
        model.addAttribute("activePage", "insured_persons");
        model.addAttribute("insuredPersonForm", new InsuredPersonFormDto());
        model.addAttribute("header", "Přidej pojištěnce");
        return "/createInsuredPerson";
    }

    @PostMapping("/create")
    public String createInsuredPerson(@Valid @ModelAttribute("insuredPersonForm") InsuredPersonFormDto insuredPersonForm,
                                      BindingResult result,
                                      Model model) {
        boolean isEmail = insuredPersonService.isEmail(insuredPersonForm.getEmail());
        if (isEmail) {
            model.addAttribute("isEmail", true);
        }

        if (result.hasErrors() || isEmail) {
            model.addAttribute("insuredPersonForm", insuredPersonForm);
            return "/createInsuredPerson";
        }

        insuredPersonService.createInsuredPerson(insuredPersonForm);
        return "redirect:/insuredPersons/0?success";
    }

    @GetMapping("/{ipId}/editInsuredPerson")
    public String editInsuredPersonForm(@PathVariable("ipId") long insuredPersonId,
                                        @RequestParam(value = "detail") boolean detail,
                                        Model model) {
        InsuredPersonDto insuredPersonDto = insuredPersonService.getInsuredPersonById(insuredPersonId);
        model.addAttribute("activePage", "insured_persons");
        model.addAttribute("insuredPersonForm", InsuredPersonMapper.mapToInsuredPersonFormDto(insuredPersonDto));
        model.addAttribute("header", "Uprav pojištěnce");
        model.addAttribute("detail", detail);
        return "/editInsuredPerson";
    }

    @PostMapping("/{ipId}/edit")
    public String editInsuredPerson(@PathVariable("ipId") long insuredPersonId,
                                    @RequestParam(name = "detail") boolean detail,
                                    @Valid @ModelAttribute("insuredPersonForm") InsuredPersonFormDto insuredPersonForm,
                                    BindingResult result,
                                    Model model) {
        //Kontrola zda byl editován email a jestli ano, zda je již v databázi
        InsuredPersonDto insuredPersonDto = insuredPersonService.getInsuredPersonById(insuredPersonId);
        insuredPersonForm.setId(insuredPersonId);
        boolean isEmail = insuredPersonService.isEmail(insuredPersonForm.getEmail());
        if (isEmail && insuredPersonDto.getEmail().equals(insuredPersonForm.getEmail())) {
            isEmail = false;
        }

        if (isEmail) {
            model.addAttribute("emailError", "Uživatel s touto emailovou adresou je již registrován");
        }

        if (result.hasErrors() || isEmail) {
            return "/editInsuredPerson";
        }

        insuredPersonService.editInsuredPersonById(insuredPersonForm, insuredPersonId);

        if (detail) {
            return String.format("redirect:/insuredPersons/%d/detail/0?success", insuredPersonId);

        } else {
            return "redirect:/insuredPersons/0?success";
        }
    }


    @GetMapping("/{ipId}/delete")
    public String deleteInsuredPersonById(@PathVariable("ipId") long insuredPersonId,
                                          Model model) {
        model.addAttribute("activePage", "insured_persons");
        insuredPersonService.deleteInsuredPersonById(insuredPersonId);
        return "redirect:/insuredPersons/0?success";
    }

    @GetMapping("/{ipId}/detail/{pageNo}")
    public String getInsuredPersonDetail(@PathVariable("ipId") long insuredPersonId,
                                         @PathVariable int pageNo,
                                         Model model) {

        UserEntity user = new UserEntity();
        String username = SecurityUtil.getSessionUser();
        if (username != null) {
            user = userService.getByUsername(username);
        }

        InsuredPersonDto insuredPersonDto = insuredPersonService.getInsuredPersonById(insuredPersonId);
        InsuranceResponseDto page = insuranceService.getAllInsurancesByInsuredPersonId(insuredPersonId, pageNo, 3); //page size

        model.addAttribute("user", user);
        model.addAttribute("role", user.getRoles().get(0).getName());
        model.addAttribute("activePage", "insured_persons");
        model.addAttribute("ipId", insuredPersonId);
        model.addAttribute("insuredPerson", insuredPersonDto);
        model.addAttribute("insurances", page.getContent());
        model.addAttribute("pageNo", page.getPageNo());
        model.addAttribute("pageSize", page.getPageSize());
        model.addAttribute("totalPages", page.getTotalPages());
        return "/insuredPersonDetail";
    }
}
