package restapi.webapp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class User {
    private @Id @GeneratedValue Long id;
    private String userName;
    private String email;
    private Date creationDate;
    @JsonIgnore
    @OneToMany(mappedBy = "creator", orphanRemoval = true, fetch=FetchType.EAGER)
    private List<Playlist> playlists = new ArrayList<>();

    public User(String userName, String email) {
        this.userName = userName;
        this.email = email;
        creationDate = new Date();
    }
}
