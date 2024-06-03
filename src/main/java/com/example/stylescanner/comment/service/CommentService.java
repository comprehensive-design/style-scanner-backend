package com.example.stylescanner.comment.service;

import com.example.stylescanner.comment.dto.CommentCreateDto;
import com.example.stylescanner.comment.dto.CommentDto;
import com.example.stylescanner.comment.dto.CommentUpdateDto;
import com.example.stylescanner.comment.dto.MyCommentResponseDto;
import com.example.stylescanner.comment.entity.Comment;
import com.example.stylescanner.comment.repository.CommentRepository;
import com.example.stylescanner.error.StateResponse;
import com.example.stylescanner.notification.entity.Notification;
import com.example.stylescanner.notification.repository.NotificationRepository;
import com.example.stylescanner.post.entity.Post;
import com.example.stylescanner.post.repository.PostRepository;
import com.example.stylescanner.user.entity.User;
import com.example.stylescanner.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final NotificationRepository notificationRepository;


    public List<CommentDto> list(Integer postId){
        return commentRepository.findByPostId(postId).stream().map(CommentDto::fromEntity).collect(Collectors.toList());
    }


    public ResponseEntity<StateResponse> create( CommentCreateDto commentCreateDto, String currentUserEmail) {
        User user = userRepository.findByEmail(currentUserEmail).orElseThrow(() -> new IllegalArgumentException("not found user"));
        Post post = postRepository.findById(commentCreateDto.getPostId()).orElseThrow(() -> new IllegalArgumentException("not found user"));

        Comment comment = Comment.builder()
                .content(commentCreateDto.getContent())
                .post(post)
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();

        commentRepository.save(comment);

        // 알림 생성 로직
        Notification notification = Notification.builder()
                .createdAt(LocalDateTime.now())
                .receiver(post.getUser())
                .comment(comment)
                .build();

        notificationRepository.save(notification);

        return ResponseEntity.ok(StateResponse.builder().code("SUCCESS").message("댓글을 성공적으로 생성했습니다.").build());
    }

    @Transactional
    public ResponseEntity<StateResponse> delete(Integer commentId, String currentUserEmail){
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("not found comment"));
        String writerEmail = comment.getUser().getEmail();
        if(!writerEmail.equals(currentUserEmail)) throw new IllegalArgumentException("댓글 작성자만 글을 삭제할 수 있습니다.");

        commentRepository.delete(comment);
        return ResponseEntity.ok(StateResponse.builder().code("SUCCESS").message("댓글을 성공적으로 삭제했습니다.").build());
    };

    public List<MyCommentResponseDto> listMe(String currentUserEmail) {
        User user = userRepository.findByEmail(currentUserEmail).orElseThrow(() -> new IllegalArgumentException("not found user"));
        List<CommentDto> commentDtoList = commentRepository.findByUser(user).stream().map(CommentDto::fromEntity).collect(Collectors.toList());


        List<MyCommentResponseDto> MyComments = new ArrayList<>();
        for(CommentDto commentDto : commentDtoList){
            MyCommentResponseDto myCommentResponseDto = new MyCommentResponseDto();
            int post_id = Math.toIntExact(commentDto.getPostId());
            Post post = postRepository.findById(post_id).orElseThrow(() -> new IllegalArgumentException("not found user"));

            myCommentResponseDto.setFeedUrl(post.getFeedUrl());
            myCommentResponseDto.setFeedTitle(post.getContent());

            myCommentResponseDto.setComment(commentDto);
            MyComments.add(myCommentResponseDto);
        }
        return MyComments;
    }

    public ResponseEntity<StateResponse> update(Integer commentId , CommentUpdateDto commentUpdateDto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("not found comment"));

        comment.setContent(commentUpdateDto.getComment());
        commentRepository.save(comment);

        return ResponseEntity.ok(StateResponse.builder().code("SUCCESS").message("댓글을 성공적으로 수정했습니다.").build());

    }
}