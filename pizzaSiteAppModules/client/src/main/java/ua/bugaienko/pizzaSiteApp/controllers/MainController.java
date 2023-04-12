package ua.bugaienko.pizzaSiteApp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.bugaienko.pizzaSiteApp.models.Person;
import ua.bugaienko.pizzaSiteApp.models.Pizza;
import ua.bugaienko.pizzaSiteApp.services.CafeService;
import ua.bugaienko.pizzaSiteApp.services.PizzaService;
import ua.bugaienko.pizzaSiteApp.util.UserUtil;

import java.util.List;

/**
 * @author Sergii Bugaienko
 */

@Controller
@RequestMapping("/")
public class MainController {

    private final CafeService cafeService;
    private final PizzaService pizzaService;
    private final UserUtil userUtil;


    @Autowired
    public MainController(CafeService cafeService, PizzaService pizzaService, UserUtil userUtil) {
        this.cafeService = cafeService;
        this.pizzaService = pizzaService;
        this.userUtil = userUtil;
    }

    @GetMapping("/")
    public String mainPage(Model model) {
        Person user = userUtil.getActiveUser();
        model.addAttribute("user", user);
        model.addAttribute("cafe", cafeService.findById(1));
        return "main/index";
    }

    @GetMapping("/menu")
    public String menuPage(Model model) {
        Person user = userUtil.getActiveUser();
        model.addAttribute("user", user);
        model.addAttribute("pizzas", pizzaService.findAll());
        model.addAttribute("pizzas_s", pizzaService.findByPizzaSize("small"));
        model.addAttribute("pizzas_M", pizzaService.findByPizzaSize("medium"));
        model.addAttribute("pizzas_Xl", pizzaService.findByPizzaSize("large"));
        return "main/menu";
    }

    @GetMapping("/about")
    public String aboutPage(Model model) {
        Person user = userUtil.getActiveUser();
        model.addAttribute("user", user);
        return "main/about";
    }

    @GetMapping("/contact")
    public String contactPage(Model model) {
        Person user = userUtil.getActiveUser();
        model.addAttribute("user", user);
        return "main/contact";
    }


}
