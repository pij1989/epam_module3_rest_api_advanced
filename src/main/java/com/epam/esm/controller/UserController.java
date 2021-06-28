package com.epam.esm.controller;

import com.epam.esm.model.creator.PageModelCreator;
import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.Page;
import com.epam.esm.model.entity.User;
import com.epam.esm.model.exception.BadRequestException;
import com.epam.esm.model.exception.NotFoundException;
import com.epam.esm.model.hateoas.assembler.OrderModelAssembler;
import com.epam.esm.model.hateoas.assembler.UserModelAssembler;
import com.epam.esm.model.hateoas.model.OrderModel;
import com.epam.esm.model.hateoas.model.UserModel;
import com.epam.esm.model.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.epam.esm.model.error.MessageKeyError.*;

@RestController
@RequestMapping(value = "/users", produces = {MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE})
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final UserModelAssembler userModelAssembler;
    private final OrderModelAssembler orderModelAssembler;

    @Autowired
    public UserController(UserService userService, UserModelAssembler userModelAssembler, OrderModelAssembler orderModelAssembler) {
        this.userService = userService;
        this.userModelAssembler = userModelAssembler;
        this.orderModelAssembler = orderModelAssembler;
    }

    @PostMapping
    public ResponseEntity<UserModel> createUser(@RequestBody User user) {
        logger.debug("USER: " + user);
        Optional<User> optionalUser = userService.createUser(user);
        if (optionalUser.isPresent()) {
            User createdUser = optionalUser.get();
            UserModel userModel = userModelAssembler.toModel(createdUser);
            return new ResponseEntity<>(userModel, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity<OrderModel> createUserOrder(@PathVariable String userId, @RequestBody Order order) throws BadRequestException, NotFoundException {
        logger.debug("USER'S ORDER:" + order);
        long parseUserId;
        try {
            parseUserId = Long.parseLong(userId);
        } catch (NumberFormatException e) {
            logger.error("Bad request:" + e.getMessage());
            throw new BadRequestException(BAD_REQUEST, e, new Object[]{});
        }
        Optional<Order> optionalOrder = userService.createOrderForUser(parseUserId, order);
        return optionalOrder.map(orderModelAssembler::toModel)
                .map(orderModel -> new ResponseEntity<>(orderModel, HttpStatus.CREATED))
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND, new Object[]{userId}));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserModel> findUser(@PathVariable String id) throws BadRequestException, NotFoundException {
        long parseId;
        try {
            parseId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            logger.error("Bad request:" + e.getMessage());
            throw new BadRequestException(BAD_REQUEST, e, new Object[]{});
        }
        Optional<User> optionalUser = userService.findUser(parseId);
        return optionalUser.map(userModelAssembler::toModel)
                .map(userModel -> new ResponseEntity<>(userModel, HttpStatus.OK))
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND, new Object[]{id}));
    }

    @GetMapping
    public ResponseEntity<PagedModel<UserModel>> findUsers(@RequestParam(name = "page", defaultValue = RequestParameter.DEFAULT_PAGE_NUMBER) String page,
                                                           @RequestParam(name = "size", defaultValue = RequestParameter.DEFAULT_PAGE_SIZE) String size) throws BadRequestException, NotFoundException {
        int numPage;
        int numSize;
        try {
            numPage = Integer.parseInt(page);
            numSize = Integer.parseInt(size);
        } catch (NumberFormatException e) {
            logger.error("Bad request:" + e.getMessage());
            throw new BadRequestException(BAD_REQUEST, e, new Object[]{});
        }
        Page<User> userPage = userService.findUsers(numPage, numSize);
        if (!userPage.getList().isEmpty()) {
            PagedModel<UserModel> userModels = PageModelCreator.create(userPage, userModelAssembler);
            return new ResponseEntity<>(userModels, HttpStatus.OK);
        } else {
            throw new NotFoundException(USERS_NOT_FOUND, new Object[]{});
        }
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<PagedModel<OrderModel>> findOrdersForUser(@PathVariable String userId,
                                                    @RequestParam(name = "page", defaultValue = RequestParameter.DEFAULT_PAGE_NUMBER) String page,
                                                    @RequestParam(name = "size", defaultValue = RequestParameter.DEFAULT_PAGE_SIZE) String size) throws BadRequestException, NotFoundException {
        long numUserId;
        int numPage;
        int numSize;
        try {
            numUserId = Long.parseLong(userId);
            numPage = Integer.parseInt(page);
            numSize = Integer.parseInt(size);
        } catch (NumberFormatException e) {
            logger.error("Bad request:" + e.getMessage());
            throw new BadRequestException(BAD_REQUEST, e, new Object[]{});
        }
        Page<Order> orderPage = userService.findOrdersForUser(numUserId, numPage, numSize);
        List<Order> orders = orderPage.getList();
        if(orders == null){
            throw new NotFoundException(USER_NOT_FOUND, new Object[]{userId});
        }
        if(!orders.isEmpty()){
            PagedModel<OrderModel>orderModels = PageModelCreator.create(orderPage,orderModelAssembler);
            return new ResponseEntity<>(orderModels, HttpStatus.OK);
        } else {
            throw new NotFoundException(USER_ORDERS_NOT_FOUND, new Object[]{userId});
        }
    }

    @PutMapping("/{userId}/orders/{orderId}")
    public ResponseEntity<UserModel> addOrderToUser(@PathVariable String userId,
                                                    @PathVariable String orderId) throws BadRequestException, NotFoundException {
        long parseUserId;
        long parseOrderId;
        try {
            parseUserId = Long.parseLong(userId);
            parseOrderId = Long.parseLong(orderId);
        } catch (NumberFormatException e) {
            logger.error("Bad request:" + e.getMessage());
            throw new BadRequestException(BAD_REQUEST, e, new Object[]{});
        }
        Optional<User> optionalUser = userService.addOrderToUser(parseUserId, parseOrderId);
        return optionalUser.map(userModelAssembler::toModel)
                .map(userModel -> new ResponseEntity<>(userModel, HttpStatus.OK))
                .orElseThrow(() -> new NotFoundException(NOT_FOUND, new Object[]{}));
    }
}
