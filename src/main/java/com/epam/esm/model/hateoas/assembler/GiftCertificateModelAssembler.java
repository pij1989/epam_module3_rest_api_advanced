package com.epam.esm.model.hateoas.assembler;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.exception.BadRequestException;
import com.epam.esm.model.exception.NotFoundException;
import com.epam.esm.model.hateoas.model.GiftCertificateModel;
import com.epam.esm.model.hateoas.model.TagModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
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
        try {
            GiftCertificateModel giftCertificateModel = new GiftCertificateModel();
            giftCertificateModel.add(linkTo(methodOn(GiftCertificateController.class)
                    .findGiftCertificate(entity.getId().toString())).withSelfRel());
            giftCertificateModel.setId(entity.getId());
            giftCertificateModel.setName(entity.getName());
            giftCertificateModel.setDescription(entity.getDescription());
            giftCertificateModel.setPrice(entity.getPrice());
            giftCertificateModel.setDuration(entity.getDuration());
            giftCertificateModel.setCreateDate(entity.getCreateDate());
            giftCertificateModel.setLastUpdateDate(entity.getLastUpdateDate());
            giftCertificateModel.setTags(toTagModel(entity.getTags()));
            return giftCertificateModel;
        } catch (NotFoundException | BadRequestException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private List<TagModel> toTagModel(List<Tag> tags) {
        if (tags.isEmpty()) {
            return Collections.emptyList();
        }
        return tags.stream()
                .map(tagModelAssembler::toModel)
                .collect(Collectors.toList());
    }
}