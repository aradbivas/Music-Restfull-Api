package restapi.webapp;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class playlistDTOFactory implements SimpleRepresentationModelAssembler<playlistDTO> {
    @Override
    public void addLinks(EntityModel<playlistDTO> resource) {
        resource.add(
                linkTo(methodOn(playlistController.class).playlistInfo(resource.getContent().getPlaylist().getId()))
                        .withSelfRel());

        resource.add(linkTo(methodOn(playlistController.class).allPlaylists())
          .withRel("Playlists information"));
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<playlistDTO>> resources) {
        resources.add(linkTo(methodOn(UserController.class).allUsersInfo()).withSelfRel());
    }
}
