package pizzaRest.repositiries;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pizzaRest.models.Person;


import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByUsername(String username);
    Optional<Person> findByEmail(String email);
}
