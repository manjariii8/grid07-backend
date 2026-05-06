package com.grid07backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long authorId;
    private String authorType;

    @Column(length = 1000)
    private String content;

    private LocalDateTime createdAt = LocalDateTime.now();
}
