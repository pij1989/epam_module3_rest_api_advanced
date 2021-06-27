package com.epam.esm.controller;

import com.epam.esm.model.entity.User;
import com.epam.esm.model.exception.BadRequestException;
import com.epam.esm.model.exception.NotFoundException;
import com.epam.esm.model.hateoas.assembler.UserModelAssembler;
import com.epam.esm.model.hateoas.model.UserModel;
import com.epam.esm.model.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.epam.esm.model.error.MessageKeyError.BAD_REQUEST;
import static com.epam.esm.model.error.MessageKeyError.NOT_FOUND;

@RestController
@RequestMapping(value = "/users", produces = {MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE})
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final UserModelAssembler userModelAssembler;

    @Autowired
    public UserController(UserService userService, UserModelAssembler userModelAssembler) {
        this.userService = userService;
        this.userModelAssembler = userModelAssembler;
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
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            UserModel userModel = userModelAssembler.toModel(user);
            return new ResponseEntity<>(userModel, HttpStatus.OK);
        } else {
            throw new NotFoundException(NOT_FOUND, new Object[]{});
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
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            UserModel userModel = userModelAssembler.toModel(user);
            return new ResponseEntity<>(userModel, HttpStatus.OK);
        } else {
            throw new NotFoundException(NOT_FOUND, new Object[]{});
        }
    }
}
