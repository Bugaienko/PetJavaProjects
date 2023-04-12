package ua.bugaienko.pizzaSiteApp.util;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ua.bugaienko.pizzaSiteApp.models.Person;
import ua.bugaienko.pizzaSiteApp.security.PersonDetails;

/**
 * @author Sergii Bugaienko
 */

@Component
public class UserUtil {

    public Person getActiveUser(){
        Person user = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
            user = personDetails.getPerson();
//            System.out.println(user.getUsername());
        }
        return user;
    }
}
