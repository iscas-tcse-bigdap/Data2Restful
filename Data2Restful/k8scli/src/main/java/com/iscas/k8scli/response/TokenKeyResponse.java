package com.iscas.k8scli.response;

import lombok.Data;

@Data
public class TokenKeyResponse {

    private String alg;
    private String value;

    public TokenKeyResponse(String newAlg, String newValue) {
        this.alg = newAlg;
        this.value = newValue;
    }

}
