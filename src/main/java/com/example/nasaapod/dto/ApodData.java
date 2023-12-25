package com.example.nasaapod.dto;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "picture_date")
    private LocalDate date;
    @Column(name = "explanation", length=2048)
    private String explanation;
    @Column(name = "hdurl")
    private String hdurl;
    @Column(name = "title")
    private String title;
    @Column(name = "url")
    private String url;

    public static ApodData of(ApodResponse resp){
        LocalDate newDate = LocalDate.parse(resp.getDate());

        return new ApodData(
                0L,
                newDate,
                resp.getExplanation().substring(0, Math.min(2048, resp.getExplanation().length())),
                resp.getHdurl().substring(0, Math.min(255, resp.getHdurl().length())),
                resp.getTitle().substring(0, Math.min(255, resp.getTitle().length())),
                resp.getUrl().substring(0, Math.min(255, resp.getUrl().length())));
    }
}
