package com.epam.esm.model.hateoas.assembler;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.controller.TagController;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.exception.NotFoundException;
import com.epam.esm.model.hateoas.model.GiftCertificateModel;
import com.epam.esm.model.hateoas.model.TagModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TagModelAssembler implements RepresentationModelAssembler<Tag, TagModel> {
    private static final Logger logger = LoggerFactory.getLogger(TagModelAssembler.class);

    @Override
    public TagModel toModel(Tag entity) {
        TagModel tagModel = new TagModel();
        try {
            tagModel.add(linkTo(methodOn(TagController.class)
                    .findTag(entity.getId())).withSelfRel());
        } catch (NotFoundException e) {
            logger.error(e.getMessage());
        }
        tagModel.setId(entity.getId());
        tagModel.setName(entity.getName());
        tagModel.setGiftCertificates(toGiftCertificateModel(entity.getGiftCertificates()));
        return tagModel;
    }

    private Set<GiftCertificateModel> toGiftCertificateModel(Set<GiftCertificate> giftCertificates) {
        if (giftCertificates.isEmpty())
            return Collections.emptySet();
        return giftCertificates.stream()
                .map(giftCertificate -> {
                    GiftCertificateModel giftCertificateModel = new GiftCertificateModel();
                    giftCertificateModel.setId(giftCertificate.getId());
                    giftCertificateModel.setName(giftCertificate.getName());
                    giftCertificateModel.setDescription(giftCertificate.getDescription());
                    giftCertificateModel.setPrice(giftCertificate.getPrice());
                    giftCertificateModel.setDuration(giftCertificate.getDuration());
                    giftCertificateModel.setCreateDate(giftCertificate.getCreateDate());
                    giftCertificateModel.setLastUpdateDate(giftCertificate.getLastUpdateDate());
                    try {
                        giftCertificateModel.add(linkTo(methodOn(GiftCertificateController.class)
                                .findGiftCertificate(giftCertificate.getId()))
                                .withSelfRel());
                    } catch (NotFoundException e) {
                        logger.error(e.getMessage());
                        throw new RuntimeException(e);
                    }
                    return giftCertificateModel;
                })
                .collect(Collectors.toSet());
    }
}
