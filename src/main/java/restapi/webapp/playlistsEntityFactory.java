package restapi.webapp;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class playlistsEntityFactory implements SimpleRepresentationModelAssembler<Playlist> {

    @Override
    public void addLinks(EntityModel<Playlist> resource) {
        resource.add(
                linkTo(methodOn(playlistController.class).getPlaylist(resource.getContent().getId()))
                        .withSelfRel());

        resource.add(linkTo(methodOn(playlistController.class).allPlaylists())
                .withRel("All playlists"));
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<Playlist>> resources) {
        resources.add(linkTo(methodOn(playlistController.class).allPlaylists()).withSelfRel());

    }
}
