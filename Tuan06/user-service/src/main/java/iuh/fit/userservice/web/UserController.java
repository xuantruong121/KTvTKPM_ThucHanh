package iuh.fit.userservice.web;

import iuh.fit.userservice.domain.Role;
import iuh.fit.userservice.dto.AuthDtos;
import iuh.fit.userservice.repo.UserRepository;
import iuh.fit.userservice.security.JwtService;
import iuh.fit.userservice.service.UserAuthService;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserAuthService userAuthService;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @PostMapping("/register")
    public AuthDtos.UserResponse register(@Valid @RequestBody AuthDtos.RegisterRequest req) {
        return userAuthService.register(req);
    }

    @PostMapping("/login")
    public AuthDtos.LoginResponse login(@Valid @RequestBody AuthDtos.LoginRequest req) {
        try {
            return userAuthService.login(req);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/users")
    public List<AuthDtos.UserResponse> listUsers(
            @RequestHeader(value = "X-User-Role", required = false) String role
    ) {
        if (!Role.ADMIN.name().equals(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "ADMIN only");
        }
        return userRepository.findAll()
                .stream()
                .map(u -> new AuthDtos.UserResponse(u.getId(), u.getUsername(), u.getRole()))
                .toList();
    }

    @GetMapping("/validate")
    public AuthDtos.ValidateResponse validate(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization
    ) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing token");
        }
        String token = authorization.substring("Bearer ".length()).trim();
        Claims c;
        try {
            c = jwtService.parse(token);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }
        Long id = Long.valueOf(c.getSubject());
        String username = String.valueOf(c.get("username"));
        Role role = Role.valueOf(String.valueOf(c.get("role")));
        return new AuthDtos.ValidateResponse(id, username, role);
    }
}

