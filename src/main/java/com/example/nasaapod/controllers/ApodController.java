package com.example.nasaapod.controllers;

import com.example.nasaapod.dto.ApodData;
import com.example.nasaapod.services.ApodService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
public class ApodController {
    private final ApodService apodService;

    @ResponseStatus(
            value = HttpStatus.BAD_REQUEST,
            reason = "Bad date formatting or date doesn't make sense"
    )
    @ExceptionHandler(BadRequestException.class)
    public void badRequestExceptionHandler(){
    }

    @GetMapping("/apod")
    public ResponseEntity<ApodData> findByDate(
            @RequestParam(name = "date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) throws BadRequestException {
        ApodData retrievedData = apodService.getApodByDate(date).orElseThrow(BadRequestException::new);
        return ResponseEntity.ok().body(retrievedData);
    }
}
