package com.example.Import_Export_Data.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        if (session.getAttribute("loggedIn") == null) {
            return "redirect:/login";
        }
        logger.debug("Loading index page");
        return "index";
    }

    @GetMapping("/transfer-page")
    public String loadTransferDataFragment() {
        logger.debug("Loading transfer data page");
        return "transferData";
    }

    @GetMapping("/transfer")
    public String getTransferPage() {
        logger.info("Accessing transfer page");
        return "index";
    }

    @GetMapping("/settings")
    public String loadSettingsPage() {
        logger.debug("Loading settings page");
        return "settings"; // returns settings.html
    }



}
