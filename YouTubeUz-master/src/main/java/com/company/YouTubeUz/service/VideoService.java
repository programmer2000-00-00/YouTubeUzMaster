package com.company.YouTubeUz.service;

import com.company.YouTubeUz.dto.*;
import com.company.YouTubeUz.entity.*;
import com.company.YouTubeUz.enums.ProfileRole;
import com.company.YouTubeUz.enums.VideoStatus;
import com.company.YouTubeUz.exp.ItemNotFoundException;
import com.company.YouTubeUz.mapper.VideoFullInfoMapper;
import com.company.YouTubeUz.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class VideoService {
    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private AttachService attachService;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private PlaylistService playlistService;
    @Autowired
    private PlaylistVideoService playlistVideoService;
    @Autowired
    private VideoTagService videoTagService;
    @Autowired
    @Lazy
    private LikeService likeService;
    public VideoShortInfoDTO create(VideoDTO dto) {
        VideoEntity entity = new VideoEntity();
        entity.setTitle(dto.getTitle());
        entity.setAttachId(dto.getAttachId());
        entity.setCategoryId(dto.getCategoryId());
        entity.setChannelId(dto.getChannelId());
        entity.setDescription(dto.getDescription());
        String key = UUID.randomUUID().toString();
        entity.setKey(key);
        entity.setType(dto.getType());
        entity.setStatus(dto.getStatus());
        entity.setPreviewAttachId(dto.getPreviewAttachId());
        entity.setPublishedDate(LocalDateTime.now());
        videoRepository.save(entity);
        return videoShortInfo(entity);
    }

    public String updateVideoDetail(Integer videoId, UpdateVideoDTO dto, Integer pId){
        System.out.println(pId);
        List<ChannelEntity> channelList = channelService.getByProfileId(pId);
        if (channelList.isEmpty()){
            throw new ItemNotFoundException("Channel Not Found");
        }
        VideoEntity entity = getById(videoId);
        boolean n = false;
        for (ChannelEntity channel : channelList){
            if (entity.getChannelId().equals(channel.getId())){
                n = true;
            }
        }
        if (!n){
            throw new ItemNotFoundException("Channel Not Found");
        }
        entity.setCategoryId(dto.getCategoryId());
        entity.setDescription(dto.getDescription());
        entity.setTitle(dto.getTitle());
        entity.setUpdatedDate(LocalDateTime.now());
        videoRepository.save(entity);
        return "Success";
    }

    public VideoEntity getById(Integer id){
        return videoRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Video Not Found"));
    }

    public String updateStatus(Integer videoId, VideoStatus status, Integer pId) {
        VideoEntity entity = getById(videoId);
        List<ChannelEntity> channelList = channelService.getByProfileId(pId);
        if (channelList.isEmpty()){
            throw new ItemNotFoundException("Channel Not Found");
        }
        boolean n = false;
        for (ChannelEntity channel : channelList){
            if (entity.getChannelId().equals(channel.getId())){
                n = true;
            }
        }
        if (!n){
            throw new ItemNotFoundException("Channel Not Found");
        }
        entity.setStatus(status);
        videoRepository.save(entity);
        return "Success";
    }

    public String delete(Integer videoId, Integer pId, ProfileRole role) {
        VideoEntity entity = getById(videoId);
        if (role.equals(ProfileRole.ADMIN)){
            videoRepository.delete(entity);
            attachService.delete(entity.getAttachId());
            attachService.delete(entity.getPreviewAttachId());
            return "Success";
        } else {
            List<ChannelEntity> channelList = channelService.getByProfileId(pId);
            if (channelList.isEmpty()){
                throw new ItemNotFoundException("Channel Not Found z");
            }
            boolean n = false;
            for (ChannelEntity channel : channelList){
                if (entity.getChannelId().equals(channel.getId())){
                    n = true;
                }
            }
            if (!n){
                throw new ItemNotFoundException("Channel Not Found");
            }
            videoRepository.delete(entity);
            attachService.delete(entity.getAttachId());
            attachService.delete(entity.getPreviewAttachId());
            return "Success";
        }

    }

    public void updateViewCount(Integer videoId) {
        VideoEntity entity = getById(videoId);
        if (entity.getViewCount()== null){
            entity.setViewCount(1);
            videoRepository.save(entity);
        } else {
            videoRepository.updateViewCount(videoId);
        }
    }

    public PageImpl<VideoShortInfoDTO> getByCategoryId(Integer categoryId, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = PageRequest.of(page, size, sort);
        List<VideoShortInfoDTO> videoDTOS = new ArrayList<>();
        Page<VideoEntity> videoEntityPage = videoRepository.findByStatusAndCategoryId(VideoStatus.PUBLIC, categoryId, pageable);
        videoEntityPage.stream().forEach(videoEntity -> {
            videoDTOS.add(videoShortInfo(videoEntity));
        });

        return new PageImpl<>(videoDTOS, pageable , videoEntityPage.getTotalElements());
    }

    public PageImpl<VideoShortInfoDTO> getByTagId(Integer tagId, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = PageRequest.of(page, size, sort);
        List<VideoShortInfoDTO> videoDTOS = new ArrayList<>();
        List<VideoTagDTO> videoTagDTOS = videoTagService.getByTagId(tagId);
        if (videoTagDTOS.isEmpty()){
            return  new PageImpl<> (new LinkedList<>());
        }
        Page<VideoEntity> videoEntityPage = null;
        for (VideoTagDTO videoTagDTO : videoTagDTOS){
            videoEntityPage = videoRepository.findByStatusAndId(VideoStatus.PUBLIC, videoTagDTO.getVideoId(), pageable);
        }
        videoEntityPage.stream().forEach(videoEntity -> {
            videoDTOS.add(videoShortInfo(videoEntity));
        });

        return new PageImpl<>(videoDTOS, pageable , videoEntityPage.getTotalElements());
    }

    public List<VideoShortInfoDTO> getByTitle(String title){
        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
        List<VideoEntity> videoEntities = videoRepository.findByStatusAndTitle(VideoStatus.PUBLIC, title, sort);
        List<VideoShortInfoDTO> videoDTOS = new ArrayList<>();
        videoEntities.forEach(entity ->{
            videoDTOS.add(videoShortInfo(entity));
        });

        return videoDTOS;
    }

    public PageImpl<VideoDTOforAdmin> getAllVideos(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = PageRequest.of(page, size, sort);
        List<VideoDTOforAdmin> videoDTOS = new ArrayList<>();
        Page<VideoEntity> videoEntityPage = videoRepository.findAll(pageable);
        videoEntityPage.stream().forEach(videEntiy -> {
            videoDTOS.add(videoDTOforAdmin(videEntiy));
        });
        return new PageImpl<>(videoDTOS, pageable, videoEntityPage.getTotalElements());
    }

    private VideoDTOforAdmin videoDTOforAdmin(VideoEntity entity){
        VideoDTOforAdmin forAdmin = new VideoDTOforAdmin();
        VideoShortInfoDTO videoDTO = videoShortInfo(entity);
        ChannelEntity channel = channelService.get(videoDTO.getChannel().getKey());
        ProfileEntity profile = profileService.get(channel.getProfileId());
        PlaylistVideoDTO playlistVideo = playlistVideoService.findByVideoId(videoDTO.getId());
        if (playlistVideo != null){
            PlaylistDTO playlistDTO = playlistService.getByIdDTO(playlistVideo.getVideoId());
            forAdmin.setPlaylist(playlistDTO);
        }
        forAdmin.setVideo(videoDTO);
        forAdmin.setProfile(profileService.toDTOSimple(profile));
        return forAdmin;
    }

    private VideoShortInfoDTO videoShortInfo(VideoEntity entity){
        VideoShortInfoDTO dto = new VideoShortInfoDTO();
        dto.setId(entity.getId());
        dto.setKey(entity.getKey());
        dto.setPreviewAttach(attachService.toOpenURLDTO(entity.getPreviewAttachId()));
        dto.setPublishedDate(entity.getPublishedDate());
        dto.setChannel(channelService.toSimpleDTO(entity.getChannelId()));
        dto.setViewCount(entity.getViewCount());
        return dto;
    }

    public VideoDTOForVideoLikeDTO videoToDTOForVIdeoLike(Integer videId){
        VideoEntity entity = getById(videId);
        VideoDTOForVideoLikeDTO dto = new VideoDTOForVideoLikeDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setKey(entity.getKey());
        dto.setChannel(channelService.toSimpleDTO(entity.getChannelId()));
        dto.setPreviewAttachId(entity.getPreviewAttachId());
        return dto;
    }

    public VideoDTOForVideoLikeDTO videoDTOForComment(Integer videoId){
        VideoEntity entity = getById(videoId);
        VideoDTOForVideoLikeDTO dto = new VideoDTOForVideoLikeDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setPreviewAttachId(entity.getPreviewAttachId());
        return dto;
    }

    public VideoDTOForPlaylistVideoDTO videoDTOForPlaylistVIdeo(Integer videoId){
        VideoEntity entity = getById(videoId);
        VideoDTOForPlaylistVideoDTO dto = new VideoDTOForPlaylistVideoDTO();
        dto.setId(entity.getId());
        dto.setPreviewAttach(attachService.toOpenURLDTO(entity.getPreviewAttachId()));
        dto.setTitle(entity.getTitle());
        dto.setChannel(channelService.toSimpleDTO(entity.getChannelId()));
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    public VideoFullInfoDTO getByVideoKey(String key, Integer pId, ProfileRole role) {
        Optional<VideoFullInfoMapper> optional = videoRepository.getByVideoKeyFullInfo(key);
        if (optional.isEmpty()){
            throw new ItemNotFoundException("Video Not Found");
        }
        VideoFullInfoMapper mapper = optional.get();
        if (mapper.getV_status().equals(VideoStatus.PRIVATE)){
            if (mapper.getCh_profile_id().equals(pId) || role.equals(ProfileRole.ADMIN)){
                return toDTOForVideoFullInfo(mapper);
            } else {
                return null;
            }
        } else {
            return toDTOForVideoFullInfo(mapper);
        }
    }

    public VideoFullInfoDTO toDTOForVideoFullInfo(VideoFullInfoMapper mapper){
        VideoFullInfoDTO dto = new VideoFullInfoDTO();
        dto.setId(mapper.getV_id());
        dto.setKey(mapper.getV_key());
        dto.setTitle(mapper.getV_title());
        dto.setDescription(mapper.getV_description());
        dto.setStatus(mapper.getV_status());
        dto.setSharedCount(mapper.getV_shared_count());
        dto.setViewCount(mapper.getV_view_count());

        ChannelForVideoFullInfoDTO channel = new ChannelForVideoFullInfoDTO();
        channel.setId(mapper.getCh_id());
        channel.setName(mapper.getCh_name());
        channel.setAttachDTO(attachService.toOpenURLDTO(mapper.getCh_photo()));
        dto.setChannel(channel);

        CategoryForVideoFullInfoDTO category = new CategoryForVideoFullInfoDTO();
        category.setId(mapper.getCa_id());
        category.setName(mapper.getCa_name());
        dto.setCategory(category);
        LikeForVideoFullInfoDTO like = likeService.getByVideoIdAndProfileId(mapper.getV_id(), mapper.getCh_profile_id());
        if (like != null){
            dto.setLike(like);
        }
        return dto;
    }
}
