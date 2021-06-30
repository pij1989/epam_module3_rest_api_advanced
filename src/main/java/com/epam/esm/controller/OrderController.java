package com.epam.esm.controller;

import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.OrderItem;
import com.epam.esm.model.exception.NotFoundException;
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
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.epam.esm.model.error.MessageKeyError.ORDER_NOT_FOUND;
import static com.epam.esm.model.error.MessageKeyError.ORDER_OR_CERTIFICATE_NOT_FOUND;

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
        Optional<Order> optionalOrder = orderService.createOrder(order);
        if (optionalOrder.isPresent()) {
            Order createdOrder = optionalOrder.get();
            OrderModel orderModel = orderModelAssembler.toModel(createdOrder);
            return new ResponseEntity<>(orderModel, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderModel> findOrder(@PathVariable Long id) throws NotFoundException {
        Optional<Order> optionalOrder = orderService.findOrder(id);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            OrderModel orderModel = orderModelAssembler.toModel(order);
            return new ResponseEntity<>(orderModel, HttpStatus.OK);
        } else {
            throw new NotFoundException(ORDER_NOT_FOUND, new Object[]{id});
        }
    }

    @PutMapping("/{orderId}/gift_certificates/{certificateId}")
    public ResponseEntity<OrderModel> addGiftCertificateToOrder(@PathVariable Long orderId,
                                                                @PathVariable Long certificateId,
                                                                @RequestBody OrderItem orderItem) throws NotFoundException {
        Optional<OrderItem> optionalOrderItem = orderService.addGiftCertificateToOrder(orderId, certificateId, orderItem);
        if (optionalOrderItem.isPresent()) {
            orderItem = optionalOrderItem.get();
            Order order = orderItem.getOrder();
            OrderModel orderModel = orderModelAssembler.toModel(order);
            return new ResponseEntity<>(orderModel, HttpStatus.OK);
        } else {
            throw new NotFoundException(ORDER_OR_CERTIFICATE_NOT_FOUND, new Object[]{orderId, certificateId});
        }
    }
}
