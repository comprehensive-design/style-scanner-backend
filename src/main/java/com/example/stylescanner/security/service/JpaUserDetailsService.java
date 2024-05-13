package com.example.stylescanner.security.service;

import com.example.stylescanner.security.dto.CustomUserDetails;
import com.example.stylescanner.user.entity.User;
import com.example.stylescanner.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(()
                -> new UsernameNotFoundException(email + "에 해당하는 회원을 찾을 수 없습니다."));
        //존재하면 UserDetails 객체 만들어서 리턴
        return new CustomUserDetails(user);
    }
}
