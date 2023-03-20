package ua.bugaienko.springmvcapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.bugaienko.springmvcapp.dao.BookDao;
import ua.bugaienko.springmvcapp.dao.LibraryDAO;
import ua.bugaienko.springmvcapp.models.Book;
import ua.bugaienko.springmvcapp.models.Person;

import javax.validation.Valid;

/**
 * @author Sergii Bugaienko
 */

@Controller
@RequestMapping("/book")
public class BookController {
    private final BookDao bookDao;
    private final LibraryDAO libraryDAO;

    @Autowired
    public BookController(BookDao bookDao, LibraryDAO libraryDAO) {
        this.bookDao = bookDao;
        this.libraryDAO = libraryDAO;
    }

    @GetMapping()
    public String books(Model model) {
        model.addAttribute("books", bookDao.getAllBook());
        return "book/index";
    }

    @GetMapping("/{id}")
    public String showBook(@PathVariable("id") int bookId, Model model, @ModelAttribute("person")Person person) {
        model.addAttribute("book", bookDao.getById(bookId));
        model.addAttribute("persons", libraryDAO.getAll());
        model.addAttribute("book_person", bookDao.getIdBookLessen(bookId));
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
        bookDao.save(book);
        return "redirect:/book";
    }

    @GetMapping("/{id}/edit")
    public String editBook(@PathVariable("id") int bookId, Model model){
        model.addAttribute("book", bookDao.getById(bookId));
        return "book/edit";
    }

    @PatchMapping("/{id}")
    public String updateBook(@ModelAttribute("book")@Valid Book book,
                             BindingResult bindingResult,
                             @PathVariable("id") int bookId) {
        if (bindingResult.hasErrors()) {
            return "book/edit";
        }
        bookDao.updateBook(bookId, book);
        return "redirect:/book";
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") int bookId){
        bookDao.deleteBook(bookId);
        return "redirect:/book";
    }

    @GetMapping("/{id}/release")
    public String releaseBook(@PathVariable("id") int bookId) {
        bookDao.releaseBook(bookId);
        return "redirect:/book";
    }

    @GetMapping("/{id}/give")
    public String giveBook(@ModelAttribute("person") Person person,
                           @PathVariable("id") int bookId){
        bookDao.giveBookTo(bookId, person);
        return "redirect:/book";
    }



}
