package web.globalbeershop.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import web.globalbeershop.data.Beer;
import web.globalbeershop.repository.BeerRepository;
import web.globalbeershop.service.BeerService;

import java.util.Optional;

@Service
public class BeerServiceImpl implements BeerService{

    private final BeerRepository beerRepository;

    public BeerServiceImpl(BeerRepository beerRepository) {
        this.beerRepository = beerRepository;
    }


    @Override
    public Optional<Beer> findById(Long id) {
        return beerRepository.findById(id);
    }

    @Override
    public Page<Beer> findAllBeersPageable(Pageable pageable) {
        return beerRepository.findAll(pageable);
    }
}
