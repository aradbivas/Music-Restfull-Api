package restapi.webapp;

import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface SongRepo extends CrudRepository<Song,Long> {
    List<Song> findByTitle(String title);
    List<Song> findByScore(int score);
    List<Song> findByLength(String length);
}
