package com.iscas.k8scli.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenResponse {

    private String accessToken;
    private String tokenType;
    private int expiresIn;
    private String refreshToken;
    @JsonIgnore
    private long grant;
}
