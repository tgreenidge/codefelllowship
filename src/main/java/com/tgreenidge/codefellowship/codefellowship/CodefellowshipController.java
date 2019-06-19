package com.tgreenidge.codefellowship.codefellowship;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class CodefellowshipController {

    @GetMapping("/")
    public String getHome() {
        return "home";
    }
}