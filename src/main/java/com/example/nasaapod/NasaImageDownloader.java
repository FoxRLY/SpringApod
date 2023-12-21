package com.example.nasaapod;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class NasaImageDownloader {
    private String apiKey;
    private String apiUrl;
    private final WebClient client;

    @Autowired
    public NasaImageDownloader(@Value("${apikey}")String apiKey, @Value("${apiurl}")String apiUrl, WebClient client){
        this.client = client;
        this.apiKey = apiKey;
        this.apiUrl = apiUrl;
    }

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
