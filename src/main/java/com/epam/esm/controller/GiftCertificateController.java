package com.epam.esm.controller;

import com.epam.esm.model.creator.PageModelCreator;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Page;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.exception.BadRequestException;
import com.epam.esm.model.exception.NotFoundException;
import com.epam.esm.model.hateoas.assembler.GiftCertificateModelAssembler;
import com.epam.esm.model.hateoas.assembler.TagModelAssembler;
import com.epam.esm.model.hateoas.model.GiftCertificateModel;
import com.epam.esm.model.hateoas.model.TagModel;
import com.epam.esm.model.service.GiftCertificateService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static com.epam.esm.controller.RequestParameter.DEFAULT_PAGE_NUMBER;
import static com.epam.esm.controller.RequestParameter.DEFAULT_PAGE_SIZE;
import static com.epam.esm.model.error.MessageKeyError.*;

@RestController
@RequestMapping(value = "/gift_certificates", produces = {MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE})
public class GiftCertificateController {
    private static final Logger logger = LogManager.getLogger(GiftCertificate.class);
    private final GiftCertificateService giftCertificateService;
    private final GiftCertificateModelAssembler giftCertificateModelAssembler;
    private final TagModelAssembler tagModelAssembler;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService,
                                     GiftCertificateModelAssembler giftCertificateModelAssembler,
                                     TagModelAssembler tagModelAssembler) {
        this.giftCertificateService = giftCertificateService;
        this.giftCertificateModelAssembler = giftCertificateModelAssembler;
        this.tagModelAssembler = tagModelAssembler;
    }

    @PostMapping
    public ResponseEntity<GiftCertificateModel> createGiftCertificate(@RequestBody GiftCertificate giftCertificate) {
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
    public ResponseEntity<GiftCertificateModel> findGiftCertificate(@PathVariable Long certificateId) throws NotFoundException {
        Optional<GiftCertificate> optionalGiftCertificate = giftCertificateService.findGiftCertificate(certificateId);
        return optionalGiftCertificate.map(giftCertificate -> new ResponseEntity<>(giftCertificateModelAssembler.toModel(giftCertificate), HttpStatus.OK))
                .orElseThrow(() -> new NotFoundException(CERTIFICATE_NOT_FOUND_ID, new Object[]{certificateId}));
    }

    @GetMapping
    public ResponseEntity<PagedModel<GiftCertificateModel>> findGiftCertificates(@RequestParam(name = "page", defaultValue = DEFAULT_PAGE_NUMBER) Integer page,
                                                                                 @RequestParam(name = "size", defaultValue = DEFAULT_PAGE_SIZE) Integer size) throws NotFoundException {
        Page<GiftCertificate> giftCertificatePage = giftCertificateService.findGiftCertificates(page, size);
        if (!giftCertificatePage.getList().isEmpty()) {
            PagedModel<GiftCertificateModel> certificateModels = PageModelCreator.create(giftCertificatePage, giftCertificateModelAssembler);
            return new ResponseEntity<>(certificateModels, HttpStatus.OK);
        } else {
            throw new NotFoundException(NOT_FOUND, new Object[]{});
        }
    }

    @GetMapping(params = {"tagName"})
    public ResponseEntity<PagedModel<GiftCertificateModel>> findGiftCertificateByTagName(@RequestParam("tagName") String name,
                                                                                         @RequestParam(name = "page", defaultValue = DEFAULT_PAGE_NUMBER) Integer page,
                                                                                         @RequestParam(name = "size", defaultValue = DEFAULT_PAGE_SIZE) Integer size) throws NotFoundException {
        Page<GiftCertificate> giftCertificatePage = giftCertificateService.findGiftCertificateByTagName(name, page, size);
        if (!giftCertificatePage.getList().isEmpty()) {
            PagedModel<GiftCertificateModel> certificateModels = PageModelCreator.create(giftCertificatePage, giftCertificateModelAssembler);
            return new ResponseEntity<>(certificateModels, HttpStatus.OK);
        } else {
            throw new NotFoundException(CERTIFICATE_NOT_FOUND_TAG_NAME, new Object[]{name});
        }
    }

    @GetMapping(params = {"filter"})
    public ResponseEntity<PagedModel<GiftCertificateModel>> searchGiftCertificate(@RequestParam("filter") String filter,
                                                                                  @RequestParam(name = "page", defaultValue = DEFAULT_PAGE_NUMBER) Integer page,
                                                                                  @RequestParam(name = "size", defaultValue = DEFAULT_PAGE_SIZE) Integer size) throws NotFoundException {
        Page<GiftCertificate> giftCertificatePage = giftCertificateService.searchGiftCertificate(filter, page, size);
        if (!giftCertificatePage.getList().isEmpty()) {
            PagedModel<GiftCertificateModel> certificateModels = PageModelCreator.create(giftCertificatePage, giftCertificateModelAssembler);
            return new ResponseEntity<>(certificateModels, HttpStatus.OK);
        } else {
            throw new NotFoundException(NOT_FOUND, new Object[]{});
        }
    }

    @GetMapping(params = {"sort", "order"})
    public ResponseEntity<PagedModel<GiftCertificateModel>> sortGiftCertificate(@RequestParam("sort") String sort,
                                                                                @RequestParam("order") String order,
                                                                                @RequestParam(name = "page", defaultValue = DEFAULT_PAGE_NUMBER) Integer page,
                                                                                @RequestParam(name = "size", defaultValue = DEFAULT_PAGE_SIZE) Integer size) throws BadRequestException, NotFoundException {
        Page<GiftCertificate> giftCertificatePage = giftCertificateService.sortGiftCertificate(sort, order, page, size);
        if (!giftCertificatePage.getList().isEmpty()) {
            PagedModel<GiftCertificateModel> certificateModels = PageModelCreator.create(giftCertificatePage, giftCertificateModelAssembler);
            return new ResponseEntity<>(certificateModels, HttpStatus.OK);
        } else {
            throw new NotFoundException(NOT_FOUND, new Object[]{});
        }
    }

    @PutMapping("/{certificateId}")
    public ResponseEntity<GiftCertificateModel> updateGiftCertificate(@RequestBody GiftCertificate giftCertificate,
                                                                      @PathVariable Long certificateId) throws NotFoundException {
        Optional<GiftCertificate> optionalGiftCertificate = giftCertificateService.updateGiftCertificate(giftCertificate, certificateId);
        return optionalGiftCertificate.map(certificate -> new ResponseEntity<>(giftCertificateModelAssembler.toModel(certificate), HttpStatus.OK))
                .orElseThrow(() -> new NotFoundException(CERTIFICATE_NOT_FOUND_ID, new Object[]{certificateId}));
    }

    @PatchMapping("/{certificateId}")
    public ResponseEntity<GiftCertificateModel> updatePartOfGiftCertificate(@RequestBody GiftCertificate giftCertificate,
                                                                            @PathVariable Long certificateId) throws NotFoundException {
        Optional<GiftCertificate> optionalGiftCertificate = giftCertificateService.updatePartGiftCertificate(giftCertificate, certificateId);
        return optionalGiftCertificate.map(certificate -> new ResponseEntity<>(giftCertificateModelAssembler.toModel(certificate), HttpStatus.OK))
                .orElseThrow(() -> new NotFoundException(CERTIFICATE_NOT_FOUND_ID, new Object[]{certificateId}));
    }

    @PostMapping("{certificateId}/tags")
    public ResponseEntity<TagModel> createTagInGiftCertificate(@PathVariable Long certificateId,
                                                               @RequestBody Tag tag,
                                                               HttpServletRequest request) throws BadRequestException {
        Optional<Tag> optionalTag = giftCertificateService.createTagInGiftCertificate(certificateId, tag);
        return optionalTag.map(createdTag -> {
            HttpHeaders responseHeaders = new HttpHeaders();
            String location = request.getRequestURL().append("/").append(createdTag.getId()).toString();
            responseHeaders.set(HttpHeaders.LOCATION, location);
            return new ResponseEntity<>(tagModelAssembler.toModel(createdTag), responseHeaders, HttpStatus.CREATED);
        }).orElseThrow(() -> new BadRequestException(CERTIFICATE_BAD_REQUEST_TAG_CREATED));
    }

    @PutMapping("{certificateId}/tags/{tagId}")
    public ResponseEntity<Object> addTagToGiftCertificate(@PathVariable Long certificateId,
                                                          @PathVariable Long tagId) throws NotFoundException {
        if (giftCertificateService.addTagToGiftCertificate(certificateId, tagId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            throw new NotFoundException(CERTIFICATE_OR_TAG_NOT_FOUND, new Object[]{certificateId, tagId});
        }
    }

    @DeleteMapping("/{certificateId}")
    public ResponseEntity<Object> deleteGiftCertificate(@PathVariable Long certificateId) throws NotFoundException {
        if (giftCertificateService.deleteGiftCertificate(certificateId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            throw new NotFoundException(CERTIFICATE_NOT_FOUND_ID, new Object[]{certificateId});
        }
    }

    @PatchMapping("/{certificateId}/price")
    public ResponseEntity<GiftCertificateModel> updatePriceOfGiftCertificate(@RequestBody GiftCertificate giftCertificate,
                                                                             @PathVariable Long certificateId) throws NotFoundException {
        Optional<GiftCertificate> optionalGiftCertificate = giftCertificateService.updatePriceGiftCertificate(giftCertificate.getPrice(), certificateId);
        return optionalGiftCertificate.map(certificate -> new ResponseEntity<>(giftCertificateModelAssembler.toModel(certificate), HttpStatus.OK))
                .orElseThrow(() -> new NotFoundException(CERTIFICATE_NOT_FOUND_ID, new Object[]{certificateId}));
    }

    @GetMapping(params = {"tagname"})
    public ResponseEntity<PagedModel<GiftCertificateModel>> searchGiftCertificateByTags(@RequestParam("tagname") String[] tagNames,
                                                                                        @RequestParam(name = "page", defaultValue = DEFAULT_PAGE_NUMBER) Integer page,
                                                                                        @RequestParam(name = "size", defaultValue = DEFAULT_PAGE_SIZE) Integer size) throws NotFoundException {
        Page<GiftCertificate> certificatePage = giftCertificateService.searchGiftCertificateByTags(tagNames, page, size);
        if (!certificatePage.getList().isEmpty()) {
            PagedModel<GiftCertificateModel> certificateModels = PageModelCreator.create(certificatePage, giftCertificateModelAssembler);
            return new ResponseEntity<>(certificateModels, HttpStatus.OK);
        } else {
            throw new NotFoundException(NOT_FOUND, new Object[]{});
        }
    }
}
