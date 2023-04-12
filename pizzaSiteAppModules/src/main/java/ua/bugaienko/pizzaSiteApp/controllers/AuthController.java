package ua.bugaienko.pizzaSiteApp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.bugaienko.pizzaSiteApp.models.Person;
import ua.bugaienko.pizzaSiteApp.services.PersonService;
import ua.bugaienko.pizzaSiteApp.util.PersonValidator;
import ua.bugaienko.pizzaSiteApp.util.UserUtil;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author Sergii Bugaienko
 */

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final PersonValidator personValidator;
    private final PersonService personService;
    private final UserUtil userUtil;
    @Value("${uploadPath}")
    private String uploadPath;

    @Autowired
    public AuthController(PersonValidator personValidator, PersonService personService, UserUtil userUtil) {
        this.personValidator = personValidator;
        this.personService = personService;
        this.userUtil = userUtil;
    }


    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("person") Person person) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String createPerson(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult,
                               @RequestParam("file") MultipartFile avatar, String rePassword) {
        if (avatar != null) {


            String uuidFile = UUID.randomUUID().toString();
//            String resultFileName = uuidFile + "-" + avatar.getOriginalFilename();
            String fileName = avatar.getOriginalFilename();

            person.setAvatar(fileName);

            personValidator.validate(person, bindingResult);
            personValidator.validate(person, rePassword, bindingResult);

            if (bindingResult.hasErrors()) {
                return "auth/registration";
            }

            Person person1 = personService.register(person);
            File uploadDir = new File(uploadPath + "/images/user/" + person1.getId());
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
//            System.out.println(uploadDir);
            try {
                avatar.transferTo(new File(uploadDir + "/" + fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            personValidator.validate(person, bindingResult);
            personValidator.validate(person, rePassword, bindingResult);

            if (bindingResult.hasErrors()) {
                return "auth/registration";
            }

            Person person1 = personService.register(person);

        }


        return "redirect:/auth/login";
    }

    @GetMapping("/exit")
    public String confirmLogout(Model model) {
        Person user = userUtil.getActiveUser();
        model.addAttribute("user", user);
        return "auth/exit";
    }

}
