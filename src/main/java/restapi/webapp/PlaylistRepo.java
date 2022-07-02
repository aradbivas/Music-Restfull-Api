package restapi.webapp;

import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;


public interface PlaylistRepo extends CrudRepository<Playlist,Long> {
    List<Playlist> findByName(String name);
    List<Playlist> findByCreationDate(Date date);
    List<Playlist> findPlaylistByCreator_id(Long id);

}
