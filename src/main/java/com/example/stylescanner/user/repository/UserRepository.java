package com.example.stylescanner.user.repository;

import com.example.stylescanner.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    //중복 가입 확인
    Optional<User> findByEmail(String email);

}
