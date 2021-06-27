package com.epam.esm.model.hateoas.assembler;

import com.epam.esm.model.entity.Order;
import com.epam.esm.model.hateoas.model.OrderModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class OrderModelAssembler implements RepresentationModelAssembler<Order, OrderModel> {
    @Override
    public OrderModel toModel(Order entity) {
        OrderModel orderModel = new OrderModel();
        orderModel.setId(entity.getId());
        orderModel.setCost(entity.getCost());
        orderModel.setCreateDate(entity.getCreateDate());
        orderModel.setUser(entity.getUser());
        orderModel.setOrderItemModels(new HashSet<>());
        return orderModel;
    }
}
