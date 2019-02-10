package web.globalbeershop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.globalbeershop.data.Beer;

import java.util.Optional;

public interface BeerRepository extends JpaRepository<Beer, Long>{
    Optional<Beer> findById(Long id);
}
