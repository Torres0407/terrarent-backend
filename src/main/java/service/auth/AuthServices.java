package service.auth;

import lombok.RequiredArgsConstructor;
import model.constants.Role;
import model.entity.User;
import model.request.auth.*;
import model.response.AuthenticationResponse;
//import org.springframework.context.support.BeanDefinitionDsl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import repository.UserRepository;

import java.time.Instant;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServices {

    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public void register(RegisterRequest req) {
        if (repo.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        String code = String.valueOf(100000 + new Random().nextInt(900000));

        User user = User.builder()
                .fullName(req.getFullName())
                .email(req.getEmail())
                .password(encoder.encode(req.getPassword()))
                .role(Role.valueOf(req.getRole().toUpperCase())) // use your Role enum
                .verified(false)
                .verificationCode(code) // only if field exists
                .build();

        repo.save(user);

    }

    public AuthenticationResponse authenticate(AuthenticationRequest req) {
        User user = repo.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!encoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return buildResponse(user);
    }

    public void verifyEmail(VerifyEmailRequest req) {
        User user = repo.findByEmail(req.getEmail()).orElseThrow();
        if (!user.getVerificationCode().equals(req.getCode())) {
            throw new RuntimeException("Invalid code");
        }
        user.setVerified(true);
        user.setVerificationCode(null);
        repo.save(user);
    }

    public void forgotPassword(ForgetPasswordRequest req) {
        User user = repo.findByEmail(req.getEmail()).orElseThrow();
        user.setResetToken(UUID.randomUUID().toString());
        user.setResetTokenExpiry(Instant.now().plusSeconds(900));
        repo.save(user);
    }

    public void resetPassword(ResetPasswordRequest req) {
        User user = repo.findByEmail(jwt.extractEmail(req.getToken())).orElseThrow();
        user.setPassword(encoder.encode(req.getNewPassword()));
        user.setResetToken(null);
        repo.save(user);
    }

    private AuthenticationResponse buildResponse(User user) {
        return AuthenticationResponse.builder()
                .accessToken(jwt.generateAccessToken(user))
                .refreshToken(jwt.generateRefreshToken(user))
                .user(AuthenticationResponse.UserResponse.builder()
                        .id(user.getId())
                        .fullName(user.getFullName())
                        .email(user.getEmail())
                        .role(user.getRole())
                        .verified(user.isVerified())
                        .build())
                .build();
    }
}
