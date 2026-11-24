package com.example.rentvideo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.rentvideo.dto.RentalResponse;
import com.example.rentvideo.service.RentalService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/videos")
@RequiredArgsConstructor
public class RentalController {

    private final RentalService rentalService;

    @PostMapping("/{videoId}/rent")
    public ResponseEntity<RentalResponse> rentVideo(
            @PathVariable Long videoId,
            Authentication authentication) {

        return ResponseEntity.ok(
                rentalService.rentVideo(videoId, authentication.getName())
        );
    }

    @PostMapping("/{videoId}/return")
    public ResponseEntity<RentalResponse> returnVideo(
            @PathVariable Long videoId,
            Authentication authentication) {

        return ResponseEntity.ok(
                rentalService.returnVideo(videoId, authentication.getName())
        );
    }
}