package com.example.globalbeershop.BeerStocked;

public class BeerStocked {

    private Long id;
    private String name;
    private String country;
    private String brewer;
    private String type;
    private Double abv;

    public BeerStocked() {
        super();
    }

    //takes id
    public BeerStocked(Long id, String name, String country, String brewer, String type, Double abv) {
        super();
        this.id = id;
        this.name = name;
        this.country = country;
        this.brewer =brewer;
        this.type = type;
        this.abv = abv;
    }

    //doesnt take id
    public BeerStocked(String name, String country, String brewer, String type, Double abv) {
        super();
        this.name = name;
        this.country = country;
        this.brewer =brewer;
        this.type = type;
        this.abv = abv;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getBrewer() {
        return brewer;
    }

    public void setBrewer(String brewer) {
        this.brewer = brewer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getAbv() {
        return abv;
    }

    public void setAbv(Double abv) {
        this.abv = abv;
    }

    @Override
    public String toString() {
        return String.format("BeerStocked [id=%d, name=%s, country=%s, brewer=%s, type=%s, abv=%f]",
                id, name, country, brewer, type, abv);
    }

}

