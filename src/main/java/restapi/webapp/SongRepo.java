package restapi.webapp;

import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface SongRepo extends CrudRepository<Song,Long> {
    List<Song> findByTitle(String title);
    List<Song> findByLength(String length);
    List<Song> findSongByScore(Integer score);
}
