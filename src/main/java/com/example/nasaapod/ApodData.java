package com.example.nasaapod;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@Table(name = "apod")
@NoArgsConstructor
@Getter
@Setter
public class ApodData {
    @Id
    @Column(name = "date")
    private LocalDate date;
    @Lob
    @Column(name = "explanation")
    private String explanation;
    @Column(name = "hdurl")
    private String hdurl;
    @Column(name = "title")
    private String title;
    @Column(name = "url")
    private String url;

    public static ApodData of(ApodResponse resp){
        LocalDate newDate = LocalDate.parse(resp.getDate());
        return new ApodData(newDate, resp.getExplanation(), resp.getHdurl(), resp.getTitle(), resp.getUrl());
    }
}
