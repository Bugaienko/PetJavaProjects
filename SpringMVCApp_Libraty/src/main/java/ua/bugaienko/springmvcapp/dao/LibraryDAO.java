package ua.bugaienko.springmvcapp.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ua.bugaienko.springmvcapp.models.Person;

import java.util.List;

/**
 * @author Sergii Bugaienko
 */

@Component
public class LibraryDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LibraryDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> getAll(){
        List<Person> persons = jdbcTemplate.query("SELECT * FROM persons", new BeanPropertyRowMapper<>(Person.class));
        return persons;
    }

    public void save(Person person) {
        jdbcTemplate.update(
                "INSERT INTO persons(full_name, year_birthday) VALUES (?, ?)",
                person.getFullName(), person.getYearBirthday());
    }

    public Person getById(int personId) {
        return jdbcTemplate.query(
                "SELECT * FROM persons WHERE person_id=?", new Object[]{personId},
                new BeanPropertyRowMapper<>(Person.class)
        ).stream().findAny().orElse(null);
    }

    public void deletePerson(int personId) {
        jdbcTemplate.update("DELETE FROM persons WHERE person_id=?", personId);
    }


    public void updatePerson(int personId, Person person) {
        jdbcTemplate.update(
                "UPDATE persons SET full_name=?, year_birthday=? WHERE person_id=?",
                person.getFullName(), person.getYearBirthday(), personId);
    }
}
