package com.ama.imanuel.connectwithmebe.shared;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@MappedSuperclass
public abstract class BaseEntity {
    @Column(updatable = false, name = "created_at")
    @CreationTimestamp
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    @JsonProperty("deleted_at")
    private LocalDateTime deletedAt;

    @Column(updatable = false)
    @JsonProperty("created_bys")
    private String createdBy;

    @JsonProperty("updated_by")
    private String updatedBy;

    @JsonProperty("deleted_by")
    private String deletedBy;
}

