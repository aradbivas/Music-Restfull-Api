package restapi.webapp;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class playlistController {
    PlaylistsEntityFactory playlistsEntityFactory;
    PlaylistRepo playlistRepo;
    UserRepo userRepo;
    UserController userController;
    SongRepo songRepo;
    playlistDTOFactory playlistDTOFactory;

    public playlistController(PlaylistsEntityFactory playlistsEntityFactory, PlaylistRepo playlistRepo, UserController userController, SongRepo songRepo, UserRepo userRepo, playlistDTOFactory playlistDTOFactory) {
        this.playlistsEntityFactory = playlistsEntityFactory;
        this.playlistRepo = playlistRepo;
        this.userController = userController;
        this.songRepo = songRepo;
        this.userRepo = userRepo;
        this.playlistDTOFactory = playlistDTOFactory;
    }

    /**
     *
     * @return all playlists that are in the DB.
     */
    @GetMapping("playlists/getAll")
    public ResponseEntity<CollectionModel<EntityModel<Playlist>>> allPlaylists() {
        return ResponseEntity.ok(playlistsEntityFactory.toCollectionModel(playlistRepo.findAll()));

    }
    /**
     *receiver a playlist id.
     * @return playlist with specific id
     */
    @GetMapping("/playlist/{Id}")
    public ResponseEntity<EntityModel<Playlist>> getPlaylist(@PathVariable Long Id) {
        return playlistRepo.findById(Id).map(playlistsEntityFactory::toModel).map(ResponseEntity::ok) //
                .orElse(ResponseEntity.notFound().build());
    }
    /**
     *receiver a user id.
     * @return all playlists that are belong to specific user.
     */
    @GetMapping("playlists/getAllPlaylists/{id}")
    public ResponseEntity<CollectionModel<EntityModel<Playlist>>> allUserPlaylists(@PathVariable Long id) {

        return ResponseEntity.ok(playlistsEntityFactory.toCollectionModel(userRepo.findById(id).get().getPlaylists()));

    }
    /**
     *receiver a playlist name
     * @return all playlists that their name is equals to the requested name.
     */
    @GetMapping("playlists/searchplaylistwithname/{name}")
    public ResponseEntity<CollectionModel<EntityModel<Playlist>>> searchPlaylistByName(@PathVariable String name) {

        return ResponseEntity.ok(playlistsEntityFactory.toCollectionModel(playlistRepo.findByName(name)));

    }
    /**
     *receiver a date.
     * @return all playlists that have the same creation date.
     */
    @GetMapping("playlists/searchplaylistbydate/{date}")
    public ResponseEntity<CollectionModel<EntityModel<Playlist>>> searchPlaylistByName(@PathVariable Date date) {

        return ResponseEntity.ok(playlistsEntityFactory.toCollectionModel(playlistRepo.findByCreationDate(date)));

    }
    /**
     *receiver a playlist id..
     * @return a playlist DTO with the same id.
     */
    @GetMapping("playlist/{id}/info")
    public ResponseEntity<EntityModel<playlistDTO>> playlistInfo(@PathVariable Long id) {
        return playlistRepo.findById(id)
                .map(playlistDTO::new) //
                .map(playlistDTOFactory::toModel) //
                .map(ResponseEntity::ok) //
                .orElse(ResponseEntity.notFound().build());
    }
    /**
     *receiver a playlist id.
     * and deletes the playlist with the specific id.
     * @return response entity no concted if playlist is deleted, or throw entity not found exception.
     */
    @DeleteMapping("playlists/delete/{id}")
    public ResponseEntity deletePlaylists(@PathVariable Long id) {
        playlistRepo.findById(id).orElseThrow(() -> new EntityNotFoundExeption(id));
        playlistRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    /**
     *receiver a user id, playlist name and song in the request body.
     * search if a user with the id exists, if not return bad request.
     * @return
     * * if user is exists, create a new playlist from all the detailes and returns ok with the new playlist created.
     */
    @PostMapping("playlists/create/{playlistName}/{userid}")
    public ResponseEntity<EntityModel<Playlist>> addPlaylist(@PathVariable String playlistName, @PathVariable Long userid, @RequestBody Song song)
    {
        return userRepo.findById(userid).map(user -> {
            Playlist playlist = new Playlist(playlistName, song, user);
            return playlistRepo.save(playlist);
        }).map(playlistsEntityFactory::toModel).map(ResponseEntity::ok).orElse(ResponseEntity.badRequest().build());
    }
    /**
     *
     * @return all playlists as DTO
     */
    @GetMapping("/playlist/info")
    public ResponseEntity<CollectionModel<EntityModel<playlistDTO>>> allPlaylistInfo() {
        return ResponseEntity.ok(
                playlistDTOFactory.toCollectionModel(
                        StreamSupport.stream(playlistRepo.findAll().spliterator(),
                                false)
                                .map(playlistDTO::new) //
                                .collect(Collectors.toList())));
    }
    /**
     *receiver a playlist id, and song in the body.
     * check if the song exists in the DB, if not throws entity not found execption.
     * if exists add the song to the playlists and returns the plasylist and ok.
     * if playlists doesn't exists returns bad request.
     *
     */
    @PutMapping("/playlists/addSongToPlaylist/{playlistID}")
    public ResponseEntity<EntityModel<Playlist>> addPlaylist(@RequestBody Song song, @PathVariable Long playlistID)
    {
        songRepo.findById(song.getSongId()).orElseThrow(()->new EntityNotFoundExeption(song.getSongId()));

        return playlistRepo.findById(playlistID).map(playlist -> {
              playlist.getSongList().add(song);
            return playlistRepo.save(playlist);
        }).map(playlistsEntityFactory::toModel).map(ResponseEntity::ok).orElse(ResponseEntity.badRequest().build());
    }

}