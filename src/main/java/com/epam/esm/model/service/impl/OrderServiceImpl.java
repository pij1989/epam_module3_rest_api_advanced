package com.epam.esm.model.service.impl;

import com.epam.esm.model.dao.GiftCertificateDao;
import com.epam.esm.model.dao.OrderDao;
import com.epam.esm.model.dao.OrderItemDao;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.OrderItem;
import com.epam.esm.model.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;
    private final GiftCertificateDao certificateDao;

    @Autowired
    public OrderServiceImpl(OrderDao orderDao, OrderItemDao orderItemDao, GiftCertificateDao certificateDao) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
        this.certificateDao = certificateDao;
    }

    @Override
    @Transactional
    public Optional<Order> createOrder(Order order) {
        if (order != null) {
            Order createdOrder = orderDao.create(order);
            return Optional.of(createdOrder);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Order> findOrder(Long id) {
        return orderDao.findById(id);
    }

    @Override
    @Transactional
    public Optional<OrderItem> addGiftCertificateToOrder(Long orderId, Long certificateId, OrderItem orderItem) {
        Optional<Order> optionalOrder = orderDao.findById(orderId);
        if (optionalOrder.isPresent()) {
            Optional<GiftCertificate> optionalGiftCertificate = certificateDao.findById(certificateId);
            if (optionalGiftCertificate.isPresent()) {
                Order order = optionalOrder.get();
                GiftCertificate giftCertificate = optionalGiftCertificate.get();
                BigDecimal calculatedCost = calculateCost(giftCertificate.getPrice(), order.getCost(), orderItem.getQuantity());
                order.setCost(calculatedCost);
                orderItem.setOrder(order);
                orderItem.addGiftCertificate(giftCertificate);
                return Optional.of(orderItemDao.create(orderItem));
            }
        }
        return Optional.empty();
    }

    private BigDecimal calculateCost(BigDecimal price, BigDecimal totalCost, int quantity) {
        if (totalCost == null) {
            totalCost = BigDecimal.ZERO;
        }
        BigDecimal itemCost = price.multiply(BigDecimal.valueOf(quantity));
        return totalCost.add(itemCost);
    }
}
