package restapi.webapp;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class playlistController {
    playlistsEntityFactory playlistsEntityFactory;
    PlaylistRepo playlistRepo;
    UserRepo userRepo;
    UserController userController;
    SongRepo songRepo;
    playlistDTOFactory playlistDTOFactory;

    public playlistController(restapi.webapp.playlistsEntityFactory playlistsEntityFactory, PlaylistRepo playlistRepo, UserController userController, SongRepo songRepo,UserRepo userRepo,playlistDTOFactory playlistDTOFactory) {
        this.playlistsEntityFactory = playlistsEntityFactory;
        this.playlistRepo = playlistRepo;
        this.userController = userController;
        this.songRepo = songRepo;
        this.userRepo = userRepo;
        this.playlistDTOFactory = playlistDTOFactory;
    }


    @GetMapping("playlists/getAll")
    public ResponseEntity<CollectionModel<EntityModel<Playlist>>> allPlaylists() {
        return ResponseEntity.ok(playlistsEntityFactory.toCollectionModel(playlistRepo.findAll()));

    }

    @GetMapping("/playlist/{Id}")
    public ResponseEntity<EntityModel<Playlist>> getPlaylist(@PathVariable Long Id) {
        return playlistRepo.findById(Id).map(playlistsEntityFactory::toModel).map(ResponseEntity::ok) //
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("playlists/getAllPlaylists/{id}")
    public ResponseEntity<CollectionModel<EntityModel<Playlist>>> allUserPlaylists(@PathVariable Long id) {

        return ResponseEntity.ok(playlistsEntityFactory.toCollectionModel(userRepo.findById(id).get().getPlaylists()));

    }
    @GetMapping("playlist/{id}/info")
    public ResponseEntity<EntityModel<playlistDTO>> playlistInfo(@PathVariable Long id) {
        return playlistRepo.findById(id)
                .map(playlistDTO::new) //
                .map(playlistDTOFactory::toModel) //
                .map(ResponseEntity::ok) //
                .orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("playlists/delete/{Id}")
    public void deletePlaylists(@PathVariable Long Id)
    {
        playlistRepo.deleteById(Id);
    }

    @PostMapping("playlists/create/{playlistName}/{userid}")
    public void addPlaylist(@PathVariable String playlistName, @PathVariable Long userid, @RequestBody Song song)
    {
        userRepo.findById(userid).map(user -> {
            Playlist playlist = new Playlist(playlistName, song, user);
            return playlistRepo.save(playlist);
        });
    }
    @GetMapping("/playlist/info")
    public ResponseEntity<CollectionModel<EntityModel<playlistDTO>>> allPlaylistInfo() {
        return ResponseEntity.ok(
                playlistDTOFactory.toCollectionModel(
                        StreamSupport.stream(playlistRepo.findAll().spliterator(),
                                false)
                                .map(playlistDTO::new) //
                                .collect(Collectors.toList())));
    }

    @PutMapping("/playlists/addSongToPlaylist/{playlistID}")
    public Optional<Playlist> addPlaylist(@RequestBody Song song, @PathVariable Long playlistID)
    {
          return playlistRepo.findById(playlistID).map(playlist -> {
              playlist.getSongList().add(song);
            return playlistRepo.save(playlist);
        });
    }

}