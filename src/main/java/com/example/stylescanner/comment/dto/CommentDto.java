package com.example.stylescanner.comment.dto;

import com.example.stylescanner.comment.entity.Comment;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private String content;
    private Long postId;
    private Long userId;
    private String displayName;
    private String profilePictureUrl;

    @Builder
    public CommentDto(Long id, String content, Long postId, Long userId, String displayName, String profilePictureUrl) {
        this.id = id;
        this.content = content;
        this.postId = postId;
        this.userId = userId;
        this.displayName = displayName;
        this.profilePictureUrl = profilePictureUrl;
    }

    public static CommentDto fromEntity(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .postId(comment.getPost().getId())
                .userId(comment.getUser().getId())
                .displayName(comment.getUser().getDisplayName())
                .profilePictureUrl(comment.getUser().getProfilePictureUrl())
                .build();
    }
}