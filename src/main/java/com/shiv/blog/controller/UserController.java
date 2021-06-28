package com.shiv.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.shiv.blog.entity.User;
import com.shiv.blog.service.UserService;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new User());
        return "login_page.html";
    }

    @PostMapping("/login")
    public String checkCredentials(String Username) {
        return "redirect:/";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("user", new User());
        return "sign_up.html";
    }

    @PostMapping("/signup")
    public String createAccount(@ModelAttribute User user) {
        user.setUserRole("USER");
        userService.addUser(user);
        return "redirect:/login";
    }
}
