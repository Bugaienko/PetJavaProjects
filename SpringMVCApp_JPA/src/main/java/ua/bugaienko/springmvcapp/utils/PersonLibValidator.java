package ua.bugaienko.springmvcapp.utils;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ua.bugaienko.springmvcapp.models.Person;

/**
 * @author Sergii Bugaienko
 */

@Component
public class PersonLibValidator implements Validator {



    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

    }
}
