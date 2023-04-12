package ua.bugaienko.pizzaSiteApp.util;

/**
 * @author Sergii Bugaienko
 */

public class PersonNotCreatedException extends RuntimeException {
    public PersonNotCreatedException(String msg){
        super(msg);
    }
}
