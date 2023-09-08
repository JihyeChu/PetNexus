package com.sparta.petnexus.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {

    private String message;
    private Integer statusCode;

    public ApiResponse(String massage, Integer statusCode) {
        this.message = massage;
        this.statusCode = statusCode;
    }
}
