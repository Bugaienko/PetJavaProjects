package pizzaRest.repositiries;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pizzaRest.models.Person;
import pizzaRest.models.Pizza;


import java.util.List;
import java.util.Optional;

@Repository
public interface PizzaRepository extends JpaRepository<Pizza, Integer> {

    List<Pizza> findDistinctPizzaByBase_SizeLikeIgnoreCase(String size, Sort name);
    List<Pizza> findByPersons(Person person);

    Optional<Pizza> findByName(String name);
}
