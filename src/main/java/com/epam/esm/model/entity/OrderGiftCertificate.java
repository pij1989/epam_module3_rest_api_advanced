package com.epam.esm.model.entity;

import javax.persistence.*;

@javax.persistence.Entity(name = "order_gift_certificates")
public class OrderGiftCertificate implements Entity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    @ManyToOne
    @JoinColumn(name = "gift_certificate_id")
    private GiftCertificate giftCertificate;
    private int amount;

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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void addOrder(Order order) {
        this.order = order;
        order.getOrderGiftCertificates().add(this);
    }

    public void removeOrder(Order order){
        this.order = null;
        order.getOrderGiftCertificates().remove(this);
    }

    public void addGiftCertificate(GiftCertificate giftCertificate){
        this.giftCertificate = giftCertificate;
        giftCertificate.getOrderGiftCertificates().add(this);
    }

    public void removeGiftCertificate(GiftCertificate giftCertificate){
        this.order = null;
        giftCertificate.getOrderGiftCertificates().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderGiftCertificate that = (OrderGiftCertificate) o;

        if (amount != that.amount) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (order != null ? !order.equals(that.order) : that.order != null) return false;
        return giftCertificate != null ? giftCertificate.equals(that.giftCertificate) : that.giftCertificate == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (order != null ? order.hashCode() : 0);
        result = 31 * result + (giftCertificate != null ? giftCertificate.hashCode() : 0);
        result = 31 * result + amount;
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OrderGiftCertificate{");
        sb.append("id=").append(id);
        sb.append(", amount=").append(amount);
        sb.append('}');
        return sb.toString();
    }
}
