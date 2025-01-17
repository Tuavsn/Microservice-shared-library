package com.microservice.shared_library.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
@Setter
public class ApiError {
    private HttpStatus status;
    private String message;
}
