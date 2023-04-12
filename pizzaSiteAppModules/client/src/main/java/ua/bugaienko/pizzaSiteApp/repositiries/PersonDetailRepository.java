package ua.bugaienko.pizzaSiteApp.repositiries;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.bugaienko.pizzaSiteApp.models.Person;

import java.util.Optional;

/**
 * @author Sergii Bugaienko
 */

@Repository
public interface PersonDetailRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByUsername(String username);
}
