package com.example.stylescanner.feed_like.entity;

import com.example.stylescanner.user.entity.User;
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
@Table(name = "feed_like")
public class Feed_Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String feedUrl;

    @CreatedDate
    private LocalDateTime createdAt;

    @ManyToOne
    private User user;

    @Builder
    public Feed_Like(String feedUrl, User user) {
        this.feedUrl = feedUrl;
        this.user = user;
    }

}
