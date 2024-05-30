package com.example.stylescanner.post.repository;

import com.example.stylescanner.post.entity.Post;
import com.example.stylescanner.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findAllByUser(User user);
}
