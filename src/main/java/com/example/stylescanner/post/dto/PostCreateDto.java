package com.example.stylescanner.post.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostCreateDto {
    private String feedUrl;
    private String content;

    @Builder
    public PostCreateDto(String feedUrl, String content) {
        this.feedUrl = feedUrl;
        this.content = content;
    }
}