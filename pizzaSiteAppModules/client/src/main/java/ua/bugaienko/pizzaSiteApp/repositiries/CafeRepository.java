package ua.bugaienko.pizzaSiteApp.repositiries;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ua.bugaienko.pizzaSiteApp.models.Cafe;

@Service
public interface CafeRepository extends JpaRepository<Cafe, Integer> {

}
