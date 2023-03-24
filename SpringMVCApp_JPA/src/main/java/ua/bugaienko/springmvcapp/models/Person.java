package ua.bugaienko.springmvcapp.models;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * @author Sergii Bugaienko
 */

@Entity
@Table(name = "persons")
public class Person {
    @Id
    @Column(name = "person_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int personId;
    @NotEmpty(message = "Name should not be empty")
    @Pattern(regexp = "[A-ZА-Я]\\w+ \\D+", message = "Name should begin with a capital letter")
    @Column(name = "full_name")
    private String fullName;
    @Min(value = 1900, message = "Year of birthday should be greater than 1900")
    @Column(name = "year_birthday")
    private int yearBirthday;

    @OneToMany(mappedBy = "reader")
    private List<Book> books;

    public Person(String fullName, int yearOfBirth) {
        this.fullName = fullName;
        this.yearBirthday = yearOfBirth;
    }

    public Person() {}

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getYearBirthday() {
        return yearBirthday;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        return "Person{" +
                "personId=" + personId +
                ", fullName='" + fullName + '\'' +
                ", yearBirthday=" + yearBirthday +
                '}';
    }

    public void setYearBirthday(int yearBirthday) {
        this.yearBirthday = yearBirthday;
    }
}
