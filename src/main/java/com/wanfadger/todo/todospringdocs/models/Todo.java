package com.wanfadger.todo.todospringdocs.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
public class Todo {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid" , strategy = "uuid")
    private String id;

    private String name;

    private LocalDateTime modifiedAt;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void onPersist(){
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
    }

    @PreUpdate
    void onUpdate(){
        this.modifiedAt = LocalDateTime.now();
    }

    public Todo(String id, String name, LocalDateTime modifiedAt, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.modifiedAt = modifiedAt;
        this.createdAt = createdAt;
    }
}
