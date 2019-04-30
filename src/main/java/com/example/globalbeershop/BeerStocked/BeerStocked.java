package com.example.globalbeershop.BeerStocked;

public class BeerStocked {

    private Long id;
    private String name;
    private String country;
    private String brewer;
    private String type;
    private Double abv;
    private Double price;
    private String image;
    private String description;


    public BeerStocked(Long id, String name, String country, String brewer, String type, Double abv, Double price, String image, String description) {
        super();
        this.id = id;
        this.name = name;
        this.country = country;
        this.brewer =brewer;
        this.type = type;
        this.abv = abv;
        this.price = price;
        this.image = image;
        this.description = description;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

