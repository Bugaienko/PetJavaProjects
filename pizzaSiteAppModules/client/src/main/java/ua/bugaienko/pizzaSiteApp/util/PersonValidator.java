package ua.bugaienko.pizzaSiteApp.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ua.bugaienko.pizzaSiteApp.models.Person;
import ua.bugaienko.pizzaSiteApp.services.PersonService;

import java.util.Optional;

/**
 * @author Sergii Bugaienko
 */

@Component
public class PersonValidator implements Validator {

    private final PersonService personService;

    @Autowired
    public PersonValidator(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person targetPerson = (Person) target;

        Optional<Person> resultByName = personService.findUserByUsername(targetPerson.getUsername());
        Optional<Person> resultByEmail = personService.findUserByEmail(targetPerson.getEmail());

        if (resultByName.isPresent()){
            errors.rejectValue("username", "", "This username is already in use. Choose another");
        }

        if (resultByEmail.isPresent()){
            errors.rejectValue("email", "", "This Email is already in use. Choose another");
        }

    }

    public void validate(Object target, String rePassword, Errors errors) {
        Person targetPerson = (Person) target;
        if (!targetPerson.getPassword().equals(rePassword)){
            errors.rejectValue("password", "", "Passwords don't match");
        }
    }
}
