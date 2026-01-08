package controller.auth;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import model.request.auth.*;
import model.response.AuthenticationResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.auth.AuthServices;

@Valid
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServices service;

    @PostMapping("/register")
    public String register(@Valid @RequestBody RegisterRequest req) {
        service.register(req);
        return "User registered successfully!";
    }

    @PostMapping("/authenticate")
    public AuthenticationResponse authenticate(@RequestBody AuthenticationRequest req) {
        return service.authenticate(req);
    }

    @PostMapping("/verify")
    public void verify(@RequestBody VerifyEmailRequest req) {
        service.verifyEmail(req);
    }

    @PostMapping("/forgot-password")
    public void forgot(@RequestBody ForgetPasswordRequest req) {
        service.forgotPassword(req);
    }

    @PostMapping("/reset-password")
    public void reset(@RequestBody ResetPasswordRequest req) {
        service.resetPassword(req);
    }
}