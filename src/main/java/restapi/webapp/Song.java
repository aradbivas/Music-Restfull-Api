package restapi.webapp;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

//import static sun.rmi.registry.RegistryImpl.getID;

@Entity
@Table(name = "songs")
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Song {
    @Id @GeneratedValue Long songID;
    String title;
    String length;
    int score;

    @Override
    public String toString(){
        return "GitHub Information {id =" + getSongID();
    }
}
