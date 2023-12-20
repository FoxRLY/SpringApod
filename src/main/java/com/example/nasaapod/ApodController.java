package com.example.nasaapod;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
public class ApodController {
    private final ApodService apodService;

    @GetMapping("/apod")
    public ResponseEntity<ApodData> findByDate(
            @RequestParam(name = "date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) throws BadRequestException {
        ApodData retrievedData = apodService.getApodByDate(date).orElseThrow(BadRequestException::new);
        return ResponseEntity.ok().body(retrievedData);
    }
}
