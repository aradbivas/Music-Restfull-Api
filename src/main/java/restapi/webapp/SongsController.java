package restapi.webapp;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

    /**
     *
     * @return all songs that are in the DB.
     */
    @GetMapping("songs/getsongs")
    public ResponseEntity<CollectionModel<EntityModel<Song>>> allsongs() {
        return ResponseEntity.ok(songsEntityFactory.toCollectionModel(songRepo.findAll()));

    }
    /**
     *  recive a specific id and search for the song in the DB
     * @return Song with the same ID.
     */
    @GetMapping("songs/getsong/{id}")
    public ResponseEntity<EntityModel<Song>> getSong(@PathVariable Long id) {

        return songRepo.findById(id).map(songsEntityFactory::toModel).map(ResponseEntity::ok) //
                .orElse(ResponseEntity.notFound().build());
    }
    /**
     *  receive a song score and search for all songs in the DB with the same score.
     * @return songs with the same score
     */
    @GetMapping("songs/getsongbyscore/{score}")
    public ResponseEntity<CollectionModel<EntityModel<Song>>> getSongByScore(@PathVariable Integer score) {

        return ResponseEntity.ok(songsEntityFactory.toCollectionModel(songRepo.findSongByScore(score)));
    }
    /**
     *  receive a song length and search for all songs in the DB with the same length.
     * @return songs with the same length.
     */
    @GetMapping("songs/getsongbylength/{length}")
    public ResponseEntity<CollectionModel<EntityModel<Song>>> getSongByLength(@PathVariable String length) {

        return ResponseEntity.ok(songsEntityFactory.toCollectionModel(songRepo.findByLength(length)));
    }
    /**
     *  receive a song title and search for all songs in the DB with the same title.
     * @return songs with the same title.
     */
    @GetMapping("songs/getsongbytitle/{title}")
    public ResponseEntity<CollectionModel<EntityModel<Song>>> getSongByTitle(@PathVariable String title) {

        return ResponseEntity.ok(songsEntityFactory.toCollectionModel(songRepo.findByTitle(title)));
    }
    /**
     *  receive a song rating and search for all songs in the DB with the same rating or higher.
     * @return songs with the same rating or higher.
     */
    @GetMapping("songs/getsongwithrating/{rating}")
    public ResponseEntity<CollectionModel<EntityModel<Song>>> getSongWithRating(@PathVariable Integer rating) {
        List<Song> allSongs= (List<Song>) songRepo.findAll();
        Predicate<Song> byRating = song -> song.score >= rating;
        List<Song> result = allSongs.stream().filter(byRating)
                .collect(Collectors.toList());
        return ResponseEntity.ok(songsEntityFactory.toCollectionModel(result));
    }
    /**
     *  receive a song in the body.
     *  create a new song entity and save it to DB.
     * @return response entity with the new song added, or bad request if song couldn't be added.
     */
    @PostMapping("songs/addsong")
    public ResponseEntity<EntityModel<Song>> addSong(@RequestBody Song song)
    {

            Song song1 = new Song(song.getTitle(),song.getLength(),song.getScore());
            songRepo.save(song1);
            return songRepo.findById(song1.getSongId()).map(songsEntityFactory::toModel).map(ResponseEntity::ok).orElse(ResponseEntity.badRequest().build());

    }
    /**
     *  receive a song id in the path.
     *  search for the song, if can't find it, throws entity not found execption,
     *  else delete the song and returns non content response
     */
    @DeleteMapping("songs/delete/{id}")
    public ResponseEntity deleteSong(@PathVariable Long id)
    {
        songRepo.findById(id).orElseThrow(()->new EntityNotFoundExeption(id));
        songRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    /**
     *  receive a song in the path, and song id in the path.
     *  search for the song to update, if can't find returns bad requeset,
     *  else updates the old song with the new data and returns ok with song updated as entity.
     */
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
    /**
     *  receive song name and artist name and search for the song in the external api,
     *  async function that waits to the future from the api, if song is exists, it adds it to the DB
     *  and return the song as entity with response ok.
     *  else returns bad request.
     */
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
