package com.epam.esm.model.assembler;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.exception.BadRequestException;
import com.epam.esm.model.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class GiftCertificateModelAssembler implements RepresentationModelAssembler<GiftCertificate, EntityModel<GiftCertificate>> {
    private static final Logger logger = LoggerFactory.getLogger(GiftCertificateModelAssembler.class);

    @Override
    public EntityModel<GiftCertificate> toModel(GiftCertificate entity) {
        try {
            return EntityModel.of(entity, linkTo(methodOn(GiftCertificateController.class)
                    .findGiftCertificate(entity.getId().toString())).withSelfRel());
        } catch (NotFoundException | BadRequestException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
