package com.epam.esm.controller;

import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.exception.BadRequestException;
import com.epam.esm.model.exception.NotFoundException;
import com.epam.esm.model.hateoas.assembler.GiftCertificateModelAssembler;
import com.epam.esm.model.hateoas.model.GiftCertificateModel;
import com.epam.esm.model.service.GiftCertificateService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.model.error.MessageKeyError.*;

@RestController
@RequestMapping("/gift_certificates")
public class GiftCertificateController {
    private static final Logger logger = LogManager.getLogger(GiftCertificate.class);
    private final GiftCertificateService giftCertificateService;
    private final GiftCertificateModelAssembler giftCertificateModelAssembler;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService, GiftCertificateModelAssembler giftCertificateModelAssembler) {
        this.giftCertificateService = giftCertificateService;
        this.giftCertificateModelAssembler = giftCertificateModelAssembler;
    }

    @PostMapping
    public ResponseEntity<GiftCertificateModel> createGiftCertificate(@RequestBody GiftCertificate giftCertificate) {
        logger.debug("Created gift certificate: " + giftCertificate);
        Optional<GiftCertificate> optionalGiftCertificate = giftCertificateService.createGiftCertificate(giftCertificate);
        if (optionalGiftCertificate.isPresent()) {
            GiftCertificate createdGiftCertificate = optionalGiftCertificate.get();
            GiftCertificateModel giftCertificateModel = giftCertificateModelAssembler.toModel(createdGiftCertificate);
            return new ResponseEntity<>(giftCertificateModel, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{certificateId}")
    public ResponseEntity<GiftCertificateModel> findGiftCertificate(@PathVariable String certificateId) throws BadRequestException, NotFoundException {
        long parseId;
        try {
            parseId = Long.parseLong(certificateId);
        } catch (NumberFormatException e) {
            logger.error("Bad request:" + e.getMessage());
            throw new BadRequestException(CERTIFICATE_BAD_REQUEST, e, new Object[]{certificateId});
        }
        Optional<GiftCertificate> optionalGiftCertificate = giftCertificateService.findGiftCertificate(parseId);
        return optionalGiftCertificate.map(giftCertificate -> new ResponseEntity<>(giftCertificateModelAssembler.toModel(giftCertificate), HttpStatus.OK))
                .orElseThrow(() -> new NotFoundException(CERTIFICATE_NOT_FOUND_ID, new Object[]{certificateId}));
    }

    @GetMapping
    public ResponseEntity<CollectionModel<GiftCertificateModel>> findAllGiftCertificate() {
        List<GiftCertificate> giftCertificates = giftCertificateService.findAllGiftCertificate();
        CollectionModel<GiftCertificateModel> certificateModels = giftCertificateModelAssembler.toCollectionModel(giftCertificates);
        return new ResponseEntity<>(certificateModels, HttpStatus.OK);
    }

    @GetMapping(params = {"tagName"})
    public ResponseEntity<List<GiftCertificate>> findGiftCertificateByTagName(@RequestParam("tagName") String name) throws NotFoundException {
        logger.debug("Name: " + name);
        List<GiftCertificate> giftCertificates = giftCertificateService.findGiftCertificateByTagName(name);
        if (!giftCertificates.isEmpty()) {
            return new ResponseEntity<>(giftCertificates, HttpStatus.OK);
        } else {
            throw new NotFoundException(CERTIFICATE_NOT_FOUND_TAG_NAME, new Object[]{name});
        }
    }

    @GetMapping(params = {"filter"})
    public ResponseEntity<List<GiftCertificate>> searchGiftCertificate(@RequestParam("filter") String filter) {
        logger.debug("Filter: " + filter);
        List<GiftCertificate> giftCertificates = giftCertificateService.searchGiftCertificate(filter);
        return new ResponseEntity<>(giftCertificates, HttpStatus.OK);
    }

    @GetMapping(params = {"sort"})
    public ResponseEntity<List<GiftCertificate>> sortGiftCertificate(@RequestParam("sort") String sort) {
        logger.debug("Sort: " + sort);
        List<GiftCertificate> giftCertificates = giftCertificateService.sortGiftCertificate(sort);
        return new ResponseEntity<>(giftCertificates, HttpStatus.OK);
    }

    @PutMapping("/{certificateId}")
    public ResponseEntity<GiftCertificate> updateGiftCertificate(@RequestBody GiftCertificate giftCertificate,
                                                                 @PathVariable String certificateId) throws BadRequestException, NotFoundException {
        logger.debug("Path variable: " + certificateId);
        long parseId;
        try {
            parseId = Long.parseLong(certificateId);
        } catch (NumberFormatException e) {
            logger.error("Bad request:" + e.getMessage());
            throw new BadRequestException(CERTIFICATE_BAD_REQUEST, e, new Object[]{certificateId});
        }
        Optional<GiftCertificate> optionalGiftCertificate = giftCertificateService.updateGiftCertificate(giftCertificate, parseId);
        return optionalGiftCertificate.map(certificate -> new ResponseEntity<>(certificate, HttpStatus.OK))
                .orElseThrow(() -> new NotFoundException(CERTIFICATE_NOT_FOUND_ID, new Object[]{certificateId}));
    }

    @PatchMapping("/{certificateId}")
    public ResponseEntity<GiftCertificate> updatePartOfGiftCertificate(@RequestBody GiftCertificate giftCertificate,
                                                                       @PathVariable String certificateId) throws BadRequestException, NotFoundException {
        logger.debug("Path variable: " + certificateId);
        long parseId;
        try {
            parseId = Long.parseLong(certificateId);
        } catch (NumberFormatException e) {
            logger.error("Bad request:" + e.getMessage());
            throw new BadRequestException(CERTIFICATE_BAD_REQUEST, e, new Object[]{certificateId});
        }
        Optional<GiftCertificate> optionalGiftCertificate = giftCertificateService.updatePartGiftCertificate(giftCertificate, parseId);
        return optionalGiftCertificate.map(certificate -> new ResponseEntity<>(certificate, HttpStatus.OK))
                .orElseThrow(() -> new NotFoundException(CERTIFICATE_NOT_FOUND_ID, new Object[]{certificateId}));
    }

    @PostMapping("{certificateId}/tags")
    public ResponseEntity<Tag> createTagInGiftCertificate(@PathVariable String certificateId,
                                                          @RequestBody Tag tag,
                                                          HttpServletRequest request) throws BadRequestException {
        logger.debug("Path variable: " + certificateId);
        long parseId;
        try {
            parseId = Long.parseLong(certificateId);
        } catch (NumberFormatException e) {
            logger.error("Bad request:" + e.getMessage());
            throw new BadRequestException(CERTIFICATE_BAD_REQUEST, e, new Object[]{certificateId});
        }
        Optional<Tag> optionalTag = giftCertificateService.createTagInGiftCertificate(parseId, tag);
        return optionalTag.map(createdTag -> {
            HttpHeaders responseHeaders = new HttpHeaders();
            String location = request.getRequestURL().append("/").append(createdTag.getId()).toString();
            responseHeaders.set(HttpHeaders.LOCATION, location);
            return new ResponseEntity<>(createdTag, responseHeaders, HttpStatus.CREATED);
        }).orElseThrow(() -> new BadRequestException(CERTIFICATE_BAD_REQUEST_TAG_CREATED));
    }

    @PutMapping("{certificateId}/tags/{tagId}")
    public ResponseEntity<Object> addTagToGiftCertificate(@PathVariable String certificateId, @PathVariable String tagId) throws BadRequestException, NotFoundException {
        logger.debug("Path variable: " + certificateId + "," + tagId);
        long parseCertificateId;
        long parseTagId;
        try {
            parseCertificateId = Long.parseLong(certificateId);
        } catch (NumberFormatException e) {
            logger.error("Bad request:" + e.getMessage());
            throw new BadRequestException(CERTIFICATE_BAD_REQUEST, e, new Object[]{certificateId});
        }
        try {
            parseTagId = Long.parseLong(tagId);
        } catch (NumberFormatException e) {
            logger.error("Bad request:" + e.getMessage());
            throw new BadRequestException(CERTIFICATE_BAD_REQUEST, e, new Object[]{tagId});
        }
        if (giftCertificateService.addTagToGiftCertificate(parseCertificateId, parseTagId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            throw new NotFoundException(CERTIFICATE_OR_TAG_NOT_FOUND, new Object[]{certificateId, tagId});
        }
    }

    @DeleteMapping("/{certificateId}")
    public ResponseEntity<Object> deleteGiftCertificate(@PathVariable String certificateId) throws BadRequestException, NotFoundException {
        logger.debug("Path variable: " + certificateId);
        long parseId;
        try {
            parseId = Long.parseLong(certificateId);
        } catch (NumberFormatException e) {
            logger.error("Bad request:" + e.getMessage());
            throw new BadRequestException(CERTIFICATE_BAD_REQUEST, e, new Object[]{certificateId});
        }
        if (giftCertificateService.deleteGiftCertificate(parseId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            throw new NotFoundException(CERTIFICATE_NOT_FOUND_ID, new Object[]{certificateId});
        }
    }
}
