package com.example.stylescanner.comment.repository;

import com.example.stylescanner.comment.entity.Comment;
import com.example.stylescanner.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByPostId(Integer postId);

    List<Comment> findByUser(User user);
}
