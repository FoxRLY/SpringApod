package com.example.nasaapod.dto;

import lombok.Data;

@Data
public class ApodResponse {
    private final String copyright;
    private final String date;
    private final String explanation;
    private final String hdurl;
    private final String media_type;
    private final String service_version;
    private final String title;
    private final String url;
}
