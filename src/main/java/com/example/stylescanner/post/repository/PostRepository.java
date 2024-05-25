package com.example.stylescanner.post.repository;

import com.example.stylescanner.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {
}
