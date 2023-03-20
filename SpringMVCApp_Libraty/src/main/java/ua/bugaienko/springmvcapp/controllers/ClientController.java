package ua.bugaienko.springmvcapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.bugaienko.springmvcapp.dao.BookDao;
import ua.bugaienko.springmvcapp.dao.LibraryDAO;
import ua.bugaienko.springmvcapp.models.Person;

import javax.validation.Valid;

/**
 * @author Sergii Bugaienko
 */

@Controller
@RequestMapping("/client")
public class ClientController {
    private final LibraryDAO libraryDAO;
    private final BookDao bookDao;

    @Autowired
    public ClientController(LibraryDAO libraryDAO, BookDao bookDao) {
        this.libraryDAO = libraryDAO;
        this.bookDao = bookDao;
    }

    @GetMapping()
    public String clients(Model model){
        model.addAttribute("persons", libraryDAO.getAll());
        return "client/index";
    }

    @GetMapping("/new")
    public String newClientPage(@ModelAttribute("person")Person person){
        return "client/new";
    }

    @PostMapping("/new")
    public String createPerson(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "client/new";
        }

        libraryDAO.save(person);
        return "redirect:/client";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int personId, Model model){
        model.addAttribute("person", libraryDAO.getById(personId));
        model.addAttribute("books", bookDao.getPersonsBooks(personId));
        return "client/show_person";
    }

    @DeleteMapping("/{id}")
    public String deletePerson(@PathVariable("id") int personId){
        libraryDAO.deletePerson(personId);
        return "redirect:/client";
    }
    @GetMapping("/{id}/edit")
    public String editPersonPage(@PathVariable("id") int personId, Model model) {
        model.addAttribute("person", libraryDAO.getById(personId));
        return "client/edit";
    }

    @PatchMapping("/{id}")
    public String updatePerson(@ModelAttribute("person") @Valid Person person,
                               BindingResult bindingResult,
                               @PathVariable("id") int personId) {
        if (bindingResult.hasErrors()) {
            return "client/edit";
        }
        libraryDAO.updatePerson(personId, person);
        return "redirect:/client";
    }
}
