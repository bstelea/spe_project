package web.globalbeershop.data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UserDTO {
    @NotNull
    @NotEmpty
    private String forename;

    @NotNull
    @NotEmpty
    private String surname;

    @NotNull
    @NotEmpty
    private String password;

    @NotNull
    @NotEmpty
    private String matchingPassword;

    @Email
    @NotNull
    @NotEmpty
    private String email;

    public UserDTO(@NotNull @NotEmpty String forename, @NotNull @NotEmpty String surname, @NotNull @NotEmpty String password, String matchingPassword, @Email @NotNull @NotEmpty String email) {
        this.forename = forename;
        this.surname = surname;
        this.password = password;
        this.matchingPassword = matchingPassword;
        this.email = email;
    }

    public UserDTO() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}