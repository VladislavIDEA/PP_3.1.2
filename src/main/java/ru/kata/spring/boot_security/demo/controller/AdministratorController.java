package ru.kata.spring.boot_security.demo.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping(value = "/admin")
public class AdministratorController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdministratorController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String listUsers(ModelMap modelMap) {
        modelMap.addAttribute("users", userService.findAll());
        return "user_list";
    }

    @GetMapping("/add")
    public String addUserForm(ModelMap modelMap) {
        modelMap.addAttribute("user", new User());
        modelMap.addAttribute("allRoles", roleService.findAll());
        return "user_add";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user_add";
        }
        Optional<User> userWithSameEmail = userService.findByEmail(user.getEmail());
        if (userWithSameEmail.isPresent()) {
            return "user_add";
        }
        userService.add(user);
        return "redirect:/admin";
    }
    @GetMapping("/edit")
    public String editUserForm(@RequestParam("id") Long id, ModelMap modelMap) {
        User user = userService.findById(id);
        modelMap.addAttribute("user", user);
        modelMap.addAttribute("allRoles", roleService.findAll());
        return "user_edit";
    }
    @PostMapping("/edit")
    public String editUser(@RequestParam("id") Long id,
                           @ModelAttribute ("user") @Valid User user,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user_edit";
        }
        Optional<User> userWithSameEmail = userService.findByEmail(user.getEmail());
        if (userWithSameEmail.isPresent() && !Objects.equals(userWithSameEmail.get().getId(), id)) {
            bindingResult.rejectValue("email","error.user", "Этот email уже" +
                    "используется другим пользователем");
            return "user_edit";
        }
        userService.update(id, user);
        return "redirect:/admin";
    }
    @GetMapping("/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}