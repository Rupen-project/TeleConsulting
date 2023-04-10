package com.had.teleconsulting.teleconsulting.Payloads;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {

    Long userID;
    String userFirstName;
    String userLastName;
    String userEmail;
    String userMobileNumber;
    String userPassword;
}
