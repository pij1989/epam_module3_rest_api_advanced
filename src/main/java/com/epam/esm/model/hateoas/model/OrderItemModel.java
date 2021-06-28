package com.epam.esm.model.hateoas.model;

import org.springframework.hateoas.RepresentationModel;

public class OrderItemModel extends RepresentationModel<OrderItemModel> {
    private int quantity;
    private OrderModel order;
    private GiftCertificateModel giftCertificate;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public OrderModel getOrder() {
        return order;
    }

    public void setOrder(OrderModel order) {
        this.order = order;
    }

    public GiftCertificateModel getGiftCertificate() {
        return giftCertificate;
    }

    public void setGiftCertificate(GiftCertificateModel giftCertificate) {
        this.giftCertificate = giftCertificate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        OrderItemModel that = (OrderItemModel) o;

        if (quantity != that.quantity) return false;
        if (order != null ? !order.equals(that.order) : that.order != null) return false;
        return giftCertificate != null ? giftCertificate.equals(that.giftCertificate) : that.giftCertificate == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + quantity;
        result = 31 * result + (order != null ? order.hashCode() : 0);
        result = 31 * result + (giftCertificate != null ? giftCertificate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OrderItemModel{");
        sb.append("quantity=").append(quantity);
        sb.append(", orderModel=").append(order);
        sb.append(", giftCertificateModel=").append(giftCertificate);
        sb.append('}');
        return sb.toString();
    }
}
