package com.epam.esm.controller;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.exception.BadRequestException;
import com.epam.esm.model.exception.NotFoundException;
import com.epam.esm.model.service.TagService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.epam.esm.model.error.MessageKeyError.TAG_BAD_REQUEST;
import static com.epam.esm.model.error.MessageKeyError.TAG_NOT_FOUND;

@RestController
@RequestMapping("/tags")
public class TagController {
    private static final Logger logger = LogManager.getLogger(TagController.class);
    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping
    ResponseEntity<Tag> createTag(@RequestBody Tag tag) {
        logger.debug("Request body: " + tag);
        Optional<Tag> optionalTag = tagService.create(tag);
        if (optionalTag.isPresent()) {
            Tag createdTag = optionalTag.get();
            return new ResponseEntity<>(createdTag, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    ResponseEntity<Tag> findTag(@PathVariable String id) throws NotFoundException, BadRequestException {
        logger.debug("Path variable: " + id);
        long parseId;
        try {
            parseId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            logger.error("Bad request:" + e.getMessage());
            throw new BadRequestException(TAG_BAD_REQUEST, e, new Object[]{id});
        }
        Optional<Tag> optionalTag = tagService.findTag(parseId);
        return optionalTag.map(tag -> new ResponseEntity<>(tag, HttpStatus.OK))
                .orElseThrow(() -> new NotFoundException(TAG_NOT_FOUND, new Object[]{id}));
    }

    @GetMapping
    ResponseEntity<List<Tag>> findAllTags() {
        List<Tag> tags = tagService.findAllTag();
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Object> deleteTag(@PathVariable String id) throws NotFoundException, BadRequestException {
        logger.debug("Path variable: " + id);
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
