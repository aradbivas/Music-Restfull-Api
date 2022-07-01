package restapi.webapp;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class UserController {
    UserRepo userRepo;
    UserEntityFactory userEntityFactory;
    UserDTOFactory userDTOFactory;
    PlaylistRepo playlistRepo;

    public UserController(UserRepo userRepo, UserEntityFactory userEntityFactory, UserDTOFactory userDTO, PlaylistRepo playlistRepo) {
        this.userRepo = userRepo;
        this.userEntityFactory = userEntityFactory;
        this.userDTOFactory = userDTO;
        this.playlistRepo = playlistRepo;
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
    @GetMapping("/users/{email}")
    public CollectionModel<EntityModel<User>> getUserByEmail(@PathVariable String email) {
        return userEntityFactory.toCollectionModel(userRepo.findByEmail(email));

    }
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
    public User addUser(@PathVariable String name, @PathVariable String email)
    {
        User newUser = new User(name,email);
        return userRepo.save(newUser);
    }
    @PutMapping("/users/{id}/addplaylist/{playlistID}")
    public Optional<User> addPlaylist(@PathVariable Long id, @RequestBody Playlist playlist)
    {
        return userRepo.findById(id).map(userToUpdate ->
        { userToUpdate.getPlaylists().add(playlist);
            return userRepo.save(userToUpdate);
        });

    }
    @DeleteMapping("/users/delete/{id}")
    void deleteUser(@PathVariable Long id)
    {
        userRepo.deleteById(id);
    }
}
