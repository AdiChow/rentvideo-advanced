package com.example.rentvideo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class VideoRequest {
@NotBlank    
private String title;
@NotBlank    
private String director;
@NotBlank    
private String genre;

}
