package com.epam.esm.model.entity;

import com.epam.esm.model.entity.type.PostgreSqlEnumType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@javax.persistence.Entity(name = "statuses")
@TypeDef(name = "enum_postgresql",typeClass = PostgreSqlEnumType.class)
public class Status implements Entity {

    public enum StatusType {
        ACTIVE, BLOCKED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    @Type(type = "enum_postgresql")
    private StatusType statusType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StatusType getStatusType() {
        return statusType;
    }

    public void setStatusType(StatusType statusType) {
        this.statusType = statusType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Status status = (Status) o;

        if (id != null ? !id.equals(status.id) : status.id != null) return false;
        return statusType == status.statusType;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (statusType != null ? statusType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Status{");
        sb.append("id=").append(id);
        sb.append(", statusType=").append(statusType);
        sb.append('}');
        return sb.toString();
    }
}
