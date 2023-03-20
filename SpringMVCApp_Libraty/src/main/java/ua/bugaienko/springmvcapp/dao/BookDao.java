package ua.bugaienko.springmvcapp.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ua.bugaienko.springmvcapp.models.Book;
import ua.bugaienko.springmvcapp.models.Person;

import java.sql.ResultSet;
import java.util.List;

/**
 * @author Sergii Bugaienko
 */

@Component
public class BookDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> getAllBook() {
        return jdbcTemplate.query("SELECT * FROM books", new BeanPropertyRowMapper<>(Book.class));
    }

    public Book getById(int bookId) {
        return jdbcTemplate.query("SELECT * FROM books WHERE book_id=?",
                new Object[]{bookId},
                new BeanPropertyRowMapper<>(Book.class)).
                stream().findAny().orElse(null);
    }

    public void save(Book book) {
        jdbcTemplate.update(
          "INSERT INTO books(title, author, year) VALUES (? , ?, ?)",
               book.getTitle(), book.getAuthor(), book.getYear()
        );
    }

    public void updateBook(int bookId, Book book) {
        jdbcTemplate.update(
                "UPDATE books SET title=?, author=?, year=? WHERE book_id=?",
                book.getTitle(), book.getAuthor(), book.getYear(), bookId
        );
    }

    public void deleteBook(int bookId) {
        jdbcTemplate.update("DELETE FROM books WHERE book_id=?", bookId);
    }

    public List<Book> getPersonsBooks(int personId) {
        return jdbcTemplate.query("SELECT * FROM books WHERE person_id=?",
                new Object[]{personId},
                new BeanPropertyRowMapper<>(Book.class));
    }

    public List<Person> getIdBookLessen(int bookId) {
        return jdbcTemplate.query("SELECT p.person_id, full_name, year_birthday FROM books b JOIN persons p on b.person_id = p.person_id WHERE book_id=?", new Object[]{bookId}, new BeanPropertyRowMapper<>(Person.class));
    }

    public void releaseBook(int bookId) {
        jdbcTemplate.update("UPDATE books SET person_id=null WHERE book_id=?", bookId);
    }

    public void giveBookTo(int bookId, Person person) {
        jdbcTemplate.update("UPDATE books SET person_id=? WHERE book_id=?", person.getPersonId(), bookId);
    }
}
