package com.had.teleconsulting.teleconsulting.Bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.security.PrivateKey;


//this class is for handling the exceptions and giving this entity as the response whenever the error occur
@Data // this will have all getter and setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorMessage {

    private HttpStatus status;
    private String message;
}
