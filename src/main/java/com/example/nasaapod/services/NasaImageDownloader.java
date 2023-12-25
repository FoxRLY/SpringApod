package com.example.nasaapod.services;

import com.example.nasaapod.dto.ApodData;
import com.example.nasaapod.dto.ApodResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class NasaImageDownloader {
    @Value("${apikey}")
    private String apiKey;
    @Value("${apiurl}")
    private String apiUrl;

    private final WebClient client;

    public Mono<ApodData> downloadImageByDate(LocalDate date) {
        String stringDate = date.toString();
        return client.get()
                .uri(apiUrl + "?api_key=" + apiKey + "&date=" + stringDate)
                .retrieve()
                .bodyToMono(ApodResponse.class)
                .onErrorResume(WebClientResponseException.BadRequest.class, badRequest -> Mono.empty())
                .flatMap(apodRes -> Mono.just(ApodData.of(apodRes)));
    }
}
