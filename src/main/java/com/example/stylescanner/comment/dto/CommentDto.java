package com.example.stylescanner.comment.dto;

import com.example.stylescanner.comment.entity.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private String content;
    private Long postId;
    private Long userId;
    private String displayName;
    private String profilePictureUrl;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @Builder
    public CommentDto(Long id, String content, Long postId, Long userId, String displayName, String profilePictureUrl, LocalDateTime createdAt) {
        this.id = id;
        this.content = content;
        this.postId = postId;
        this.userId = userId;
        this.displayName = displayName;
        this.profilePictureUrl = profilePictureUrl;
        this.createdAt = createdAt;
    }

    public static CommentDto fromEntity(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .postId(comment.getPost().getId())
                .userId(comment.getUser().getId())
                .displayName(comment.getUser().getDisplayName())
                .profilePictureUrl(comment.getUser().getProfilePictureUrl())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}