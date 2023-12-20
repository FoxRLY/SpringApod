package com.example.nasaapod;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface ApodRepository extends JpaRepository<ApodData, LocalDate> {
    Optional<ApodData> findByDate(LocalDate date);
}
