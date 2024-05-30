package com.example.stylescanner.comment.api;

import com.example.stylescanner.comment.dto.CommentCreateDto;
import com.example.stylescanner.comment.dto.CommentDto;
import com.example.stylescanner.comment.dto.CommentUpdateDto;
import com.example.stylescanner.comment.dto.MyCommentResponseDto;
import com.example.stylescanner.error.StateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/comment")
@Tag(name = "Comment", description = "커뮤니티 댓글 API")
public interface CommentApi {

    @GetMapping("/{postId}")
    @Operation(summary = "커뮤니티 글에 대한 댓글목록 조회 메서드", description = "커뮤니티 글의 댓글을 조회하기 위한 메서드입니다.")
    List<CommentDto> list(@PathVariable Integer postId);

    @PostMapping("/create")
    @Operation(summary = "커뮤니티 댓글 생성 메서드", description = "로그인한 사용자가 커뮤니티 댓글을 작성하기 위한 메서드입니다.")
    ResponseEntity<StateResponse> create(@RequestBody CommentCreateDto commentCreateDto, HttpServletRequest request);

    @DeleteMapping("/delete/{commentId}")
    @Operation(summary = "커뮤니티 댓글 삭제 메서드", description = "로그인한 사용자가 자신이 작성한 커뮤니티 댓글을 삭제하기 위한 메서드입니다.")
    ResponseEntity<StateResponse> delete(@PathVariable Integer commentId, HttpServletRequest request);

    @GetMapping("/me")
    @Operation(summary = "사용자 커뮤니티 댓글 조회 메서드", description = "로그인한 사용자가 자신이 작성한 커뮤니티 댓글을 조회하기 위한 메서드입니다. ")
    List<MyCommentResponseDto> listMe(HttpServletRequest request);

    @PostMapping("/update/{commentId}")
    @Operation(summary = "사용자 커뮤니티 댓글 수정 메서드", description = "로그인한 사용자가 자신이 작성한 커뮤니티 댓글을 수정하기 위한 메서드입니다.")
    ResponseEntity<StateResponse> update(@PathVariable Integer commentId , @RequestBody CommentUpdateDto commentUpdateDto);
}
