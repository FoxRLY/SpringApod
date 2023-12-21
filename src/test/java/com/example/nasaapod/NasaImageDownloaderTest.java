package com.example.nasaapod;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class NasaImageDownloaderTest {

    static ObjectMapper jsonMapper = new ObjectMapper();
    static MockWebServer mockServer;
    static NasaImageDownloader imageDownloader;

    @BeforeAll
    static void setUp() throws IOException {
        mockServer = new MockWebServer();
        mockServer.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockServer.shutdown();
    }

    @BeforeEach
    void init(){
        String host = String.format("http://localhost:%s", mockServer.getPort());
        WebClient client = WebClient.create();
        imageDownloader = new NasaImageDownloader("dslfk", host, client);
    }

    @Test
    @DisplayName("Получение данных из NASA по валидным данным")
    void downloadImageByDate() throws RuntimeException, JsonProcessingException {
        ApodResponse response = new ApodResponse(
                "lsdkfj",
                "2020-02-02",
                "fdslkfj",
                "dslkfjsdf",
                "dsflkj",
                "sdlfkjsd",
                "sd;fk",
                "sdlkfj");
        ApodData expectedData = ApodData.of(response);
        mockServer.enqueue(new MockResponse()
                .setBody(jsonMapper.writeValueAsString(response))
                .addHeader("Content-Type", "application/json"));
        Optional<ApodData> returnedData = imageDownloader.downloadImageByDate(LocalDate.parse("2020-02-02")).blockOptional();
        ApodData retreivedData = returnedData.orElseThrow(RuntimeException::new);
        assertEquals(retreivedData.getDate(), expectedData.getDate());
        assertEquals(retreivedData.getExplanation(), expectedData.getExplanation());
        assertEquals(retreivedData.getHdurl(), expectedData.getHdurl());
        assertEquals(retreivedData.getUrl(), expectedData.getUrl());
        assertEquals(retreivedData.getTitle(), expectedData.getTitle());
    }


    @Test
    @DisplayName("Получение ошибки из NASA по невалидным данным")
    void downloadImageByDateBad() {
        mockServer.enqueue(new MockResponse()
                .setResponseCode(400)
                .addHeader("Content-Type", "application/json"));
        Optional<ApodData> returnedData = imageDownloader.downloadImageByDate(LocalDate.parse("2020-02-02")).blockOptional();
        assertEquals(Optional.empty(), returnedData);
    }
}