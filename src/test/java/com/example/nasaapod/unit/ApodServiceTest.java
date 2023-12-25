package com.example.nasaapod.unit;

import com.example.nasaapod.dto.ApodData;
import com.example.nasaapod.repositories.ApodRepository;
import com.example.nasaapod.services.ApodService;
import com.example.nasaapod.services.NasaImageDownloader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApodServiceTest {

    @Mock
    NasaImageDownloader imageDownloader;

    @Mock
    ApodRepository apodRepository;

    @InjectMocks
    ApodService apodService;

    LocalDate date = LocalDate.parse("2022-09-20");

    LocalDate badDate = LocalDate.parse("1000-03-20");

    ApodData data = new ApodData(0L, date, "bruuuuhhhhh", "werudwfkj", "bruhich", "erlkjt.mds");

    @Test
    @DisplayName("Получение картинки из базы")
    void getApodByDateFromDB() {
        when(apodRepository.findByDate(date)).thenReturn(Optional.of(data));
        apodService.getApodByDate(date).ifPresentOrElse(
                apodData -> assertEquals(apodData, data),
                () -> fail("ApodData is empty")
        );
        verify(apodRepository, times(1))
                .findByDate(eq(date));
        verify(imageDownloader, times(0))
                .downloadImageByDate(eq(date));

    }

    @Test
    @DisplayName("Получение картинки от NASA, если нет в базе")
    void getApodByDateFromNasa() {
        when(apodRepository.findByDate(date)).thenReturn(Optional.empty());
        when(imageDownloader.downloadImageByDate(date)).thenReturn(Mono.just(data));
        apodService.getApodByDate(date).ifPresentOrElse(
                apodData -> assertEquals(apodData, data),
                () -> fail("ApodData is empty")
        );
        verify(apodRepository, times(1))
                .findByDate(eq(date));
        verify(imageDownloader, times(1))
                .downloadImageByDate(eq(date));
    }

    @Test
    @DisplayName("Ничего не возвращаем, если дата не существует в базе NASA")
    void getApodByDateReturnsEmptyWhenDateIsBad(){
        when(apodRepository.findByDate(badDate)).thenReturn(Optional.empty());
        when(imageDownloader.downloadImageByDate(badDate)).thenReturn(Mono.empty());
        assertEquals(Optional.empty(), apodService.getApodByDate(badDate));
        verify(apodRepository, times(1))
                .findByDate(eq(badDate));
        verify(imageDownloader, times(1))
                .downloadImageByDate(eq(badDate));
    }
}