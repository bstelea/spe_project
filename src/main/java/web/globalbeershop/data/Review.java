package web.globalbeershop.data;


import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @NotEmpty
    @ManyToOne
    @JoinColumn(name = "beer_id")
    private Beer beer;

    @Min(0)
    @Max(5)
    @Column(name = "rating")
    private String rating;

    @Size(max = 500)
    @Column(name = "message")
    private String message;

    public Review() {
    }

    public Review(User user, Beer beer, @Min(0) @Max(5) String rating, @Size(max = 500) String message) {
        this.user = user;
        this.beer = beer;
        this.rating = rating;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Beer getBeer() {
        return beer;
    }

    public void setBeer(Beer beer) {
        this.beer = beer;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
