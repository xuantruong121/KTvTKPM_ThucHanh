package iuh.fit.userservice.dto;

import iuh.fit.userservice.domain.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthDtos {
    public record RegisterRequest(
            @NotBlank @Size(min = 3, max = 80) String username,
            @NotBlank @Size(min = 4, max = 80) String password,
            Role role
    ) {}

    public record LoginRequest(
            @NotBlank String username,
            @NotBlank String password
    ) {}

    public record UserResponse(
            Long id,
            String username,
            Role role
    ) {}

    public record LoginResponse(
            String accessToken,
            UserResponse user
    ) {}

    public record ValidateResponse(
            Long id,
            String username,
            Role role
    ) {}
}

