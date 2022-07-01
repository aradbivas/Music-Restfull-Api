package restapi.webapp;

import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

@RestController
public class SongsController {

    SongRepo songRepo;
    SongsEntityFactory songsEntityFactory;
    SongService songService;

    public SongsController(SongRepo songRepo, SongsEntityFactory songsEntityFactory,  SongService songService
    ) {
        this.songRepo = songRepo;
        this.songsEntityFactory = songsEntityFactory;
        this.songService = songService;
    }

    @GetMapping("songs/getsongs")
    public ResponseEntity<CollectionModel<EntityModel<Song>>> allsongs() {
        return ResponseEntity.ok(songsEntityFactory.toCollectionModel(songRepo.findAll()));

    }
    @GetMapping("songs/{id}")
    public ResponseEntity<EntityModel<Song>> getSong(@PathVariable Long Id) {
        return songRepo.findById(Id).map(songsEntityFactory::toModel).map(ResponseEntity::ok) //
                .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping("songs/addsong")
    public void addSong(@RequestBody Song song)
    {
        songRepo.save(song);
    }

    @DeleteMapping("song/delete/{id}")
    void deleteSong(@PathVariable Long id)
    {

        songRepo.deleteById(id);
    }
    @PutMapping("/song/changesong/{id}")
    public Optional<Song> changeSong(@RequestBody Song song, @PathVariable Long id)
    {
        return songRepo.findById(id).map(songToUpdate ->
        {   songToUpdate.score = song.score;
            songToUpdate.title = song.title;
            songToUpdate.length = song.length;
            return songRepo.save(songToUpdate);
        });

    }

    @GetMapping("song/{songName}/{artistName}")
    public ResponseEntity<EntityModel<Song>> getSong(@PathVariable String songName, @PathVariable String artistName) throws ExecutionException, InterruptedException {

        CompletableFuture<SongTrackResponse> song = songService.userDetails(songName,artistName);
        CompletableFuture.completedFuture(song).join();
        Song song1 = song.get().recordings.get(0);
        songRepo.save(song1);
        return songRepo.findById(song1.getSongID()).map(songsEntityFactory::toModel).map(ResponseEntity::ok) //
                .orElse(ResponseEntity.notFound().build());
    }
}
