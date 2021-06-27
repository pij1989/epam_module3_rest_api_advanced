package com.epam.esm.controller;

import com.epam.esm.model.entity.Order;
import com.epam.esm.model.hateoas.assembler.OrderModelAssembler;
import com.epam.esm.model.hateoas.model.OrderModel;
import com.epam.esm.model.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(value = "/orders", produces = {MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE})
public class OrderController {
    public static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;
    private final OrderModelAssembler orderModelAssembler;

    @Autowired
    public OrderController(OrderService orderService, OrderModelAssembler orderModelAssembler) {
        this.orderService = orderService;
        this.orderModelAssembler = orderModelAssembler;
    }

    @PostMapping
    public ResponseEntity<OrderModel> createOrder(@RequestBody Order order) {
        logger.debug("ORDER: " + order);
        Optional<Order>optionalOrder = orderService.createOrder(order);
        if (optionalOrder.isPresent()) {
            Order createdOrder = optionalOrder.get();
            OrderModel orderModel = orderModelAssembler.toModel(createdOrder);
            return new ResponseEntity<>(orderModel, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
