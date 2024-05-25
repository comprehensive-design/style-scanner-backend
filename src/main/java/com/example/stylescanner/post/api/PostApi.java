package com.example.stylescanner.post.api;

import com.example.stylescanner.error.StateResponse;
import com.example.stylescanner.post.dto.PostCreateDto;
import com.example.stylescanner.post.dto.PostDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/post")
@Tag(name = "Post", description = "커뮤니티 글 API")
public interface PostApi {
    @GetMapping("")
    @Operation(summary = "커뮤니티 글 목록 조회 메서드", description = "커뮤니티 글 목록을 조회하기 위한 메서드입니다.")
    List<PostDto> list();

    @GetMapping("/{postId}")
    @Operation(summary = "커뮤니티 글 상세 조회 메서드", description = "커뮤니티 글 상세 내용을 조회하기 위한 메서드입니다.")
    PostDto read(@PathVariable Integer postId);

    @PostMapping("/create")
    @Operation(summary = "커뮤니티 글 생성 메서드", description = "로그인한 사용자가 커뮤니티 글을 생성하기 위한 메서드입니다.")
    ResponseEntity<StateResponse> create(@RequestBody PostCreateDto postCreateDto, HttpServletRequest request);

    @DeleteMapping("/delete/{postId}")
    @Operation(summary = "커뮤니티 글 삭제 메서드", description = "로그인한 사용자가 커뮤니티 글을 삭제하기 위한 메서드입니다.")
    ResponseEntity<StateResponse> delete(@PathVariable Integer postId, HttpServletRequest request);
}
