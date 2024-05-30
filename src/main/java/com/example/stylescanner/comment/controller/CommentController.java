package com.example.stylescanner.comment.controller;

import com.example.stylescanner.comment.api.CommentApi;
import com.example.stylescanner.comment.dto.CommentCreateDto;
import com.example.stylescanner.comment.dto.CommentDto;
import com.example.stylescanner.comment.dto.CommentUpdateDto;
import com.example.stylescanner.comment.dto.MyCommentResponseDto;
import com.example.stylescanner.comment.service.CommentService;
import com.example.stylescanner.error.StateResponse;
import com.example.stylescanner.jwt.provider.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController implements CommentApi {


    private final CommentService commentService;
    private final JwtProvider jwtProvider;

    @Override
    public List<CommentDto> list(@PathVariable Integer postId) {
        return commentService.list(postId);
    }

    @Override
    public ResponseEntity<StateResponse> create(@RequestBody CommentCreateDto commentCreateDto, HttpServletRequest request){
        String currentUserEmail = jwtProvider.getAccount(jwtProvider.resolveToken(request).substring(7));
        return commentService.create(commentCreateDto, currentUserEmail);
    }

    @Override
    public ResponseEntity<StateResponse> delete(@PathVariable Integer commentId, HttpServletRequest request){
        String currentUserEmail = jwtProvider.getAccount(jwtProvider.resolveToken(request).substring(7));
        return commentService.delete(commentId, currentUserEmail);
    }

    @Override
    public List<MyCommentResponseDto> listMe(HttpServletRequest request) {
        String currentUserEmail = jwtProvider.getAccount(jwtProvider.resolveToken(request).substring(7));
        return commentService.listMe(currentUserEmail);
    }

    @Override
    public ResponseEntity<StateResponse> update(Integer commentId, CommentUpdateDto commentUpdateDto) {
        return commentService.update(commentId, commentUpdateDto);
    }


}