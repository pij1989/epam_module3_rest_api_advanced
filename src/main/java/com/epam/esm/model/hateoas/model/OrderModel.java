package com.epam.esm.model.hateoas.model;

import com.epam.esm.model.entity.User;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class OrderModel extends RepresentationModel<OrderModel> {
    private Long id;
    private BigDecimal cost;
    private LocalDateTime createDate;
    private User user;
    Set<OrderItemModel> orderItems = new HashSet<>();

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

    public Set<OrderItemModel> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Set<OrderItemModel> orderItems) {
        this.orderItems = orderItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        OrderModel that = (OrderModel) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (cost != null ? !cost.equals(that.cost) : that.cost != null) return false;
        if (createDate != null ? !createDate.equals(that.createDate) : that.createDate != null) return false;
        if (user != null ? !user.equals(that.user) : that.user != null) return false;
        return orderItems != null ? orderItems.equals(that.orderItems) : that.orderItems == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (cost != null ? cost.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (orderItems != null ? orderItems.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OrderModel{");
        sb.append("id=").append(id);
        sb.append(", cost=").append(cost);
        sb.append(", createDate=").append(createDate);
        sb.append(", user=").append(user);
        sb.append(", orderItemModels=").append(orderItems);
        sb.append('}');
        return sb.toString();
    }
}
