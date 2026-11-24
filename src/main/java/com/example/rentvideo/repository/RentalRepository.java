package com.example.rentvideo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rentvideo.entity.Rental;

public interface RentalRepository extends JpaRepository<Rental, Long> {

    long countByUserIdAndReturnedAtIsNull(Long userId);

    boolean existsByUserIdAndVideoIdAndReturnedAtIsNull(
            Long userId,
            Long videoId
    );

    Optional<Rental> findByUserIdAndVideoIdAndReturnedAtIsNull(
            Long userId,
            Long videoId
    );

    List<Rental> findByUserId(Long userId);
}