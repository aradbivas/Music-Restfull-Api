package restapi.webapp;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
class UserDTOFactory implements SimpleRepresentationModelAssembler<UserDTO> {

    @Override
    public void addLinks(EntityModel<UserDTO> resource) {
        resource.add(
                linkTo(methodOn(UserController.class).userInfo(resource.getContent().getUser().getId()))
                        .withSelfRel());

        resource.add(linkTo(methodOn(UserController.class).allUsers())
                .withRel("users information"));
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<UserDTO>> resources) {
        resources.add(linkTo(methodOn(UserController.class).allUsersInfo()).withSelfRel());
    }
}
