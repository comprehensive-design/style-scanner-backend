package com.example.stylescanner.user.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user") // This should match the table name in your database
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", updatable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Column(nullable = false)
    private LocalDate birthdate;

    @Column(nullable = false)
    private Byte gender;

    @Column(name = "profile_picture_url")
    private String profilePictureUrl;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;


    // If you are using Lombok @Builder, you should include all the fields in the builder method
    @Builder
    public User(String email, String password, String displayName, LocalDate birthdate, Byte gender,
                String profilePictureUrl, String bio, LocalDateTime createdAt) {
        this.email = email;
        this.password = password;
        this.displayName = displayName;
        this.birthdate = birthdate;
        this.gender = gender;
        this.profilePictureUrl = profilePictureUrl;
        this.bio = bio;
        this.createdAt = createdAt;
    }
}
