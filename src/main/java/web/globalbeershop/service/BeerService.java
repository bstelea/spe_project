package web.globalbeershop.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import web.globalbeershop.data.Beer;

import java.util.Optional;

public interface BeerService {

    Optional<Beer> findById(Long id);

    Page<Beer> findAllBeersPageable(Pageable pageable);
}
