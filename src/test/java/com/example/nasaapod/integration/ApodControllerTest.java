package com.example.nasaapod.integration;

import com.example.nasaapod.dto.ApodData;
import com.example.nasaapod.repositories.ApodRepository;
import com.example.nasaapod.services.NasaImageDownloader;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import java.time.LocalDate;

import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApodControllerTest {

    @LocalServerPort
    private Integer port;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15-alpine"
    );
    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    ApodRepository apodRepository;

    @Autowired
    NasaImageDownloader imageDownloader;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port + "/";
        apodRepository.deleteAll();
    }

    @Test
    @DisplayName("Проврека получения картинки из базы")
    void findByDate() {
        ApodData data = new ApodData(0L, LocalDate.parse("2020-12-12"), "bruuuuhhhhh", "werudwfkj", "bruhich", "erlkjt.mds");
        apodRepository.save(data);
        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/apod?date=2020-12-12")
                .then()
                .statusCode(200)
                .body("date", equalTo(data.getDate().toString()))
                .body("explanation", equalTo(data.getExplanation()))
                .body("hdurl", equalTo(data.getHdurl()))
                .body("title", equalTo(data.getTitle()))
                .body("url", equalTo(data.getUrl()));
    }

    @Test
    @DisplayName("Проверка получения картинки из NASA")
    void findByDateFromNasa() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/apod?date=2020-12-12")
                .then()
                .statusCode(200)
                .body("date", equalTo("2020-12-12"));
    }

    @Test
    @DisplayName("Проверка на ошибку при несуществующей дате")
    void findByBadDate() {
        RestAssured.given()
                .when()
                .get("/apod?date=1000-09-09")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("Проверка на ошибку при неверно отформатированной дате")
    void findByBadFromattedDate() {
        RestAssured.given()
                .when()
                .get("/apod?date=232390-233834-sdf")
                .then()
                .statusCode(400);
    }
}