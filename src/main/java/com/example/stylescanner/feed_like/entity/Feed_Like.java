package com.example.stylescanner.feed_like.entity;

import com.example.stylescanner.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.net.URL;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Feed_Like {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer like_id;

    @Column(nullable = false)
    private URL feed_url;

    @CreatedDate
    private LocalDateTime created_at;

    @ManyToOne
    private User user_id;
}
