package com.example.stylescanner.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user")
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
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

    @Builder
    public User(String email, String password, String displayName, LocalDate birthdate, Byte gender,
                LocalDateTime createdAt, String profilePictureUrl, String bio) {
        this.email = email;
        this.password = password;
        this.displayName = displayName;
        this.birthdate = birthdate;
        this.gender = gender;
        this.createdAt = createdAt;
        this.profilePictureUrl = profilePictureUrl;
        this.bio = bio;
    }

    public void passwordEncode(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(this.password);
    }
}
