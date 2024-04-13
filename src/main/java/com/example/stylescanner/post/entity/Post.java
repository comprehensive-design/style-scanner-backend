package com.example.stylescanner.post.entity;

import com.example.stylescanner.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String feedUrl;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    private User user;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;


    @Builder
    public Post(String feedUrl, String content, User user) {
        this.feedUrl = feedUrl;
        this.content = content;
        this.user = user;
    }
}
