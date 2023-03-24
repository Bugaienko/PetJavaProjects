package ua.bugaienko.springmvcapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.bugaienko.springmvcapp.models.Person;
import ua.bugaienko.springmvcapp.services.BooksService;
import ua.bugaienko.springmvcapp.services.PersonService;

import javax.validation.Valid;

/**
 * @author Sergii Bugaienko
 */

@Controller
@RequestMapping("/client")
public class ClientController {

    private final PersonService personService;
    private final BooksService booksService;

    @Autowired
    public ClientController(PersonService personService, BooksService booksService) {
        this.personService = personService;
        this.booksService = booksService;
    }

    @GetMapping()
    public String clients(Model model) {
        model.addAttribute("persons", personService.findAll());
        return "client/index";
    }

    @GetMapping("/new")
    public String newClientPage(@ModelAttribute("person") Person person) {
        return "client/new";
    }

    @PostMapping("/new")
    public String createPerson(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "client/new";
        }

        personService.save(person);
        return "redirect:/client";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int personId, Model model) {
        Person person = personService.findOne(personId);
        model.addAttribute("person", person);
        model.addAttribute("books", booksService.findByPerson(person));
        return "client/show_person";
    }

    @DeleteMapping("/{id}")
    public String deletePerson(@PathVariable("id") int personId) {
        personService.delete(personId);
        return "redirect:/client";
    }

    @GetMapping("/{id}/edit")
    public String editPersonPage(@PathVariable("id") int personId, Model model) {
        model.addAttribute("person", personService.findOne(personId));
        return "client/edit";
    }

    @PatchMapping("/{id}")
    public String updatePerson(@ModelAttribute("person") @Valid Person person,
                               BindingResult bindingResult,
                               @PathVariable("id") int personId) {
        if (bindingResult.hasErrors()) {
            return "client/edit";
        }
        personService.update(personId, person);
        return "redirect:/client";
    }
}
