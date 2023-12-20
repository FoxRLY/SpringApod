package com.example.nasaapod;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

    @BeforeEach
    void setUp() {

        RestAssured.baseURI = "http://localhost:" + port + "/";
        apodRepository.deleteAll();
    }

    @Test
    void findByDate() {
        ApodData data = new ApodData(LocalDate.parse("2020-12-12"), "bruuuuhhhhh", "werudwfkj", "bruhich", "erlkjt.mds");
        apodRepository.save(data);
        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/apod?date=2020-12-12")
                .then()
                .statusCode(200)
                .body("date", equalTo(data.getDate().toString()));
    }
}