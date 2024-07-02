package com.microservice.shared_library.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public abstract class BaseEntityAudit extends BaseEntity implements Serializable {
    private String createdBy;
    private String updatedBy;

    @CreationTimestamp
    @Column(name = "create_at", updatable = false)
    protected Date createdAt;

    @UpdateTimestamp
    @Column(name = "update_at")
    protected Date updatedAt;

    @Column(nullable = false, columnDefinition = "boolean default false")
    protected boolean deleted = false;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) { return true; }
        if (!(obj instanceof BaseEntityAudit other)) { return false; }
        if(!super.equals(obj)) { return false; }
        return createdBy.equals(other.createdBy)
                && updatedBy.equals(other.updatedBy)
                && createdAt == other.createdAt
                && updatedAt == other.updatedAt;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), createdBy, updatedBy, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "BaseEntityAudit [" +
                "createdBy=" + createdBy +
                ", updatedBy=" + updatedBy +
                ", createdAt=" + createdAt + "]" +
                super.toString();
    }
}
