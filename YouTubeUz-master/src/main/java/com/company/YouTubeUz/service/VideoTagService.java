package com.company.YouTubeUz.service;

import com.company.YouTubeUz.dto.TagDTO;
import com.company.YouTubeUz.dto.VideoTagDTO;
import com.company.YouTubeUz.entity.VideoEntity;
import com.company.YouTubeUz.entity.VideoTagEntity;
import com.company.YouTubeUz.exp.AppBadRequestException;
import com.company.YouTubeUz.exp.AppForbiddenException;
import com.company.YouTubeUz.exp.ItemNotFoundException;
import com.company.YouTubeUz.repository.VideoTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class VideoTagService {
    @Autowired
    private VideoTagRepository videoTagRepository;
    @Autowired
    @Lazy
    private VideoService videoService;
    @Autowired
    private TagService tagService;

    public VideoTagDTO create(Integer pId, VideoTagDTO dto) {
        VideoEntity video = videoService.getById(dto.getVideoId());
        if (!video.getChannel().getProfileId().equals(pId)){
            throw new AppForbiddenException("Not Access");
        }
        TagDTO tag = tagService.getById(dto.getTagId());
        Optional<VideoTagEntity> optional = videoTagRepository.findByTagIdAndVideoId(dto.getTagId(), dto.getVideoId());
        if (optional.isPresent()) {
            throw new AppBadRequestException("Tag Already Exsists");
        }

        VideoTagEntity entity = new VideoTagEntity();
        entity.setTagId(tag.getId());
        entity.setVideoId(video.getId());
        videoTagRepository.save(entity);
        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }
    public String delete(Integer pId, Integer videoId){
        VideoEntity video = videoService.getById(videoId);
        if (!video.getChannel().getProfileId().equals(pId)){
            throw new AppForbiddenException("Not Access");
        }
        List<VideoTagEntity> videoTagList = videoTagRepository.findByVideoId(videoId);
        if (videoTagList.isEmpty()){
            throw new ItemNotFoundException("Video Not Found");
        }
        videoTagRepository.deleteAll(videoTagList);
        return "Success";
    }

    public List<VideoTagDTO> getByVideoId(Integer videoId){
        VideoEntity video = videoService.getById(videoId);
        List<VideoTagDTO> videoTagDTOS = new ArrayList<>();
        List<VideoTagEntity> videoTagList = videoTagRepository.findByVideoId(videoId);
        if (videoTagList.isEmpty()){
            throw new ItemNotFoundException("Video Not Found");
        }
        videoTagList.forEach(videoTagEntity -> {
            videoTagDTOS.add(toDTO(videoTagEntity));
        });
        return videoTagDTOS;
    }

    public List<VideoTagDTO> getByTagId(Integer tagId) {
        List<VideoTagDTO> videoTagDTOS = new ArrayList<>();
        List<VideoTagEntity> videoTagEntities = videoTagRepository.findByTagId(tagId);
        if (videoTagEntities.isEmpty()){
            return new LinkedList<>();
        }
        videoTagEntities.forEach(entity -> {
            videoTagDTOS.add(toDTO(entity));
        });
        return videoTagDTOS;
    }

    public VideoTagDTO toDTO(VideoTagEntity entity){
        VideoTagDTO dto = new VideoTagDTO();
        dto.setId(entity.getId());
        dto.setVideoId(entity.getVideoId());
        TagDTO tagDTO = tagService.toDTOForVideoTag(entity.getTagId());
        dto.setTagDTO(tagDTO);
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }
}
