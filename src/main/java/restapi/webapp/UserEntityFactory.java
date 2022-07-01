package restapi.webapp;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserEntityFactory implements SimpleRepresentationModelAssembler<User> {



    @Override
    public void addLinks(EntityModel<User> resource) {
        resource.add(
                linkTo(methodOn(UserController.class).getUser(resource.getContent().getId()))
                        .withSelfRel());

        resource.add(linkTo(methodOn(UserController.class).allUsers())
                .withRel("All users"));
    }


    @Override
    public void addLinks(CollectionModel<EntityModel<User>> resources) {
            resources.add(linkTo(methodOn(UserController.class).allUsers()).withSelfRel());

    }
}
