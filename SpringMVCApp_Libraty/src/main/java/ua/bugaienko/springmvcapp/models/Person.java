package ua.bugaienko.springmvcapp.models;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * @author Sergii Bugaienko
 */

public class Person {
    private int personId;
    @NotEmpty(message = "Name should not be empty")
    @Pattern(regexp = "[A-ZА-Я]\\w+ \\D+", message = "Name should begin with a capital letter")
    private String fullName;
    @Min(value = 1900, message = "Year of birthday should be greater than 1900")
    private int yearBirthday;

    public Person(int id, String fullName, int yearOfBirth) {
        this.personId = id;
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

    public void setYearBirthday(int yearBirthday) {
        this.yearBirthday = yearBirthday;
    }
}
