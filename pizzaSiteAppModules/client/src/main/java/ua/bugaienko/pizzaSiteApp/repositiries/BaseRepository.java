package ua.bugaienko.pizzaSiteApp.repositiries;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.bugaienko.pizzaSiteApp.models.Base;

import java.util.List;
import java.util.Optional;

public interface BaseRepository extends JpaRepository<Base, Integer> {
    Optional<Base> findByName(String name);
    List<Base> findAll();
}
