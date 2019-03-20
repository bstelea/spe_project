package web.globalbeershop.data;
import lombok.Getter;
import lombok.Setter;

public class FormDTO {

    private String name;
    private String email;
    private String comments;

    public FormDTO() {
        this.name = name;
        this.email = email;
        this.comments = comments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
