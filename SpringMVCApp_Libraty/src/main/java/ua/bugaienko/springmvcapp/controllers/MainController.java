package ua.bugaienko.springmvcapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Sergii Bugaienko
 */

@Controller
@RequestMapping()
public class MainController {

    @GetMapping()
    public String index(){
        return "main/index";
    }
}
