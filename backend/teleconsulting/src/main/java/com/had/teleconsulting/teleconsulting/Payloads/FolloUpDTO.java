package com.had.teleconsulting.teleconsulting.Payloads;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
public class FolloUpDTO {

    long followUpID;
    Date followUpDate;
}
