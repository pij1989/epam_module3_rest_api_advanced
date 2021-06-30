package com.epam.esm.model.hateoas.assembler;

import com.epam.esm.controller.UserController;
import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.User;
import com.epam.esm.model.exception.NotFoundException;
import com.epam.esm.model.hateoas.model.OrderModel;
import com.epam.esm.model.hateoas.model.UserModel;
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
public class UserModelAssembler implements RepresentationModelAssembler<User, UserModel> {
    private static final Logger logger = LoggerFactory.getLogger(UserModelAssembler.class);
    private final OrderModelAssembler orderModelAssembler;

    @Autowired
    public UserModelAssembler(OrderModelAssembler orderModelAssembler) {
        this.orderModelAssembler = orderModelAssembler;
    }

    @Override
    public UserModel toModel(User entity) {
        UserModel userModel = new UserModel();
        try {
            userModel.add(linkTo(methodOn(UserController.class)
                    .findUser(entity.getId())).withSelfRel());
        } catch (NotFoundException e) {
            logger.error(e.getMessage());
        }
        userModel.setId(entity.getId());
        userModel.setFirstName(entity.getFirstName());
        userModel.setLastName(entity.getLastName());
        userModel.setUsername(entity.getUsername());
        userModel.setEmail(entity.getEmail());
        userModel.setPassword(entity.getPassword());
        userModel.setRole(entity.getRole());
        userModel.setStatus(entity.getStatus());
        userModel.setOrders(toOrderModel(entity.getOrders()));
        return userModel;
    }

    private Set<OrderModel> toOrderModel(Set<Order> orders) {
        if (orders.isEmpty()) {
            return Collections.emptySet();
        }
        return orders.stream()
                /*.map(order -> {
                    OrderModel orderModel = new OrderModel();
                    orderModel.setId(order.getId());
                    orderModel.setCost(order.getCost());
                    orderModel.setCreateDate(order.getCreateDate());
                    orderModel.setUser(order.getUser());
                    return orderModel;
                })*/
                .map(orderModelAssembler::toModel)
                .collect(Collectors.toSet());
    }
}
