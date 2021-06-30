package com.epam.esm.controller;

import com.epam.esm.model.creator.PageModelCreator;
import com.epam.esm.model.entity.Page;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.exception.NotFoundException;
import com.epam.esm.model.hateoas.assembler.TagModelAssembler;
import com.epam.esm.model.hateoas.model.TagModel;
import com.epam.esm.model.service.TagService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.epam.esm.controller.RequestParameter.DEFAULT_PAGE_NUMBER;
import static com.epam.esm.controller.RequestParameter.DEFAULT_PAGE_SIZE;
import static com.epam.esm.model.error.MessageKeyError.NOT_FOUND;
import static com.epam.esm.model.error.MessageKeyError.TAG_NOT_FOUND;

@RestController
@RequestMapping(value = "/tags", produces = {MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE})
public class TagController {
    private static final Logger logger = LogManager.getLogger(TagController.class);
    private final TagService tagService;
    private final TagModelAssembler tagModelAssembler;

    @Autowired
    public TagController(TagService tagService, TagModelAssembler tagModelAssembler) {
        this.tagService = tagService;
        this.tagModelAssembler = tagModelAssembler;
    }

    @PostMapping
    public ResponseEntity<TagModel> createTag(@RequestBody Tag tag) {
        Optional<Tag> optionalTag = tagService.create(tag);
        if (optionalTag.isPresent()) {
            Tag createdTag = optionalTag.get();
            TagModel tagModel = tagModelAssembler.toModel(createdTag);
            return new ResponseEntity<>(tagModel, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagModel> findTag(@PathVariable Long id) throws NotFoundException {
        Optional<Tag> optionalTag = tagService.findTag(id);
        if (optionalTag.isPresent()) {
            Tag tag = optionalTag.get();
            TagModel tagModel = tagModelAssembler.toModel(tag);
            return new ResponseEntity<>(tagModel, HttpStatus.OK);
        } else {
            throw new NotFoundException(TAG_NOT_FOUND, new Object[]{id});
        }
    }

    @GetMapping
    public ResponseEntity<CollectionModel<TagModel>> findAllTags() {
        List<Tag> tags = tagService.findAllTag();
        CollectionModel<TagModel> tagCollectionModel = tagModelAssembler.toCollectionModel(tags);
        return new ResponseEntity<>(tagCollectionModel, HttpStatus.OK);
    }

    @GetMapping(params = {"page", "size"})
    public ResponseEntity<PagedModel<TagModel>> findTags(@RequestParam(defaultValue = DEFAULT_PAGE_NUMBER) Integer page,
                                                         @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) Integer size) throws NotFoundException {
        Page<Tag> tagPage = tagService.findTags(page, size);
        if (!tagPage.getList().isEmpty()) {
            PagedModel<TagModel> pagedModel = PageModelCreator.create(tagPage, tagModelAssembler);
            return new ResponseEntity<>(pagedModel, HttpStatus.OK);
        } else {
            throw new NotFoundException(NOT_FOUND, new Object[]{});
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTag(@PathVariable Long id) throws NotFoundException {
        if (tagService.deleteTag(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            throw new NotFoundException(TAG_NOT_FOUND, new Object[]{id});
        }
    }
}