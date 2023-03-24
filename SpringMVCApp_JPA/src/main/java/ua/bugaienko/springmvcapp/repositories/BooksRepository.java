package ua.bugaienko.springmvcapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.bugaienko.springmvcapp.models.Book;
import ua.bugaienko.springmvcapp.models.Person;

import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {

    List<Book> findByReader(Person reader);

}
