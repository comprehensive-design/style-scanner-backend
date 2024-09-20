package com.example.stylescanner.post.dto;

import com.example.stylescanner.comment.dto.CommentDto;
import com.example.stylescanner.post.entity.Post;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class PostDto {
    private Long id;
    private String feedCode;
    private String content;
    private LocalDateTime createdAt;
    private Long userId;
    private String displayName;
    private String profilePictureUrl;
    private List<CommentDto> comments;
    private String username;

    @Builder
    public PostDto(Long id, String feedCode, String content, LocalDateTime createdAt, Long userId, String displayName, String profilePictureUrl, List<CommentDto> comments, String username) {
        this.id = id;
        this.feedCode = feedCode;
        this.content = content;
        this.createdAt = createdAt;
        this.userId = userId;
        this.displayName = displayName;
        this.profilePictureUrl = profilePictureUrl;
        this.comments = comments;
        this.username = username;
    }

    public static PostDto fromEntity(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .feedCode(post.getFeedCode())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .userId(post.getUser().getId())
                .displayName(post.getUser().getDisplayName())
                .profilePictureUrl(post.getUser().getProfilePictureUrl())
                .comments(post.getComments().stream().map(CommentDto::fromEntity).collect(Collectors.toList()))
                .username(post.getUsername())
                .build();
    }
}
