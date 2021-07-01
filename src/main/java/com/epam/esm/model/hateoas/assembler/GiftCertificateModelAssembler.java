package com.epam.esm.model.hateoas.assembler;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.exception.NotFoundException;
import com.epam.esm.model.hateoas.model.GiftCertificateModel;
import com.epam.esm.model.hateoas.model.TagModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GiftCertificateModelAssembler implements RepresentationModelAssembler<GiftCertificate, GiftCertificateModel> {
    private static final Logger logger = LoggerFactory.getLogger(GiftCertificateModelAssembler.class);

    private final TagModelAssembler tagModelAssembler;

    @Autowired
    public GiftCertificateModelAssembler(TagModelAssembler tagModelAssembler) {
        this.tagModelAssembler = tagModelAssembler;
    }

    @Override
    public GiftCertificateModel toModel(GiftCertificate entity) {
        GiftCertificateModel giftCertificateModel = new GiftCertificateModel();
        try {
            giftCertificateModel.add(linkTo(methodOn(GiftCertificateController.class)
                    .findGiftCertificate(entity.getId())).withSelfRel());
        } catch (NotFoundException e) {
            logger.error(e.getMessage());
        }
        giftCertificateModel.setId(entity.getId());
        giftCertificateModel.setName(entity.getName());
        giftCertificateModel.setDescription(entity.getDescription());
        giftCertificateModel.setPrice(entity.getPrice());
        giftCertificateModel.setDuration(entity.getDuration());
        giftCertificateModel.setCreateDate(entity.getCreateDate());
        giftCertificateModel.setLastUpdateDate(entity.getLastUpdateDate());
        giftCertificateModel.setTags(toTagModel(entity.getTags()));
        return giftCertificateModel;
    }

    private Set<TagModel> toTagModel(Set<Tag> tags) {
        if (tags.isEmpty()) {
            return Collections.emptySet();
        }
        return tags.stream()
                .map(tagModelAssembler::toModel)
                .collect(Collectors.toSet());
    }
}
