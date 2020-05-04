package com.mylibrary.rental.rest.assemblers;

import com.mylibrary.rental.dto.RentalResource;
import com.mylibrary.rental.rest.RentalController;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Allows to construct the response according to API Guidelines and HAL format:
 * https://adidas-group.gitbooks.io/api-guidelines/content/message/hal.html
 * Each resource should have an assembler responsible for the proper format.
 * Look at {@link org.springframework.hateoas.EntityLinks} and {@link org.springframework.hateoas.RelProvider}
 * to know more about adding links and embedding resources.
 *
 * @author Vadym Lotar
 * @see AbstractResourceAssembler
 * @see RentalResource
 * @see RentalController
 * @since 1.0-SNAPSHOT
 */
@Component
public class RentalResourceAssembler extends AbstractResourceAssembler<RentalResource, RentalResource> {

    /**
     * Creates a new {@link ResourceAssemblerSupport} using the given controller class and resource type.
     */
    public RentalResourceAssembler() {
        super(RentalController.class, RentalResource.class);
    }

    /**
     * Allows to append HAL metadata, like self link, links to other resources, etc.
     *
     * @param entity {@link RentalResource}
     * @return adjusted {@link RentalResource}
     */
    public RentalResource toResource(RentalResource entity) {
        // Add self link
        entity.add(linkTo(methodOn(RentalController.class).getRentalById(entity.getRentalId())).withSelfRel());

        return entity;
    }

}
