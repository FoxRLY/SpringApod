package com.example.nasaapod.services;

import com.example.nasaapod.repositories.ApodRepository;
import com.example.nasaapod.dto.ApodData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApodService {
    private final NasaImageDownloader imageDownloader;
    private final ApodRepository apodRepository;
    public Optional<ApodData> getApodByDate(LocalDate date) {
        Optional<ApodData> dbResult = apodRepository.findByDate(date);
        if (dbResult.isPresent()) {
            return dbResult;
        }
        Optional<ApodData> apiResult = imageDownloader.downloadImageByDate(date).blockOptional();
        apiResult.ifPresent(apodRepository::save);
        return apiResult;
    }
}
