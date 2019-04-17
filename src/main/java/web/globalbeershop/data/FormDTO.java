package web.globalbeershop.data;

public class FormDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String comments;

    public FormDTO() {
    }

    public FormDTO(String firstName, String lastName, String email, String comments) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.comments = comments;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
