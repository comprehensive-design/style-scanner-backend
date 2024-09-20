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

    @Column(nullable = false) //feedUrl -> feedCode로 수정 , 해당 코드로 이미지 찾는 API 호출할때 사용해서 이미지 불러오기
    private String feedCode;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Comment> comments;


    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String username;

    @Builder
    public Post(String feedCode, String content, LocalDateTime createdAt, User user, List<Comment> comments, String username) {
        this.feedCode = feedCode;
        this.content = content;
        this.createdAt = createdAt;
        this.user = user;
        this.comments = comments;
        this.username = username;
    }
}
