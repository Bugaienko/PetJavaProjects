package ua.bugaienko.pizzaSiteApp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.bugaienko.pizzaSiteApp.models.Person;
import ua.bugaienko.pizzaSiteApp.models.Pizza;
import ua.bugaienko.pizzaSiteApp.services.PizzaService;
import ua.bugaienko.pizzaSiteApp.util.UserUtil;

import java.util.List;

/**
 * @author Sergii Bugaienko
 */

@Controller
@RequestMapping("/user") public class PersonController {

    private final UserUtil userUtil;
    private final PizzaService pizzaService;

    @Autowired
    public PersonController(UserUtil userUtil, PizzaService pizzaService) {
        this.userUtil = userUtil;
        this.pizzaService = pizzaService;
    }

    @GetMapping("/myPage")
    public String userPage(Model model){
        Person user = userUtil.getActiveUser();
        List<Pizza> pizzas = pizzaService.findByPerson(user);
        user.setFavorites(pizzas);

        model.addAttribute("user", user);
        model.addAttribute("pizzas", user.getSortedPizza());
        return "person/user";

    }

}
