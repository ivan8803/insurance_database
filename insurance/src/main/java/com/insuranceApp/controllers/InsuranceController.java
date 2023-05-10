package com.insuranceApp.controllers;

import com.insuranceApp.dto.InsuranceDto;
import com.insuranceApp.dto.InsuranceResponseDto;
import com.insuranceApp.dto.InsuredPersonDto;
import com.insuranceApp.models.InsuranceType;
import com.insuranceApp.models.UserEntity;
import com.insuranceApp.repository.UserRepository;
import com.insuranceApp.security.SecurityUtil;
import com.insuranceApp.services.UserService;
import com.insuranceApp.services.impl.InsuranceServiceImpl;
import com.insuranceApp.services.impl.InsuredPersonServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class InsuranceController {

    private final InsuranceServiceImpl insuranceService;
    private final InsuredPersonServiceImpl insuredPersonService;
    private final UserService userService;
    private final int PAGE_SIZE = 5;

    @GetMapping("/insurances/{pageNo}")
    public String getAllInsurances(@PathVariable int pageNo,
                                   Model model) {

        UserEntity user = new UserEntity();
        String username = SecurityUtil.getSessionUser();
        if (username != null) {
            user = userService.getByUsername(username);
        }

        InsuranceResponseDto page = new InsuranceResponseDto();
        if (user.getRoles().get(0).getName().equals("ADMIN")){
            page = insuranceService.getAllInsurances(pageNo, PAGE_SIZE);
        } else {
            page = insuranceService.getAllInsurancesCreatedBy(user.getId(), pageNo, PAGE_SIZE);
        }

        model.addAttribute("user", user);
        model.addAttribute("role", user.getRoles().get(0).getName());
        model.addAttribute("insurances", page.getContent());
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("pageSize", PAGE_SIZE);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("title", "Pojištění");
        model.addAttribute("activePage", "insurances");
        return "/insurances";
    }

    @GetMapping("/insuredPersons/{ipId}/createInsurance")
    public String createInsuranceForm(@PathVariable("ipId") long insuredPersonId, Model model) {

        InsuranceType[] insuranceTypes = InsuranceType.values();
        model.addAttribute("insuranceTypes", insuranceTypes);
        model.addAttribute("activePage", "insurances");
        model.addAttribute("ipId", insuredPersonId);
        model.addAttribute("insuranceForm", new InsuranceDto());

        return "/createInsurance";
    }

    @PostMapping("/insuredPersons/{ipId}/insurances/create")
    public String createInsurance(@PathVariable("ipId") long insuredPersonId,
                                  @Valid @ModelAttribute("insuranceForm") InsuranceDto insuranceForm,
                                  BindingResult result,
                                  Model model) {

        InsuranceType[] insuranceTypes = InsuranceType.values();
        model.addAttribute("insuranceTypes", insuranceTypes);
        model.addAttribute("activePage", "insurances");

        if (result.hasErrors()) {
            model.addAttribute("insuranceForm", insuranceForm);
            return "/createInsurance";
        }

        insuranceService.createInsurance(insuredPersonId, insuranceForm);

        return String.format("redirect:/insuredPersons/%d/detail/0?success", insuredPersonId);
    }

    @GetMapping("/insuredPersons/{ipId}/insurances/{insuranceId}/editInsurance")
    public String editInsuranceForm(@PathVariable("ipId") long insuredPersonId,
                                    @PathVariable long insuranceId,
                                    @RequestParam("access") byte access,
                                    Model model) {

        InsuranceType[] insuranceTypes = InsuranceType.values();
        model.addAttribute("insuranceTypes", insuranceTypes);
        InsuranceDto insuranceDto = insuranceService.getInsuranceById(insuranceId);

        model.addAttribute("activePage", "insurances");
        model.addAttribute("insuranceId", insuranceId);
        model.addAttribute("ipId", insuredPersonId);
        model.addAttribute("insuranceForm", insuranceDto);
        model.addAttribute("access", access);
        return "/editInsurance";
    }

    @PostMapping("/insuredPersons/{ipId}/insurances/{insuranceId}/edit")
    public String editInsurance(@PathVariable("ipId") long insuredPersonId,
                                @PathVariable long insuranceId,
                                @RequestParam("access") byte access,
                                @Valid @ModelAttribute("insuranceForm") InsuranceDto insuranceForm,
                                BindingResult result,
                                Model model) {

        InsuranceType[] insuranceTypes = InsuranceType.values();
        model.addAttribute("insuranceTypes", insuranceTypes);
        model.addAttribute("activePage", "insurances");
        insuranceForm.setId(insuranceId);

        if (result.hasErrors()) {
            return "/editInsurance";
        }

        insuranceService.editInsuranceById(insuranceForm, insuredPersonId);

        return switch (access) {
            case 1 -> String.format("redirect:/insuredPersons/%d/detail/0?success", insuredPersonId);
            case 2 -> String.format("redirect:/insuredPersons/%d/insurances/%d/detail?success", insuredPersonId, insuranceId);
            default -> "redirect:/insurances/0?success";
        };
    }

    @GetMapping("/insuredPersons/{ipId}/insurances/{insuranceId}/delete")
    public String deleteInsuranceById(@PathVariable("ipId") long insuredPersonId, @PathVariable long insuranceId) {
        insuranceService.deleteInsuranceById(insuranceId);
        return String.format("redirect:/insuredPersons/%d/detail/0?success", insuredPersonId);
    }

    @GetMapping("/insuredPersons/{ipId}/insurances/{insuranceId}/detail")
    public String getInsuranceDetail(@PathVariable("ipId") long insuredPersonId,
                                     @PathVariable long insuranceId,
                                     Model model) {

        UserEntity user = new UserEntity();
        String username = SecurityUtil.getSessionUser();
        if (username != null) {
            user = userService.getByUsername(username);
        }

        InsuranceDto insuranceDto = insuranceService.getInsuranceById(insuranceId);
        InsuredPersonDto insuredPersonDto = insuredPersonService.getInsuredPersonById(insuredPersonId);

        model.addAttribute("role", user.getRoles().get(0).getName());
        model.addAttribute("activePage", "insurances");
        model.addAttribute("insurance", insuranceDto);
        model.addAttribute("insuredPerson", insuredPersonDto);

        return "/insuranceDetail";
    }
}
