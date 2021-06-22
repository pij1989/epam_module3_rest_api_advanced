package com.epam.esm.model.assembler;

import com.epam.esm.controller.TagController;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.exception.BadRequestException;
import com.epam.esm.model.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TagModelAssembler implements RepresentationModelAssembler<Tag, EntityModel<Tag>> {
    private static final Logger logger = LoggerFactory.getLogger(TagModelAssembler.class);

    @Override
    public EntityModel<Tag> toModel(Tag entity) {
        try {
            return EntityModel.of(entity, linkTo(methodOn(TagController.class)
                    .findTag(entity.getId().toString())).withSelfRel());
        } catch (NotFoundException | BadRequestException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
