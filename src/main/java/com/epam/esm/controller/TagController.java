package com.epam.esm.controller;

import com.epam.esm.model.assembler.TagModelAssembler;
import com.epam.esm.model.creator.PageModelCreator;
import com.epam.esm.model.entity.Page;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.exception.BadRequestException;
import com.epam.esm.model.exception.NotFoundException;
import com.epam.esm.model.service.TagService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.epam.esm.model.error.MessageKeyError.*;

@RestController
@RequestMapping(value = "/tags", produces = {MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE})
public class TagController {
    private static final Logger logger = LogManager.getLogger(TagController.class);
    private static final String DEFAULT_PAGE_NUMBER = "1";
    private static final String DEFAULT_PAGE_SIZE = "5";
    private final TagService tagService;
    private final TagModelAssembler tagModelAssembler;

    @Autowired
    public TagController(TagService tagService, TagModelAssembler tagModelAssembler) {
        this.tagService = tagService;
        this.tagModelAssembler = tagModelAssembler;
    }

    @PostMapping
    public ResponseEntity<EntityModel<Tag>> createTag(@RequestBody Tag tag) {
        Optional<Tag> optionalTag = tagService.create(tag);
        if (optionalTag.isPresent()) {
            Tag createdTag = optionalTag.get();
            EntityModel<Tag> tagModel = tagModelAssembler.toModel(createdTag);
            return new ResponseEntity<>(tagModel, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Tag>> findTag(@PathVariable String id) throws NotFoundException, BadRequestException {
        long parseId;
        try {
            parseId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            logger.error("Bad request:" + e.getMessage());
            throw new BadRequestException(TAG_BAD_REQUEST, e, new Object[]{id});
        }
        Optional<Tag> optionalTag = tagService.findTag(parseId);
        if (optionalTag.isPresent()) {
            Tag tag = optionalTag.get();
            EntityModel<Tag> tagModel = tagModelAssembler.toModel(tag);
            return new ResponseEntity<>(tagModel, HttpStatus.OK);
        } else {
            throw new NotFoundException(TAG_NOT_FOUND, new Object[]{id});
        }
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Tag>>> findAllTags() {
        List<Tag> tags = tagService.findAllTag();
        CollectionModel<EntityModel<Tag>> tagCollectionModel = tagModelAssembler.toCollectionModel(tags);
        return new ResponseEntity<>(tagCollectionModel, HttpStatus.OK);
    }

    @GetMapping(params = {"page", "size"})
    public ResponseEntity<PagedModel<EntityModel<Tag>>> findTags(@RequestParam(defaultValue = DEFAULT_PAGE_NUMBER) String page,
                                                                 @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) String size) throws BadRequestException {
        int numPage;
        int numSize;
        try {
            numPage = Integer.parseInt(page);
            numSize = Integer.parseInt(size);
        } catch (NumberFormatException e) {
            logger.error("Bad request:" + e.getMessage());
            throw new BadRequestException(TAG_BAD_REQUEST_PARAMETERS, e, new Object[]{page, size});
        }
        Page<Tag> tagPage = tagService.findTags(numPage, numSize);
        PagedModel<EntityModel<Tag>> pagedModel = PageModelCreator.create(tagPage, tagModelAssembler);
        return new ResponseEntity<>(pagedModel, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTag(@PathVariable String id) throws NotFoundException, BadRequestException {
        long parseId;
        try {
            parseId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            logger.error("Bad request:" + e.getMessage());
            throw new BadRequestException(TAG_BAD_REQUEST, e, new Object[]{id});
        }
        if (tagService.deleteTag(parseId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            throw new NotFoundException(TAG_NOT_FOUND, new Object[]{id});
        }
    }
}