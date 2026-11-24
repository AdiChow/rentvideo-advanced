package com.example.rentvideo.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RentalResponse {

    private Long rentalId;
    private Long videoId;
    private String videoTitle;
    private LocalDateTime rentedAt;
    private LocalDateTime returnedAt;
}