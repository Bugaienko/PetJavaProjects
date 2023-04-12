package ua.bugaienko.pizzaSiteApp.repositiries;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.bugaienko.pizzaSiteApp.models.TypeIngredient;

import java.util.Optional;

@Repository
public interface TypesRepository extends JpaRepository<TypeIngredient, Integer> {

    Optional<TypeIngredient> findByName(String name);
}
