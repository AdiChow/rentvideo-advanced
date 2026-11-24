package com.example.rentvideo.service;

import java.time.LocalDateTime;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rentvideo.dto.RentalResponse;
import com.example.rentvideo.entity.Rental;
import com.example.rentvideo.entity.Role;
import com.example.rentvideo.entity.User;
import com.example.rentvideo.entity.Video;
import com.example.rentvideo.exception.UserNotFoundException;
import com.example.rentvideo.exception.VideoNotFoundException;
import com.example.rentvideo.repository.RentalRepository;
import com.example.rentvideo.repository.UserRepository;
import com.example.rentvideo.repository.VideoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RentalService {

    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;
    private final VideoRepository videoRepository;

    @Transactional
    public RentalResponse rentVideo(Long videoId, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (user.getRole() != Role.CUSTOMER) {
            throw new AccessDeniedException("Only customers can rent videos");
        }

        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new VideoNotFoundException(
                        "Video not found with id: " + videoId));

        boolean alreadyRented = rentalRepository
        .existsByUserIdAndVideoIdAndReturnedAtIsNull(user.getId(), videoId);

if (alreadyRented) {
    throw new IllegalStateException(
            "You already have an active rental for this video"
    );
}

if (!video.isAvailable()) {
    throw new IllegalStateException("Video is not available");
}

        long activeRentals = rentalRepository
                .countByUserIdAndReturnedAtIsNull(user.getId());

        if (activeRentals >= 2) {
            throw new IllegalStateException("Maximum of two active rentals allowed");
        }

        Rental rental = new Rental();
        rental.setUser(user);
        rental.setVideo(video);
        rental.setRentedAt(LocalDateTime.now());
        rental.setReturnedAt(null);

        video.setAvailable(false);
        videoRepository.save(video);

        Rental savedRental = rentalRepository.save(rental);

return new RentalResponse(
        savedRental.getId(),
        savedRental.getVideo().getId(),
        savedRental.getVideo().getTitle(),
        savedRental.getRentedAt(),
        savedRental.getReturnedAt()
);
    }

    @Transactional
    public RentalResponse returnVideo(Long videoId, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        videoRepository.findById(videoId)
        .orElseThrow(() -> new VideoNotFoundException(
                "Video not found with id: " + videoId));
        Rental rental = rentalRepository
                .findByUserIdAndVideoIdAndReturnedAtIsNull(user.getId(), videoId)
                .orElseThrow(() -> new IllegalStateException(
                        "No active rental found for this video"));

        rental.setReturnedAt(LocalDateTime.now());

        Video video = rental.getVideo();
        video.setAvailable(true);

        videoRepository.save(video);

       Rental savedRental = rentalRepository.save(rental);

return new RentalResponse(
        savedRental.getId(),
        savedRental.getVideo().getId(),
        savedRental.getVideo().getTitle(),
        savedRental.getRentedAt(),
        savedRental.getReturnedAt()
);
    }
}