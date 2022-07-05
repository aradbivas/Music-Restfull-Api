package restapi.webapp;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Song {
    private @Id @GeneratedValue Long songId;
    String title;
    String length;
    int score;
    public Song(String title, String length, int score) {
        this.title = title;
        this.length = length;
        this.score = score;
    }

    @Override
    public String toString(){
        return "GitHub Information {id =" + getSongId();
    }
}
