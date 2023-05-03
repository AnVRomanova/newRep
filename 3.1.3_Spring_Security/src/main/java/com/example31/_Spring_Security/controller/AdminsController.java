package com.example31._Spring_Security.controller;


import com.example31._Spring_Security.model.Role;
import com.example31._Spring_Security.model.User;
import com.example31._Spring_Security.service.RoleService;
import com.example31._Spring_Security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/")
public class AdminsController {

    private final UserService service;

    private final RoleService roleService;

    @Autowired
    public AdminsController(UserService service, RoleService roleService ) {
        this.service = service;
        this.roleService = roleService;

    }


    @GetMapping
    public String homePage() {
        return "home";
    }


    @GetMapping("/hello")
    public String helloUserPage() {
        return "hello";
    }


    @GetMapping("/read_profile")
    public String profilePage(Model model, Principal principal) {
        User user = service.findByName(principal.getName());
        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping("/only_for_admins")
    public String adminsPage(Model model) {
        List<User> usersList = service.listUsers();
        model.addAttribute("userList", usersList);
        return "adminsPage";
    }

    // Edit user
    @GetMapping("only_for_admins/{id}/edit")
    public String editPage(@PathVariable("id") long id, ModelMap model) {
        User user = service.readUser(id);
        List<Role> roles = roleService.listRoles();
        model.addAttribute("allRoles", roles);
        model.addAttribute("user", user);
        return "editPage";
    }


    @PatchMapping("only_for_admins/{id}")
    public String editUser(@ModelAttribute("user") User user) {
        service.edit(user);
        return "redirect:/only_for_admins";
    }

    //delete users
    @DeleteMapping("only_for_admins/{id}")
    public String deletePage(@PathVariable("id") long id) {
        service.delete(id);
        return "redirect:/only_for_admins";
    }

    // Add new user
    @GetMapping("only_for_admins/add")
    public String addPage(ModelMap model) {
        List<Role> roles = roleService.listRoles();
        model.addAttribute("allRoles", roles);
        model.addAttribute("user", new User());
        return "addPage";
    }

    @PostMapping("only_for_admins/add")
    public String create(@Validated(User.class) @ModelAttribute("user") User user, @RequestParam("authorities") List<String> values) {
        List<Role> roleList = service.getSetOfRoles(values);
        user.setRoles(roleList);
        service.add(user);
        return "redirect:/only_for_admins";
    }


}
