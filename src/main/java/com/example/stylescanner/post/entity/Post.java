package com.example.stylescanner.post.entity;

import com.example.stylescanner.comment.entity.Comment;
import com.example.stylescanner.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

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

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Comment> comments;


    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;


    @Builder
    public Post(String feedUrl, String content, LocalDateTime createdAt, User user, List<Comment> comments) {
        this.feedUrl = feedUrl;
        this.content = content;
        this.createdAt = createdAt;
        this.user = user;
        this.comments = comments;
    }
}
