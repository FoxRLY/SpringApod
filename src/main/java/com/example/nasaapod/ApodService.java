package com.example.nasaapod;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApodService {
    private final NasaImageDownloader imageDownloader;
    private final ApodRepository apodRepository;

    @Transactional
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
