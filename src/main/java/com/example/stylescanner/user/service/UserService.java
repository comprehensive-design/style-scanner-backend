package com.example.stylescanner.user.service;

import com.example.stylescanner.user.dto.UserRegisterRequestDto;
import com.example.stylescanner.user.entity.User;
import com.example.stylescanner.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService  {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> list(){
        return userRepository.findAll();
    }

    public User read(Long id){
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public void signup(UserRegisterRequestDto requestDto) {
        String email = requestDto.getEmail();
        String password = passwordEncoder.encode(requestDto.getPassword());

        //회원 중복 체크
        Optional <User> chekcUserEmail = userRepository.findByEmail(email);
        if(chekcUserEmail.isPresent()) {
            throw new IllegalArgumentException("이미 등록된 사용자입니다.");
        }

        User user = User.builder()
                .email(requestDto.getEmail())
                .password(password)
                .displayName(requestDto.getDisplayName())
                .birthdate(requestDto.getBirthdate())
                .gender(requestDto.getGender())
                .createdAt(LocalDateTime.now())
                .profilePictureUrl(null)
                .bio(null).build();

        userRepository.save(user);
    }
}
