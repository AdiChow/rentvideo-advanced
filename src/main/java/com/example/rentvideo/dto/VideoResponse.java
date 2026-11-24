package com.example.rentvideo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
@AllArgsConstructor 
public class VideoResponse {
private Long id;
private String title;
private String director;
private String genre;
private boolean available;

}
