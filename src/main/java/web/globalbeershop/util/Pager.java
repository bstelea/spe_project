package web.globalbeershop.util;

import org.springframework.data.domain.Page;
import web.globalbeershop.data.Beer;

public class Pager {

    private final Page<Beer> beers;

    public Pager(Page<Beer> beers) {
        this.beers = beers;
    }

    public int getPageIndex() { return beers.getNumber() + 1; }

    public int getPageSize() { return beers.getSize(); }

    public boolean hasNext() { return beers.hasNext(); }

    public boolean hasPrevious() { return beers.hasPrevious(); }

    public int getTotalPages() { return beers.getTotalPages(); }

    public long getTotalElements() { return beers.getTotalElements(); }

    public boolean indexOutOfBounds() {
        return this.getPageIndex() < 0 || this.getPageIndex() > this.getTotalElements();
    }
}
