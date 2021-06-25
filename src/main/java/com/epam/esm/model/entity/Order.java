package com.epam.esm.model.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@javax.persistence.Entity(name="orders")
public class Order implements Entity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal cost;
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    Set<OrderGiftCertificate> orderGiftCertificates = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<OrderGiftCertificate> getOrderGiftCertificates() {
        return orderGiftCertificates;
    }

    public void setOrderGiftCertificates(Set<OrderGiftCertificate> orderGiftCertificates) {
        this.orderGiftCertificates = orderGiftCertificates;
    }

    public void addOrderGiftCertificate(OrderGiftCertificate orderGiftCertificate){
        orderGiftCertificates.add(orderGiftCertificate);
        orderGiftCertificate.setOrder(this);
    }

    public void removeOrderGiftCertificate(OrderGiftCertificate orderGiftCertificate){
        orderGiftCertificates.remove(orderGiftCertificate);
        orderGiftCertificate.setOrder(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (id != null ? !id.equals(order.id) : order.id != null) return false;
        if (cost != null ? !cost.equals(order.cost) : order.cost != null) return false;
        if (createDate != null ? !createDate.equals(order.createDate) : order.createDate != null) return false;
        if (user != null ? !user.equals(order.user) : order.user != null) return false;
        return orderGiftCertificates != null ? orderGiftCertificates.equals(order.orderGiftCertificates) : order.orderGiftCertificates == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (cost != null ? cost.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (orderGiftCertificates != null ? orderGiftCertificates.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Order{");
        sb.append("id=").append(id);
        sb.append(", cost=").append(cost);
        sb.append(", createDate=").append(createDate);
        sb.append(", user=").append(user);
        sb.append(", orderGiftCertificates=").append(orderGiftCertificates);
        sb.append('}');
        return sb.toString();
    }
}
