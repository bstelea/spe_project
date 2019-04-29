package web.globalbeershop.data;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "beer")
public class Beer {

    public Beer(Long id, @Length(min = 3, message = "*Name must be at least 3 characters") String name, @DecimalMin(value = "0.0", message = "*Price has to be a non negative value") Double price, @Min(value = 0, message = "*Stock has to be a non negative value") Integer stock, String country, String brewer, String type, @DecimalMin(value = "0.00", message = "*ABV has to be a non negative value") Double abv, String image, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.country = country;
        this.brewer = brewer;
        this.type = type;
        this.abv = abv;
        this.image = image;
        this.description = description;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "beer_id")
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    @Length(min = 3, message = "*Name must be at least 3 characters")
    private String name;

    @Column(name = "price", nullable = false)
    @DecimalMin(value = "0.0", message = "*Price has to be a non negative value")
    private Double price;

    @Column(name = "stock", nullable = false)
    @Min(value = 0, message = "*Stock has to be a non negative value")
    private Integer stock;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "brewer", nullable = false)
    private String brewer;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "abv", nullable = false)
    @DecimalMin(value = "0.00", message = "*ABV has to be a non negative value")
    private Double abv;

    @Column(name = "image")
    private String image;

    @Column(name = "description")
    private String description;

    @Column(name = "continent")
    private String continent;

    @Column(name = "rating")
    private Double rating;

    @OneToMany(mappedBy = "beer")
    private List<Review> reviews = new ArrayList<>();

    public Beer(@Length(min = 3, message = "*Name must be at least 3 characters") String name, @DecimalMin(value = "0.0", message = "*Price has to be a non negative value") Double price, @Min(value = 0, message = "*Stock has to be a non negative value") Integer stock, String country, String brewer, String type, @DecimalMin(value = "0.00", message = "*ABV has to be a non negative value") Double abv, String image, String description, String continent, Double rating, List<Review> reviews) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.country = country;
        this.brewer = brewer;
        this.type = type;
        this.abv = abv;
        this.image = image;
        this.description = description;
        this.continent = continent;
        this.rating = rating;
        this.reviews = reviews;
    }

    public Beer(Long id, @Length(min = 3, message = "*Name must be at least 3 characters") String name, @DecimalMin(value = "0.0", message = "*Price has to be a non negative value") Double price, @Min(value = 0, message = "*Stock has to be a non negative value") Integer stock, String country, String brewer, String type, @DecimalMin(value = "0.00", message = "*ABV has to be a non negative value") Double abv, String image, String description, String continent, Double rating, List<Review> reviews) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.country = country;
        this.brewer = brewer;
        this.type = type;
        this.abv = abv;
        this.image = image;
        this.description = description;
        this.continent = continent;
        this.rating = rating;
        this.reviews = reviews;
    }

    public Beer() {
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Beer beer = (Beer) o;

        return id.equals(beer.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }
}
