package com.example.stylescanner.comment.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class MyCommentResponseDto {
    private String feedCode;
    private String feedTitle;
    private CommentDto comment;
}
