package restapi.webapp;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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
    @GetMapping("songs/getsong/{id}")
    public ResponseEntity<EntityModel<Song>> getSong(@PathVariable Long id) {

        return songRepo.findById(id).map(songsEntityFactory::toModel).map(ResponseEntity::ok) //
                .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping("songs/addsong")
    public ResponseEntity<EntityModel<Song>> addSong(@RequestBody Song song)
    {

            Song song1 = new Song(song.getTitle(),song.getLength(),song.getScore());
            songRepo.save(song1);
            return songRepo.findById(song1.getSongId()).map(songsEntityFactory::toModel).map(ResponseEntity::ok).orElse(ResponseEntity.badRequest().build());

    }

    @DeleteMapping("songs/delete/{id}")
    public ResponseEntity deleteSong(@PathVariable Long id)
    {
        songRepo.findById(id).orElseThrow(()->new UserNotFoundExeption(id));
        songRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/song/changesong/{id}")
    public ResponseEntity<EntityModel<Song>> changeSong(@RequestBody Song song, @PathVariable Long id)
    {
        return songRepo.findById(id).map(songToUpdate ->
        {   songToUpdate.score = song.score;
            songToUpdate.title = song.title;
            songToUpdate.length = song.length;
            return songRepo.save(songToUpdate);
        }).map(songsEntityFactory::toModel).map(ResponseEntity::ok).orElse(ResponseEntity.badRequest().build());

    }

    @GetMapping("song/findsong/{songName}/{artistName}")
    public ResponseEntity<EntityModel<Song>> getSong(@PathVariable String songName, @PathVariable String artistName) throws ExecutionException, InterruptedException {

        CompletableFuture<SongTrackResponse> song = songService.userDetails(songName,artistName);
        CompletableFuture.completedFuture(song).join();
        Song song1 = song.get().recordings.get(0);
        songRepo.save(song1);
        return songRepo.findById(song1.getSongId()).map(songsEntityFactory::toModel).map(ResponseEntity::ok) //
                .orElse(ResponseEntity.notFound().build());
    }
}
