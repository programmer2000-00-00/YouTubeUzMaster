package com.company.YouTubeUz.service;

import com.company.YouTubeUz.dto.PlaylistVideoDTO;
import com.company.YouTubeUz.entity.PlaylistEntity;
import com.company.YouTubeUz.entity.PlaylistVideoEntity;
import com.company.YouTubeUz.exp.AppForbiddenException;
import com.company.YouTubeUz.exp.ItemNotFoundException;
import com.company.YouTubeUz.repository.PlaylistVideoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PlaylistVideoService {
    @Autowired
    private PlaylistVideoRepository playlistVideoRepository;
    @Autowired
    private PlaylistService playlistService;
    @Autowired
    @Lazy
    private VideoService videoService;
    public PlaylistVideoDTO create(Integer pId ,PlaylistVideoDTO dto) {
        PlaylistEntity playlist = playlistService.getById(dto.getPlaylistId());
        if (!playlist.getChannel().getProfileId().equals(pId)){
            throw new AppForbiddenException("Not Access");
        }
        videoService.getById(dto.getVideoId());
        PlaylistVideoEntity entity = new PlaylistVideoEntity();
        entity.setPlaylistId(dto.getPlaylistId());
        entity.setVideoId(dto.getVideoId());
        entity.setOrderNum(dto.getOrderNum());
        playlistVideoRepository.save(entity);
        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }


    public String update(Integer pId, PlaylistVideoDTO dto) {
        PlaylistEntity playlist = playlistService.getById(dto.getPlaylistId());
        if (!playlist.getChannel().getProfileId().equals(pId)){
            throw new AppForbiddenException("Not Access");
        }
        Optional<PlaylistVideoEntity> optional = playlistVideoRepository.findByPlaylistIdAndVideoId(dto.getPlaylistId(), dto.getVideoId());
        if (optional.isEmpty()){
            throw new ItemNotFoundException("Playlist Video Not Found");
        }
        PlaylistVideoEntity entity = optional.get();
        entity.setOrderNum(dto.getOrderNum());
        entity.setUpdatedDate(LocalDateTime.now());
        playlistVideoRepository.save(entity);
        return "Success";
    }

    public String  delete(Integer pId, Integer playlistId, Integer videoId) {
        Optional<PlaylistVideoEntity> optional = playlistVideoRepository.findByPlaylistIdAndVideoId(playlistId, videoId);
        if (optional.isEmpty()){
            throw new ItemNotFoundException("Playlist Video Not Found");
        }
        PlaylistVideoEntity entity = optional.get();
        PlaylistEntity playlist = playlistService.getById(entity.getPlaylistId());
        if (!playlist.getChannel().getProfileId().equals(pId)){
            throw new AppForbiddenException("Not Access");
        }
        playlistVideoRepository.delete(optional.get());
        return "Success";
    }

    public PlaylistVideoDTO getByVideoId(Integer videoId){
        log.error(  "Not found {}{}" , videoId, PlaylistVideoService.class);
        PlaylistVideoEntity entity = playlistVideoRepository.findByVideoId(videoId).orElseThrow(() -> new ItemNotFoundException("Video Not Found"));
        return toDTO(entity);
    }

    public PlaylistVideoDTO findByVideoId(Integer videoId){
        Optional<PlaylistVideoEntity> entity = playlistVideoRepository.findByVideoId(videoId);
        if (entity.isEmpty()){
            return null;
        }
        return toDTO(entity.get());
    }

    private PlaylistVideoDTO toDTO(PlaylistVideoEntity entity){
        PlaylistVideoDTO dto = new PlaylistVideoDTO();
        dto.setId(entity.getId());
        dto.setPlaylistId(entity.getPlaylistId());
        dto.setOrderNum(entity.getOrderNum());
        dto.setVideoId(entity.getVideoId());
        return dto;
    }

    public List<PlaylistVideoDTO> getByPlaylistId(Integer playlistId) {
        List<PlaylistVideoEntity> playlistVideoEntities = playlistVideoRepository.findByPlaylistId(playlistId);
        if (playlistVideoEntities.isEmpty()){
            return new LinkedList<>();
        }
        List<PlaylistVideoDTO> playlistVideoDTOS = new ArrayList<>();

        playlistVideoEntities.forEach(entity -> {
            playlistVideoDTOS.add(playlistVideoInfo(entity));
        });
        return playlistVideoDTOS;
    }

    public PlaylistVideoDTO playlistVideoInfo(PlaylistVideoEntity entity){
        PlaylistVideoDTO dto = new PlaylistVideoDTO();
        dto.setId(entity.getId());
        dto.setPlaylistId(entity.getPlaylistId());
        dto.setVideoDTO(videoService.videoDTOForPlaylistVIdeo(entity.getVideoId()));
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setOrderNum(entity.getOrderNum());
        return dto;
    }

    public Integer getVIdeoCount(Integer playlistId){
        return playlistVideoRepository.getCount(playlistId);
    }
}
