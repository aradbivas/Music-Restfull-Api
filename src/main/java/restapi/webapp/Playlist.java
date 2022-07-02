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
public class Playlist
{

        @Id @GeneratedValue Long id;
        private String name;
        @ManyToMany  private List<Song> songList = new ArrayList<>();
        private Date creationDate;

        @JsonIgnore @OneToOne private User creator;

        public Playlist(String name, Song song, User user) {
                this.name = name;
                songList.add(song);
                this.creator = user;
                this.creationDate = new Date();
        }
        public Playlist(String name, List<Song> song, User user) {
                this.name = name;
                this.songList = song;
                this.creator = user;
                this.creationDate = new Date();
        }
}
