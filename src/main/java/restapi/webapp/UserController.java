package restapi.webapp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
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
    /**
     *
     * @return all users that are in the DB.
     */
    @GetMapping("/users")
    public ResponseEntity<CollectionModel<EntityModel<User>>> allUsers() {
        return ResponseEntity.ok(userEntityFactory.toCollectionModel(userRepo.findAll()));
    }
    /**
     *receiver a date.
     * @return all users that have the same creation date.
     */
    @GetMapping("users/searchbydate/{date}")
    public ResponseEntity<CollectionModel<EntityModel<User>>> searchUserByDate(@PathVariable Date date) {

        return ResponseEntity.ok(userEntityFactory.toCollectionModel(userRepo.findByCreationDate(date)));

    }
    /**
     *receiver a user name
     * @return all users that their name is equals to the requested name.
     */
    @GetMapping("users/searchbyusername/{name}")
    public ResponseEntity<EntityModel<User>> searchUserByName(@PathVariable String name) {

        return ResponseEntity.ok(userEntityFactory.toModel(userRepo.findByUserName(name)));

    }
    /**
     *receiver a user id..
     * @return a users  with the same id.
     */
    @GetMapping("users/{Id}")
    public ResponseEntity<EntityModel<User>> getUser(@PathVariable Long Id) {
        return userRepo.findById(Id).map(userEntityFactory::toModel).map(ResponseEntity::ok) //
                .orElse(ResponseEntity.notFound().build());
    }
    /**
     *receiver a user email..
     * @return a all users with the same email.
     */
    @GetMapping("/users/email/{email}")
    public ResponseEntity<CollectionModel<EntityModel<User>>> getUserByEmail(@PathVariable String email) {
        List<User> user = userRepo.findByEmail(email);
        if (user.isEmpty()) {
            throw new EntityNotFoundExeption(email);
        }
        return ResponseEntity.ok(userEntityFactory.toCollectionModel(userRepo.findByEmail(email)));
    }
    /**
     *receiver a user id..
     * @return a users with the same id as DTO.
     */
    @JsonIgnore
    @GetMapping("/users/{id}/info")
    public ResponseEntity<EntityModel<UserDTO>> userInfo(@PathVariable Long id) {
        return userRepo.findById(id)
                .map(UserDTO::new) //
                .map(userDTOFactory::toModel) //
                .map(ResponseEntity::ok) //
                .orElse(ResponseEntity.notFound().build());
    }
    /**
     *
     * @return all users that are in the DB as DTO.
     */
    @GetMapping("/users/info")
    public ResponseEntity<CollectionModel<EntityModel<UserDTO>>> allUsersInfo() {
        return ResponseEntity.ok(
                userDTOFactory.toCollectionModel(
                        StreamSupport.stream(userRepo.findAll().spliterator(),
                                false)
                                .map(UserDTO::new) //
                                .collect(Collectors.toList())));
    }
    /**
     *receive name and email in the path,
     * creates a new user and returns the user as entity model and response created.
     * if get a URO exception returns bad request.
     */
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
    /**
     *receive user id  and playlist in the body,
     * search for the user by id, if exists adds the playlists to the user, and returns the user and ok response,
     * else returns bad request.
     */
    @PutMapping("/users/{id}/addplaylist")
    public ResponseEntity<EntityModel<User>> addPlaylist(@PathVariable Long id, @RequestBody Playlist playlist) {
        return userRepo.findById(id).map(userToUpdate ->
        {
            userToUpdate.getPlaylists().add(playlist);
            return userRepo.save(userToUpdate);
        }).map(userEntityFactory::toModel).map(ResponseEntity::ok).orElse(ResponseEntity.badRequest().build());
    }
    /**
     *receiver a users id.
     * and deletes the users with the specific id.
     * @return response entity no content if users is deleted, or throw entity not found exception.
     */
    @DeleteMapping("/users/delete/{id}")
    public ResponseEntity deleteUser(@PathVariable Long id) {
        userRepo.findById(id).orElseThrow(() -> new EntityNotFoundExeption(id));
        userRepo.deleteById(id);
        return ResponseEntity.noContent().build();

    }
    /**
     *receiver a users id.
     * search for the user, if exists returns all the user`s playlists, with response ok,
     * else throw entity not found exception.
     */
    @GetMapping("users/{id}/getplaylists/")
    public ResponseEntity<CollectionModel<EntityModel<Playlist>>> allUserPlaylists(@PathVariable Long id) {
        userRepo.findById(id).orElseThrow(()->new EntityNotFoundExeption(id));
       return ResponseEntity.ok(playlistsEntityFactory.toCollectionModel(playlistRepo.findPlaylistByCreator_id(id)));
    }
}