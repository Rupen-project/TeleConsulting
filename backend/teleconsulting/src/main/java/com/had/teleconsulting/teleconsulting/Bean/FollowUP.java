package com.had.teleconsulting.teleconsulting.Bean;

import lombok.*;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "FollowUP")
public class FollowUP {
    // appointment id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false,name = "followUpID")
    Long followUpID;

    @Column(nullable = false,name = "followUpDate")
    Date followUpDate;


}
