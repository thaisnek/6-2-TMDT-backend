package com.example.webtmdt.service.impl;

import com.example.webtmdt.configuration.security.CustomUserDetails;
import com.example.webtmdt.configuration.security.JwtTokenProvider;
import com.example.webtmdt.dto.request.LoginRequest;
import com.example.webtmdt.dto.request.RegisterRequest;
import com.example.webtmdt.dto.response.AuthResponse;
import com.example.webtmdt.entity.User;
import com.example.webtmdt.enums.UserRole;
import com.example.webtmdt.enums.UserStatus;
import com.example.webtmdt.repository.UserRepository;
import com.example.webtmdt.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUserName(request.getUserName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tên đăng nhập đã tồn tại!");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email đã được sử dụng!");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .userName(request.getUserName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.CUSTOMER)
                .status(UserStatus.ACTIVE)
                .build();

        userRepository.save(user);

        // Optional: Tự động Login sau khi đăng ký
        return login(new LoginRequest(request.getUserName(), request.getPassword()));
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUserName(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        return AuthResponse.builder()
                .accessToken(jwt)
                .tokenType("Bearer")
                .userId(userDetails.getUser().getId())
                .userName(userDetails.getUsername())
                .role(userDetails.getUser().getRole().name())
                .build();
    }
}
