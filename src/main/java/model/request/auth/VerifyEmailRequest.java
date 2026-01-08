package model.request.auth;

import lombok.Data;

@Data
public class VerifyEmailRequest {
    private String email;
    private String code;
}
