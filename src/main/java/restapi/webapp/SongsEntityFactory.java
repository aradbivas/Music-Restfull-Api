package restapi.webapp;

import lombok.SneakyThrows;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@Component
public class SongsEntityFactory  implements SimpleRepresentationModelAssembler<Song> {
    @SneakyThrows
    @Override
    public void addLinks(EntityModel<Song> resource) {
        resource.add(
                linkTo(methodOn(SongsController.class).getSong(resource.getContent().getSongId()))
                        .withSelfRel());

        resource.add(linkTo(methodOn(SongsController.class).allsongs())
                .withRel("All songs"));
    }


    @Override
    public void addLinks(CollectionModel<EntityModel<Song>> resources) {
        resources.add(linkTo(methodOn(SongsController.class).allsongs()).withSelfRel());
    }

}
