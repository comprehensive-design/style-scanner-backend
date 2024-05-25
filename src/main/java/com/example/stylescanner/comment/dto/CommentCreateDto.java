package com.example.stylescanner.comment.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentCreateDto {
    private Integer postId;
    private String content;

    @Builder
    public CommentCreateDto(Integer postId, String content) {
        this.postId = postId;
        this.content = content;
    }
}