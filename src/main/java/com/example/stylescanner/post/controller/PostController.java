package com.example.stylescanner.post.controller;

import com.example.stylescanner.error.StateResponse;
import com.example.stylescanner.jwt.provider.JwtProvider;
import com.example.stylescanner.post.api.PostApi;
import com.example.stylescanner.post.dto.PostCreateDto;
import com.example.stylescanner.post.dto.PostDto;
import com.example.stylescanner.post.dto.PostUpdateDto;
import com.example.stylescanner.post.entity.Post;
import com.example.stylescanner.post.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class PostController implements PostApi {

    private final PostService postService;
    private final JwtProvider jwtProvider;

    @Override
    public List<PostDto> list() {
        List<Post> posts = postService.list();
        return posts.stream()
                .map(PostDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public PostDto read(Integer postId) {
        Post post = postService.read(postId);
        return PostDto.fromEntity(post);
    }

    @Override
    public ResponseEntity<StateResponse> create(@RequestBody PostCreateDto postCreateDto, HttpServletRequest request){
        String currentUserEmail = jwtProvider.getAccount(jwtProvider.resolveToken(request).substring(7));
        return postService.create(postCreateDto, currentUserEmail);
    }

    @Override
    public ResponseEntity<StateResponse> delete(@PathVariable Integer postId, HttpServletRequest request){
        String currentUserEmail = jwtProvider.getAccount(jwtProvider.resolveToken(request).substring(7));
        return postService.delete(postId, currentUserEmail);
    }

    @Override
    public List<PostDto> me(HttpServletRequest request) {
        String currentUserEmail = jwtProvider.getAccount(jwtProvider.resolveToken(request).substring(7));
        List<Post> posts = postService.me(currentUserEmail);
        return posts.stream()
                .map(PostDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<StateResponse> update(Integer postId, PostUpdateDto postUpdateDto) {
        return postService.update(postId, postUpdateDto);
    }
}