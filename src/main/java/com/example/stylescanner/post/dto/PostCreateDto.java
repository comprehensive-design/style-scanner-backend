package com.example.stylescanner.post.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostCreateDto {
    private String feedCode;
    private String content;
    private String username;

    @Builder
    public PostCreateDto(String feedCode, String content, String username) {
        this.feedCode = feedCode;
        this.content = content;
        this.username = username;
    }
}