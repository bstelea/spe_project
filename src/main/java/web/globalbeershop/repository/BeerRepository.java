package web.globalbeershop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.globalbeershop.data.Beer;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface BeerRepository extends JpaRepository<Beer, Long>,
        QuerydslPredicateExecutor<Beer> {

    Optional<Beer> findById(Long id);

    @Query("SELECT DISTINCT b.country FROM Beer b WHERE b.stock > 0")
    List<String> findAllDistinctCountry();

    @Query("SELECT DISTINCT b.brewer FROM Beer b WHERE b.stock > 0")
    List<String> findAllDistinctBrewer();

    @Query("SELECT DISTINCT b.abv FROM Beer b WHERE b.stock > 0")
    List<String> findAllDistinctAbv();

    @Query("SELECT DISTINCT b.type FROM Beer b WHERE b.stock > 0")
    List<String> findAllDistinctType();
}
