package model.response;
import lombok.Builder;
import lombok.Data;
import model.constants.Role;

import java.util.UUID;

@Data
@Builder
public class AuthenticationResponse {
    private String accessToken;
    private String refreshToken;
    private UserResponse user;

    @Data
    @Builder
    public static class UserResponse {
        private UUID id;
        private String fullName;
        private String email;
        private Role role;
        private boolean verified;
    }
}
