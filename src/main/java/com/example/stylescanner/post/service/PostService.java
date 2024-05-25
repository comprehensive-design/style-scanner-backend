package com.example.stylescanner.post.service;

import com.example.stylescanner.error.StateResponse;
import com.example.stylescanner.post.dto.PostCreateDto;
import com.example.stylescanner.post.entity.Post;
import com.example.stylescanner.post.repository.PostRepository;
import com.example.stylescanner.user.entity.User;
import com.example.stylescanner.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;


    public List<Post> list(){
        return postRepository.findAll();
    }

    public Post read(Integer id){
        return postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 글이 없습니다. id= "+ id));
    }

    public ResponseEntity<StateResponse> create(PostCreateDto postCreateDto, String currentUserEmail) {
        User user = userRepository.findByEmail(currentUserEmail).orElseThrow(() -> new IllegalArgumentException("not found user"));

        Post post = Post.builder()
                .feedUrl(postCreateDto.getFeedUrl())
                .content(postCreateDto.getContent())
                .createdAt(LocalDateTime.now())
                .user(user)
                .build();

        postRepository.save(post);
        return ResponseEntity.ok(StateResponse.builder().code("SUCCESS").message("글을 성공적으로 생성했습니다.").build());
    }

    @Transactional
    public ResponseEntity<StateResponse> delete(Integer postId, String currentUserEmail){
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("not found post"));
        String writerEmail = post.getUser().getEmail();
        if(!writerEmail.equals(currentUserEmail)) throw new IllegalArgumentException("작성자만 글을 삭제할 수 있습니다.");

        postRepository.delete(post);
        return ResponseEntity.ok(StateResponse.builder().code("SUCCESS").message("글을 성공적으로 삭제했습니다.").build());
    };
}