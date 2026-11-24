package com.example.rentvideo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rentvideo.dto.VideoRequest;
import com.example.rentvideo.dto.VideoResponse;
import com.example.rentvideo.entity.Video;
import com.example.rentvideo.service.VideoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping("/videos")
@RequiredArgsConstructor 
public class VideoController {

    private final VideoService videoService;

    @PostMapping
    public ResponseEntity<VideoResponse> addVideo(@Valid @RequestBody VideoRequest request)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(videoService.addVideo(request));
    }

    @GetMapping
    public List<VideoResponse> getAllVideos()
    {
        return videoService.getAllVideos();
    }

    @GetMapping("/{id}")
    public VideoResponse getVideoById(@PathVariable Long id)
    {
        return videoService.getVideoById(id);

    }

    @PutMapping("/{id}")
    public VideoResponse updateVideo( @PathVariable Long id , @Valid  @RequestBody VideoRequest request )
    {
        return videoService.updateVideo(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVideo(@PathVariable Long id)
    {
        videoService.deleteVideo(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
