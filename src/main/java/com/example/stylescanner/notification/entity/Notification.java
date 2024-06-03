package com.example.stylescanner.notification.entity;

import com.example.stylescanner.comment.entity.Comment;
import com.example.stylescanner.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(columnDefinition = "boolean default false")
    private boolean checked;

    @ManyToOne
    @JsonIgnore
    private User receiver;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Comment comment;

    @Builder
    public Notification(LocalDateTime createdAt, User receiver, Comment comment) {
        this.createdAt = createdAt;
        this.receiver = receiver;
        this.comment = comment;
    }
}