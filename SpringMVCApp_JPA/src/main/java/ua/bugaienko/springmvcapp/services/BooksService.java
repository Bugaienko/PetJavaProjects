package ua.bugaienko.springmvcapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.bugaienko.springmvcapp.models.Book;
import ua.bugaienko.springmvcapp.models.Person;
import ua.bugaienko.springmvcapp.repositories.BooksRepository;

import java.util.List;

/**
 * @author Sergii Bugaienko
 */

@Service
@Transactional(readOnly = true)
public class BooksService {
    private final BooksRepository booksRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> findByPerson(Person person) {
        return booksRepository.findByReader(person);
    }

    public List<Book> findAll() {
        return booksRepository.findAll();
    }

    public Book findOne(int id){
        return booksRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }



    @Transactional
    public void update(int book_id, Book dataForUpdate) {
        Book book = findOne(book_id);
        book.setTitle(dataForUpdate.getTitle());
        book.setAuthor(dataForUpdate.getAuthor());
        book.setYear(dataForUpdate.getYear());
        booksRepository.save(book);
    }

    @Transactional
    public void delete(int id){
        booksRepository.deleteById(id);
    }

    @Transactional
    public void setReader(int book_id, Person person) {
        Book book = findOne(book_id);
        book.setReader(person);
        booksRepository.save(book);
    }

    @Transactional
    public void releaseBook(int book_id) {
        Book book = findOne(book_id);
        System.out.println("release: " + book + " : " + book.getReader());
        book.setReader(null);
        System.out.println("release2: " + book + " : " + book.getReader());

        booksRepository.save(book);
    }
}
