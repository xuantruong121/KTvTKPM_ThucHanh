package iuh.fit.userservice.service;

import iuh.fit.userservice.domain.Role;
import iuh.fit.userservice.domain.User;
import iuh.fit.userservice.dto.AuthDtos;
import iuh.fit.userservice.repo.UserRepository;
import iuh.fit.userservice.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public AuthDtos.UserResponse register(AuthDtos.RegisterRequest req) {
        if (userRepository.existsByUsername(req.username())) {
            throw new IllegalArgumentException("Username already exists");
        }
        Role role = req.role() == null ? Role.USER : req.role();

        User u = new User();
        u.setUsername(req.username());
        u.setPasswordHash(passwordEncoder.encode(req.password()));
        u.setRole(role);

        User saved = userRepository.save(u);
        return new AuthDtos.UserResponse(saved.getId(), saved.getUsername(), saved.getRole());
    }

    @Transactional(readOnly = true)
    public AuthDtos.LoginResponse login(AuthDtos.LoginRequest req) {
        User u = userRepository.findByUsername(req.username())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!passwordEncoder.matches(req.password(), u.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        String token = jwtService.generateToken(u.getId(), u.getRole(), u.getUsername());
        return new AuthDtos.LoginResponse(token, new AuthDtos.UserResponse(u.getId(), u.getUsername(), u.getRole()));
    }
}

