package ua.bugaienko.springmvcapp.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ua.bugaienko.springmvcapp.dao.LibraryDAO;
import ua.bugaienko.springmvcapp.models.Person;

/**
 * @author Sergii Bugaienko
 */

@Component
public class PersonLibValidator implements Validator {

    private final LibraryDAO libraryDAO;

    @Autowired
    public PersonLibValidator(LibraryDAO libraryDAO) {
        this.libraryDAO = libraryDAO;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

    }
}
