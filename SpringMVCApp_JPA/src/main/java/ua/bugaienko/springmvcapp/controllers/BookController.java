package ua.bugaienko.springmvcapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.bugaienko.springmvcapp.models.Book;
import ua.bugaienko.springmvcapp.models.Person;
import ua.bugaienko.springmvcapp.services.BooksService;
import ua.bugaienko.springmvcapp.services.PersonService;

import javax.validation.Valid;

/**
 * @author Sergii Bugaienko
 */

@Controller
@RequestMapping("/book")
public class BookController {
    private final BooksService booksService;
    private final PersonService personService;

    @Autowired
    public BookController(BooksService booksService, PersonService personService) {
        this.booksService = booksService;
        this.personService = personService;
    }

    @GetMapping()
    public String books(Model model) {
        model.addAttribute("books", booksService.findAll());
        return "book/index";
    }

    @GetMapping("/{id}")
    public String showBook(@PathVariable("id") int bookId, Model model, @ModelAttribute("person")Person person) {
        Book book = booksService.findOne(bookId);
        model.addAttribute("book", book);
        model.addAttribute("persons", personService.findAll());
        model.addAttribute("book_person", book.getReader());
        return "book/show_book";
    }

    @GetMapping("/add")
    public String newBookPage(@ModelAttribute("book")Book book) {
        System.out.println("ADD");
        return "book/new";
    }

    @PostMapping("/add")
    public String createBook(@ModelAttribute("book") @Valid Book book,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "book/new";
        }
        booksService.save(book);
        return "redirect:/book";
    }

    @GetMapping("/{id}/edit")
    public String editBook(@PathVariable("id") int bookId, Model model){
        model.addAttribute("book", booksService.findOne(bookId));
        return "book/edit";
    }

    @PatchMapping("/{id}")
    public String updateBook(@ModelAttribute("book")@Valid Book book,
                             BindingResult bindingResult,
                             @PathVariable("id") int bookId) {
        if (bindingResult.hasErrors()) {
            return "book/edit";
        }
        booksService.update(bookId, book);
        return "redirect:/book";
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") int bookId){
        booksService.delete(bookId);
        return "redirect:/book";
    }

    @GetMapping("/{id}/release")
    public String releaseBook(@PathVariable("id") int bookId) {
        booksService.releaseBook(bookId);
        return "redirect:/book/" + bookId;
    }

    @GetMapping("/{id}/give")
    public String giveBook(@ModelAttribute("person") Person person,
                           @PathVariable("id") int bookId){
//        bookDao.giveBookTo(bookId, person);
        booksService.setReader(bookId, person);
        return "redirect:/book/" + bookId;
    }



}
