package com.epam.esm.model.hateoas.assembler;

import com.epam.esm.controller.OrderController;
import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.OrderItem;
import com.epam.esm.model.exception.NotFoundException;
import com.epam.esm.model.hateoas.model.OrderItemModel;
import com.epam.esm.model.hateoas.model.OrderModel;
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
public class OrderModelAssembler implements RepresentationModelAssembler<Order, OrderModel> {
    private static final Logger logger = LoggerFactory.getLogger(OrderModelAssembler.class);
    private final OrderItemModelAssembler orderItemModelAssembler;

    @Autowired
    public OrderModelAssembler(OrderItemModelAssembler orderItemModelAssembler) {
        this.orderItemModelAssembler = orderItemModelAssembler;
    }

    @Override
    public OrderModel toModel(Order entity) {
        OrderModel orderModel = new OrderModel();
        try {
            orderModel.add(linkTo(methodOn(OrderController.class)
                    .findOrder(entity.getId())).withSelfRel());
        } catch (NotFoundException e) {
            logger.error(e.getMessage());
        }
        orderModel.setId(entity.getId());
        orderModel.setCost(entity.getCost());
        orderModel.setCreateDate(entity.getCreateDate());
        orderModel.setOrderItems(toOrderItemModel(entity.getOrderItems()));
        return orderModel;
    }

    private Set<OrderItemModel> toOrderItemModel(Set<OrderItem> orderItems) {
        if (orderItems.isEmpty()) {
            return Collections.emptySet();
        }

        return orderItems.stream()
                .map(orderItemModelAssembler::toModel)
                .collect(Collectors.toSet());
    }
}
