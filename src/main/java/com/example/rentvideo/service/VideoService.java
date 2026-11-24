package com.example.rentvideo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.rentvideo.dto.VideoRequest;
import com.example.rentvideo.dto.VideoResponse;
import com.example.rentvideo.entity.Video;
import com.example.rentvideo.exception.VideoNotFoundException;
import com.example.rentvideo.repository.VideoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final VideoRepository videoRepository;

    public VideoResponse addVideo(VideoRequest request) {
        Video video = new Video();
        video.setTitle(request.getTitle());
        video.setDirector(request.getDirector());
        video.setGenre(request.getGenre());

        Video savedVideo = videoRepository.save(video);

        VideoResponse videoResponse = new VideoResponse(savedVideo.getId(), savedVideo.getTitle(),
                savedVideo.getDirector(), savedVideo.getGenre(), savedVideo.isAvailable());
        return videoResponse;

    }

    public List<VideoResponse> getAllVideos() {
        return videoRepository.findByAvailableTrue()
                .stream()
                .map(video -> new VideoResponse(
                        video.getId(), video.getTitle(), video.getDirector(), video.getGenre(), video.isAvailable()))
                .toList();
    }

    public VideoResponse getVideoById(Long id) {
        Video existingVideo =  videoRepository.findById(id)
                .orElseThrow(() -> new VideoNotFoundException("Video not found with id :" + id));

         VideoResponse videoResponse = new VideoResponse(existingVideo.getId(), existingVideo.getTitle(),
                existingVideo.getDirector(), existingVideo.getGenre(), existingVideo.isAvailable());
        return videoResponse;        


    }

   public VideoResponse updateVideo(Long id, VideoRequest request) {
        Video existingVideo = videoRepository.findById(id)
                .orElseThrow(() -> new VideoNotFoundException("Video not found with id :" + id));

        existingVideo.setDirector(request.getDirector());
        existingVideo.setTitle(request.getTitle());
        existingVideo.setGenre(request.getGenre());

        Video updatedVideo = videoRepository.save(existingVideo);

         VideoResponse videoResponse = new VideoResponse(updatedVideo.getId(), updatedVideo.getTitle(),
                updatedVideo.getDirector(), updatedVideo.getGenre(), updatedVideo.isAvailable());
        return videoResponse;       
    }

    public void deleteVideo(Long id) {
        Video existingVideo = videoRepository.findById(id)
                .orElseThrow(() -> new VideoNotFoundException("Video not found with id :" + id));

        videoRepository.delete(existingVideo);
    }
}
