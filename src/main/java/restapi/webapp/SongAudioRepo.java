package restapi.webapp;

import org.springframework.data.repository.CrudRepository;

public interface SongAudioRepo extends CrudRepository<Song,Long> {
}
