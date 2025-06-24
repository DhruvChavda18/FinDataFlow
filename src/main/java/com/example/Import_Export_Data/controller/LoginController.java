package com.example.Import_Export_Data.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLoginPage(HttpSession session) {
        // If already logged in, redirect to index
        if (session.getAttribute("loggedIn") != null) {
            return "redirect:/";
        }
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username, @RequestParam String password, HttpSession session, RedirectAttributes redirectAttributes) {
        if ("user".equals(username) && "user".equals(password)) {
            session.setAttribute("loggedIn", true);
            return "redirect:/";
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid username or password!");
            return "redirect:/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
} 