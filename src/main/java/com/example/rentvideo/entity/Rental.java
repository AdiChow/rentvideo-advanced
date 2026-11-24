package com.example.rentvideo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
    name = "rentals",
    indexes = {
        @Index(name = "idx_rental_user_returned", columnList = "user_id, returned_at"),
        @Index(name = "idx_rental_video_returned", columnList = "video_id, returned_at")
    }
)
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "video_id", nullable = false)
    private Video video;

    @Column(nullable = false)
    private LocalDateTime rentedAt;

    private LocalDateTime returnedAt;
}