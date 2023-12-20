package com.example.nasaapod;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
public class NasaImageDownloader {

    @Autowired
    NasaImageDownloader(WebClient client) {
        this.client = client;
    }

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
