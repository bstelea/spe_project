package web.globalbeershop.data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "order_table")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column(name = "name")
    @NotEmpty(message = "*Please provide your name")
    private String name;

    @Column(name = "last_name")
    @NotEmpty(message = "*Please provide your last name")
    private String lastName;

    @Column(name = "email", nullable = false)
    @Email(message = "*Please provide a valid email")
    @NotEmpty(message = "*Please provide an email")
    private String email;

    @Column(name = "address")
    @NotEmpty(message = "*Please enter your address")
    private String address;

    @Column(name = "city")
    @NotEmpty(message = "*Please enter your city")
    private String city;



    @Column(name = "county")
    @NotEmpty(message = "*Please enter your county")
    private String county;

    @Column(name = "zone")
    @NotEmpty(message = "*Please enter your postcode")
    private String zone;

    @Column(name = "payment_ref")
    private String paymentRef;

    public Order(){

    }
    public Order(@NotEmpty(message = "*Please provide your name") String name, @NotEmpty(message = "*Please provide your last name") String lastName, @Email(message = "*Please provide a valid email") @NotEmpty(message = "*Please provide an email") String email, @NotEmpty(message = "*Please enter your address") String address, @NotEmpty(message = "*Please enter your city") String city, @NotEmpty(message = "*Please enter your county") String county, @NotEmpty(message = "*Please enter your postcode") String zone, String paymentRef, User user, List<OrderItem> items) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.city = city;
        this.county = county;
        this.zone = zone;
        this.paymentRef = paymentRef;
        this.user = user;
        this.items = items;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> items = new ArrayList<>();

    public String getPaymentRef() {
        return paymentRef;
    }

    public void setPaymentRef(String paymentRef) {
        this.paymentRef = paymentRef;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }
}
