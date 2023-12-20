package com.example.nasaapod;

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

    LocalDate govnoDate = LocalDate.parse("1000-03-20");

    ApodData data = new ApodData(date, "bruuuuhhhhh", "werudwfkj", "bruhich", "erlkjt.mds");


//    @BeforeEach
//    void init(){
//
////        Mockito.when(imageDownloader.downloadImageByDate(date)).thenReturn(Mono.just(data));
//        Mockito.when(apodRepository.findByDate(date)).thenReturn(Optional.of(data));
//    }

    @Test
    @DisplayName("Получение картинки из базы")
    void getApodByDateFromDB() {
        when(apodRepository.findByDate(date)).thenReturn(Optional.of(data));
        assertEquals(data, apodService.getApodByDate(date).get());
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
        assertEquals(data, apodService.getApodByDate(date).get());
        verify(apodRepository, times(1))
                .findByDate(eq(date));
        verify(imageDownloader, times(1))
                .downloadImageByDate(eq(date));
    }

    @Test
    @DisplayName("Ничего не возвращаем, если дата говно")
    void getApodByDateReturnsEmptyWhenDateIsBad(){
        when(apodRepository.findByDate(govnoDate)).thenReturn(Optional.empty());
        when(imageDownloader.downloadImageByDate(govnoDate)).thenReturn(Mono.empty());
        assertEquals(Optional.empty(), apodService.getApodByDate(govnoDate));
        verify(apodRepository, times(1))
                .findByDate(eq(govnoDate));
        verify(imageDownloader, times(1))
                .downloadImageByDate(eq(govnoDate));
    }
}