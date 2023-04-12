package pizzaRest.util;

/**
 * @author Sergii Bugaienko
 */

public class PersonNotCreatedException extends RuntimeException {
    public PersonNotCreatedException(String msg){
        super(msg);
    }
}
