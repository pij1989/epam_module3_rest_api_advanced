package com.epam.esm.model.creator;

import com.epam.esm.model.entity.Entity;
import com.epam.esm.model.entity.Page;
import org.springframework.hateoas.*;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.web.util.UriComponentsBuilder.fromUri;

public class PageModelCreator<T extends Entity> {
    private static final String DEFAULT_REQUEST_PARAM_PAGE = "page";
    private static final String DEFAULT_REQUEST_PARAM_SIZE = "size";

    private PageModelCreator() {
    }

    public static <T extends Entity> PagedModel<EntityModel<T>> create(Page<T> page, RepresentationModelAssembler<T,
            EntityModel<T>> representationModelAssembler) {
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(page.getSize(), page.getNumber(),
                page.getTotalElements(), page.getTotalPages());
        List<EntityModel<T>> entityModels = page.getList().stream()
                .map(representationModelAssembler::toModel)
                .collect(Collectors.toList());
        PagedModel<EntityModel<T>> pagedModel = PagedModel.of(entityModels, pageMetadata);
        return addPaginationLinks(pagedModel, page);
    }

    private static <T extends Entity> PagedModel<EntityModel<T>> addPaginationLinks(PagedModel<EntityModel<T>> pagedModel, Page<T> page) {

        UriTemplate base = UriTemplate.of(ServletUriComponentsBuilder.fromCurrentRequest().build().toString());

        boolean isNavigable = page.hasPrevious() || page.hasNext();

        if (isNavigable) {
            pagedModel.add(createLink(base, 1, page.getSize(), IanaLinkRelations.FIRST));
        }

        if (page.hasPrevious()) {
            pagedModel.add(createLink(base, page.getNumber() - 1, page.getSize(), IanaLinkRelations.PREV));
        }

        pagedModel.add(createLink(base, page.getNumber(), page.getSize(), IanaLinkRelations.SELF));

        if (page.hasNext()) {
            pagedModel.add(createLink(base, page.getNumber() + 1, page.getSize(), IanaLinkRelations.NEXT));
        }

        if (isNavigable) {
            pagedModel.add(createLink(base, page.getTotalPages(), page.getSize(), IanaLinkRelations.LAST));
        }
        return pagedModel;
    }

    private static Link createLink(UriTemplate base, int page, int size, LinkRelation rel) {
        UriComponentsBuilder builder = fromUri(base.expand());
        builder.replaceQueryParam(DEFAULT_REQUEST_PARAM_PAGE, page);
        builder.replaceQueryParam(DEFAULT_REQUEST_PARAM_SIZE, size);
        return Link.of(UriTemplate.of(builder.build().toString()), rel);
    }
}
