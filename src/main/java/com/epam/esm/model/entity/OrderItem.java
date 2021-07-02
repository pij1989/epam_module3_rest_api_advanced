package com.epam.esm.model.entity;

import org.hibernate.envers.Audited;

import javax.persistence.*;

@javax.persistence.Entity(name = "order_items")
@Audited
public class OrderItem implements Entity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    @ManyToOne
    @JoinColumn(name = "gift_certificate_id")
    private GiftCertificate giftCertificate;
    private int quantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public GiftCertificate getGiftCertificate() {
        return giftCertificate;
    }

    public void setGiftCertificate(GiftCertificate giftCertificate) {
        this.giftCertificate = giftCertificate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int amount) {
        this.quantity = amount;
    }

    public void addOrder(Order order) {
        this.order = order;
        order.getOrderItems().add(this);
    }

    public void removeOrder(Order order){
        this.order = null;
        order.getOrderItems().remove(this);
    }

    public void addGiftCertificate(GiftCertificate giftCertificate){
        this.giftCertificate = giftCertificate;
        giftCertificate.getOrderItems().add(this);
    }

    public void removeGiftCertificate(GiftCertificate giftCertificate){
        this.order = null;
        giftCertificate.getOrderItems().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderItem orderItem = (OrderItem) o;

        if (quantity != orderItem.quantity) return false;
        return id != null ? id.equals(orderItem.id) : orderItem.id == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + quantity;
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OrderItem{");
        sb.append("id=").append(id);
        sb.append(", quantity=").append(quantity);
        sb.append('}');
        return sb.toString();
    }
}
