package restapi.webapp;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Value;

@Value
@JsonPropertyOrder({"userName", "Email"})
public class UserDTO {
    @JsonIgnore
    private final User User;
    public String getUserName() {
        return this.User.getUserName();
    }
    public String getEmail() {
        return this.User.getEmail();
    }


}
