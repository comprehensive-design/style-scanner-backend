package com.example.stylescanner.comment.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentCreateDto {
    private Integer postId;
    private Integer userId;
    private String content;

    @Builder
    public CommentCreateDto(Integer postId, Integer userId, String content) {
        this.postId = postId;
        this.userId = userId;
        this.content = content;
    }
}