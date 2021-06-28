package com.epam.esm.model.hateoas.assembler;

import com.epam.esm.model.entity.OrderItem;
import com.epam.esm.model.hateoas.model.OrderItemModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class OrderItemModelAssembler implements RepresentationModelAssembler<OrderItem, OrderItemModel> {

    private final GiftCertificateModelAssembler giftCertificateModelAssembler;

    @Autowired
    public OrderItemModelAssembler(GiftCertificateModelAssembler giftCertificateModelAssembler) {
        this.giftCertificateModelAssembler = giftCertificateModelAssembler;
    }

    @Override
    public OrderItemModel toModel(OrderItem entity) {
        OrderItemModel orderItemModel = new OrderItemModel();
        orderItemModel.setQuantity(entity.getQuantity());
        orderItemModel.setGiftCertificate(giftCertificateModelAssembler.toModel(entity.getGiftCertificate()));
        return orderItemModel;
    }
}
