/* (C)2021 */
package com.jdk.jdk.basic;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PostPersist;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Table(name = "test_entity")
@Entity(name = "User")
@Getter
@Setter
public class TestEntity implements Serializable {
    private static final long serialVersionUID = -217926599704323443L;

    private Long id;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    public Long getId() {
        return id;
    }

    @PostPersist
    public void postPersist() {}

    @PrePersist
    public void prePersist() {}

    @PreUpdate
    public void preUpdate() {}

    @PreRemove
    public void preRemove() {}
}
