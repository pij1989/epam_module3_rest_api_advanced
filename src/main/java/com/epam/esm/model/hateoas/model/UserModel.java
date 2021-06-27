package com.epam.esm.model.hateoas.model;

import com.epam.esm.model.entity.Role;
import com.epam.esm.model.entity.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.hateoas.RepresentationModel;

import java.util.HashSet;
import java.util.Set;

public class UserModel extends RepresentationModel<UserModel> {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    @JsonIgnoreProperties(value = "id")
    private Role role;
    @JsonIgnoreProperties(value = "id")
    private Status status;
    private Set<OrderModel> orders = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Set<OrderModel> getOrders() {
        return orders;
    }

    public void setOrders(Set<OrderModel> orders) {
        this.orders = orders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        UserModel userModel = (UserModel) o;

        if (id != null ? !id.equals(userModel.id) : userModel.id != null) return false;
        if (firstName != null ? !firstName.equals(userModel.firstName) : userModel.firstName != null) return false;
        if (lastName != null ? !lastName.equals(userModel.lastName) : userModel.lastName != null) return false;
        if (username != null ? !username.equals(userModel.username) : userModel.username != null) return false;
        if (email != null ? !email.equals(userModel.email) : userModel.email != null) return false;
        if (password != null ? !password.equals(userModel.password) : userModel.password != null) return false;
        if (role != null ? !role.equals(userModel.role) : userModel.role != null) return false;
        if (status != null ? !status.equals(userModel.status) : userModel.status != null) return false;
        return orders != null ? orders.equals(userModel.orders) : userModel.orders == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (orders != null ? orders.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserModel{");
        sb.append("id=").append(id);
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", username='").append(username).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", role=").append(role);
        sb.append(", status=").append(status);
        sb.append(", orderModels=").append(orders);
        sb.append('}');
        return sb.toString();
    }
}
