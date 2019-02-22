package web.globalbeershop.data;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "email")
    @Email(message = "Please provide a valid Email")
    private String email;

    @Column(unique=true)
    @NotEmpty(message = "*Please provide an username")
    String username;

    @Column(name = "password")
    @NotEmpty(message = "*Please provide an password")

    private String password;

    @Column(name = "forename")
    private String forename;

    @Column(name = "surname")
    private String surname;

    @Column(name = "enabled")
    private int enabled;

    @Column(name = "role")
    @NotEmpty(message = "*Please provide a role")
    private String role;
//
//    @OneToMany(mappedBy = "user")
//    private
//    List<Order> orders = new ArrayList<>();

    public User() {
    }

    public User(Long id, @Email(message = "Please provide a valid Email") String email, @NotEmpty(message = "*Please provide an username") String username, @Length(min = 6, message = "*Your password must have at least 6 characters") @NotEmpty(message = "*Please provide your password") String password, String forename, String surname, int enabled, @NotEmpty(message = "*Please provide a role") String role){//, List<Order> orders) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.forename = forename;
        this.surname = surname;
        this.enabled = enabled;
        this.role = role;
//        this.orders = orders;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getForename() {
        return forename;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

//    public List<Order> getOrders() {
//        return orders;
//    }

//    public void setOrders(List<Order> orders) {
//        this.orders = orders;
//    }
}
