package com.myapp.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Hidden
public class HomeController {

    @RequestMapping("/")
    public String home() {
        return "redirect:swagger-ui.html";
    }

}
