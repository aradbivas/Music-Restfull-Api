package restapi.webapp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class UserController {
    UserRepo userRepo;
    UserEntityFactory userEntityFactory;
    UserDTOFactory userDTOFactory;
    PlaylistRepo playlistRepo;
    PlaylistsEntityFactory playlistsEntityFactory;

    public UserController(UserRepo userRepo, UserEntityFactory userEntityFactory, UserDTOFactory userDTO, PlaylistRepo playlistRepo, PlaylistsEntityFactory playlistsEntityFactory) {
        this.userRepo = userRepo;
        this.userEntityFactory = userEntityFactory;
        this.userDTOFactory = userDTO;
        this.playlistRepo = playlistRepo;
        this.playlistsEntityFactory = playlistsEntityFactory;
    }

    @GetMapping("/users")
    public ResponseEntity<CollectionModel<EntityModel<User>>> allUsers() {
        return ResponseEntity.ok(userEntityFactory.toCollectionModel(userRepo.findAll()));
    }

    @GetMapping("users/{Id}")
    public ResponseEntity<EntityModel<User>> getUser(@PathVariable Long Id) {
        return userRepo.findById(Id).map(userEntityFactory::toModel).map(ResponseEntity::ok) //
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/users/email/{email}")
    public ResponseEntity<CollectionModel<EntityModel<User>>> getUserByEmail(@PathVariable String email) {
        List<User> user = userRepo.findByEmail(email);
        if (user.isEmpty()) {
            throw new UserNotFoundExeption(email);
        }
        return ResponseEntity.ok(userEntityFactory.toCollectionModel(userRepo.findByEmail(email)));
    }

    @JsonIgnore
    @GetMapping("/users/{id}/info")
    public ResponseEntity<EntityModel<UserDTO>> userInfo(@PathVariable Long id) {
        return userRepo.findById(id)
                .map(UserDTO::new) //
                .map(userDTOFactory::toModel) //
                .map(ResponseEntity::ok) //
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/users/info")
    public ResponseEntity<CollectionModel<EntityModel<UserDTO>>> allUsersInfo() {
        return ResponseEntity.ok(
                userDTOFactory.toCollectionModel(
                        StreamSupport.stream(userRepo.findAll().spliterator(),
                                false)
                                .map(UserDTO::new) //
                                .collect(Collectors.toList())));
    }

    @PostMapping("/users/adduser/{name}/{email}")
    public ResponseEntity<?> addUser(@PathVariable String name, @PathVariable String email) {
        try {
            User newUser = new User(name, email);
            userRepo.save(newUser);
            EntityModel<User> userEntityModel = userEntityFactory.toModel(newUser);
            return ResponseEntity.created(new URI(userEntityModel.getRequiredLink(IanaLinkRelations.SELF).getHref())).body(userEntityModel);
        } catch (URISyntaxException e) {
            return ResponseEntity.badRequest().body("Cannot create new user" + name + email);
        }

    }

    @PutMapping("/users/{id}/addplaylist/{playlistID}")
    public ResponseEntity<EntityModel<User>> addPlaylist(@PathVariable Long id, @RequestBody Playlist playlist) {
        return userRepo.findById(id).map(userToUpdate ->
        {
            userToUpdate.getPlaylists().add(playlist);
            return userRepo.save(userToUpdate);
        }).map(userEntityFactory::toModel).map(ResponseEntity::ok).orElse(ResponseEntity.badRequest().build());


    }

    @DeleteMapping("/users/delete/{id}")
    public ResponseEntity deleteUser(@PathVariable Long id) {
        userRepo.findById(id).orElseThrow(() -> new UserNotFoundExeption(id));
        userRepo.deleteById(id);
        return ResponseEntity.noContent().build();

    }

    @GetMapping("users/{id}/getplaylists/")
    public ResponseEntity<CollectionModel<EntityModel<Playlist>>> allUserPlaylists(@PathVariable Long id) {
        userRepo.findById(id).orElseThrow(()->new UserNotFoundExeption(id));
       return ResponseEntity.ok(playlistsEntityFactory.toCollectionModel(playlistRepo.findPlaylistByCreator_id(id)));
    }
}