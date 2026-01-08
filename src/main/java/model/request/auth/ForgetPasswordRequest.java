package model.request.auth;

import lombok.Data;

@Data
public class ForgetPasswordRequest {
    private String email;
}
