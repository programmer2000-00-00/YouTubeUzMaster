package com.company.YouTubeUz.service;

import com.company.YouTubeUz.dto.*;
import com.company.YouTubeUz.entity.ChannelEntity;
import com.company.YouTubeUz.entity.PlaylistEntity;
import com.company.YouTubeUz.enums.PlaylistStatus;
import com.company.YouTubeUz.exp.ItemAlreadyExistsException;
import com.company.YouTubeUz.exp.ItemNotFoundException;
import com.company.YouTubeUz.mapper.PlaylistInfoForAdminMapper;
import com.company.YouTubeUz.mapper.PlaylistShortInfoMapper;
import com.company.YouTubeUz.mapper.VideoForPlaylistShortInfoMapper;
import com.company.YouTubeUz.mapper.ViewCountMapper;
import com.company.YouTubeUz.repository.PlaylistRepository;
import com.company.YouTubeUz.repository.PlaylistVideoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PlaylistService {
    @Autowired
    private PlaylistRepository playlistRepository;
    @Autowired
    private ChannelService channelService;
    @Autowired
    @Lazy
    private PlaylistVideoService playlistVideoService;
    @Autowired
    private AttachService attachService;
    @Autowired
    private PlaylistVideoRepository playlistVideoRepository;

    public PlaylistDTO create(Integer pId, PlaylistDTO dto) {
        channelService.check(dto.getChannelId(), pId);
        Optional<PlaylistEntity> optional = playlistRepository.findByNameAndChannelId(dto.getName(), dto.getChannelId());
        if (optional.isPresent()) {
            log.error("Playlis Already Exsists: {}", dto);
            throw new ItemAlreadyExistsException("Playlis Already Exsists");
        }
        PlaylistEntity entity = new PlaylistEntity();
        entity.setName(dto.getName());
        entity.setChannelId(dto.getChannelId());
        entity.setOrderNum(dto.getOrderNum());
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }
        entity.setStatus(dto.getStatus());
        playlistRepository.save(entity);
        return toDTO(entity);
    }

    public String update(Integer id, PlaylistDTO dto, Integer pId) {
        channelService.check(dto.getChannelId(), pId);
        Optional<PlaylistEntity> optional = playlistRepository.findByNameAndChannelId(dto.getName(), dto.getChannelId());
        if (optional.isPresent()) {
            log.error("Playlis Already Exsists: {}", dto);
            throw new ItemAlreadyExistsException("Playlis Already Exsists");
        }
        Optional<PlaylistEntity> optionalPlaylist = playlistRepository.findByIdAndChannelId(id, dto.getChannelId());
        if (optionalPlaylist.isEmpty()) {
            log.error("Playlist Not Found: {}", dto);
            throw new ItemAlreadyExistsException("Playlist Not Found");
        }
        PlaylistEntity entity = optionalPlaylist.get();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setStatus(dto.getStatus());
        entity.setUpdatedDate(LocalDateTime.now());
        entity.setOrderNum(dto.getOrderNum());
        entity.setChannelId(dto.getChannelId());
        playlistRepository.save(entity);
        return "Success";
    }

    public String updateStauts(Integer id, PlaylistStatus status, Integer pId) {
        List<ChannelEntity> channelList = channelService.getByProfileId(pId);
        if (channelList.isEmpty()) {
            throw new ItemNotFoundException("Playlist Not Found");
        }
        PlaylistEntity entity = getById(id);
        for (ChannelEntity channel : channelList) {
            if (!entity.getChannelId().equals(channel.getId())) {
                continue;
            } else {
                entity.setStatus(status);
                entity.setUpdatedDate(LocalDateTime.now());
                playlistRepository.save(entity);
                return "Success";
            }
        }
        throw new ItemNotFoundException("Playlist Not Found");
    }

    public String delete(Integer id, Integer pId) {
        List<ChannelEntity> channelList = channelService.getByProfileId(pId);
        if (channelList.isEmpty()) {
            throw new ItemNotFoundException("Playlist Not Found");
        }
        PlaylistEntity entity = getById(id);
        for (ChannelEntity channel : channelList) {
            if (!entity.getChannelId().equals(channel.getId())) {
                continue;
            } else {
                playlistRepository.delete(entity);
                return "Success";
            }
        }
        throw new ItemNotFoundException("Playlist Not Found");
    }

    public PlaylistEntity getById(Integer id) {
        return playlistRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Playlist Not Found"));
    }

    public PlaylistDTO getByIdDTO(Integer id) {
        PlaylistEntity entity = playlistRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Playlist Not Found"));
        return toDTO(entity);
    }

    public PageImpl<PlaylistInfoForAdminDTO> getListPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "orderNum"));
        List<PlaylistInfoForAdminDTO> dtoList = new ArrayList<>();
        Page<PlaylistInfoForAdminMapper> pageList = playlistRepository.getPlaylistInfo(pageable);
        pageList.forEach(playlistInfoForAdminMapper -> {
            dtoList.add(toDTOForAdmin(playlistInfoForAdminMapper));
        });
        return new PageImpl<PlaylistInfoForAdminDTO>(dtoList, pageable, pageList.getTotalElements());
    }

    public PageImpl<PlaylistInfoForAdminDTO> getListByUserId(Integer userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "orderNum"));
        List<PlaylistInfoForAdminDTO> dtoList = new ArrayList<>();
        Page<PlaylistInfoForAdminMapper> pageList = playlistRepository.getPlaylistInfoByUserId(userId, pageable);
        if (pageList.isEmpty()) {
            return new PageImpl<>(new LinkedList<>());
        }
        for (PlaylistInfoForAdminMapper playlistEntity : pageList) {
            dtoList.add(toDTOForAdmin(playlistEntity));
        }
        return new PageImpl<PlaylistInfoForAdminDTO>(dtoList, pageable, pageList.getTotalElements());
    }

    public PageImpl<PlaylistShortInfoDTO> getListByChannelKey(Integer pId, String channelKey, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "orderNum"));
        List<PlaylistShortInfoDTO> dtoList = new ArrayList<>();
        ChannelEntity channel = channelService.get(channelKey);
        Page<PlaylistShortInfoMapper> pageList = null;
        if (channel.getProfileId().equals(pId)){
          pageList = playlistRepository.getPlaylistShortInfo(channel.getId(), pageable);
        } else {
            pageList = playlistRepository.getPlaylistShortInfoForAll(channel.getId(), pageable, PlaylistStatus.PUBLIC);
        }
        for (PlaylistShortInfoMapper commentEntity : pageList) {
            dtoList.add(toDTOForShortInfo(commentEntity));
        }
        return new PageImpl<PlaylistShortInfoDTO>(dtoList, pageable, pageList.getTotalElements());
    }

    public PlaylistSimpleDTO getListByPlaylistId(Integer pId, Integer playlistId) {
        PlaylistEntity entity = getById(playlistId);
        PlaylistSimpleDTO dto = new PlaylistSimpleDTO();
        if (entity.getStatus().equals(PlaylistStatus.PRIVATE)){
            ChannelEntity channel = channelService.get(entity.getChannelId());
            if (!channel.getProfileId().equals(pId)){
                return new PlaylistSimpleDTO();
            } else {
                dto.setId(entity.getId());
                dto.setName(entity.getName());
                int count = playlistVideoRepository.getCount(entity.getId());
                dto.setVideoCount(count);
                dto.setLastUpdateDate(entity.getUpdatedDate());
                ViewCountMapper mapper = playlistVideoRepository.getViewCount(entity.getId());
                if (mapper != null){
                    dto.setVideoCount(mapper.getView_count());
                }
                dto.setVideoCount(playlistVideoService.getVIdeoCount(playlistId));
                return dto;
            }
        } else {
            dto.setId(entity.getId());
            dto.setName(entity.getName());
            int count = playlistVideoRepository.getCount(entity.getId());
            dto.setVideoCount(count);
            dto.setLastUpdateDate(entity.getUpdatedDate());
            ViewCountMapper mapper = playlistVideoRepository.getViewCount(entity.getId());
            if (mapper != null){
                dto.setVideoCount(mapper.getView_count());
            }
            dto.setVideoCount(playlistVideoService.getVIdeoCount(playlistId));
            return dto;
        }
    }

    private PlaylistDTO toDTO(PlaylistEntity entity) {
        PlaylistDTO dto = new PlaylistDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setId(entity.getId());
        dto.setDescription(entity.getDescription());
        dto.setOrderNum(entity.getOrderNum());
        dto.setChannelId(entity.getChannelId());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setUpdatedDate(entity.getUpdatedDate());
        return dto;
    }

    public PlaylistShortInfoDTO toDTOForShortInfo(PlaylistShortInfoMapper mapper){
        PlaylistShortInfoDTO dto = new PlaylistShortInfoDTO();
        dto.setId(mapper.getPl_id());
        dto.setName(mapper.getPl_name());
        dto.setCreatedDate(mapper.getPl_createdDate());
        int count = playlistVideoRepository.getCount(mapper.getPl_id());
        dto.setVideoCount(count);
        ChannelForPlaylistShortInfoDTO channel = new ChannelForPlaylistShortInfoDTO();
        channel.setId(mapper.getCh_id());
        channel.setName(mapper.getCh_name());
        dto.setChannel(channel);
        List<VideoForPlaylistShortInfoDTO> videoForPlaylistShortInfoDTOS = new ArrayList<>();
        List<VideoForPlaylistShortInfoMapper> videoList = playlistVideoRepository.findByPlaylistIdForPlaylistShortInfo(mapper.getPl_id());
        for (VideoForPlaylistShortInfoMapper video : videoList){
            VideoForPlaylistShortInfoDTO videoForPlaylistShortInfoDTO = new VideoForPlaylistShortInfoDTO();
            videoForPlaylistShortInfoDTO.setId(video.v_id());
            videoForPlaylistShortInfoDTO.setName(video.v_name());
            videoForPlaylistShortInfoDTO.setKey(video.v_key());
            videoForPlaylistShortInfoDTOS.add(videoForPlaylistShortInfoDTO);
        }
        dto.setVideo(videoForPlaylistShortInfoDTOS);
        return dto;
    }

    public PlaylistInfoForAdminDTO toDTOForAdmin(PlaylistInfoForAdminMapper mapper) {

        PlaylistInfoForAdminDTO dto = new PlaylistInfoForAdminDTO();
        dto.setId(mapper.getPl_id());
        dto.setName(mapper.getPl_name());
        dto.setDescription(mapper.getPl_description());

        ChannelForPlaylistInfoDTO channelDTO = new ChannelForPlaylistInfoDTO();
        channelDTO.setId(mapper.getCh_id());
        channelDTO.setName(mapper.getCh_name());
        if (mapper.getCh_photo_id() != null) {
            AttachDTO attachDTO = new AttachDTO(attachService.toOpenURL(mapper.getCh_photo_id()));
            channelDTO.setAttachDTO(attachDTO);
        }
        dto.setChannel(channelDTO);

        ProfileForPlaylistInfoDTO profileDTO = new ProfileForPlaylistInfoDTO();
        profileDTO.setId(mapper.getPr_id());
        profileDTO.setName(mapper.getPr_name());
        profileDTO.setSurname(mapper.getPr_surname());
        if (Optional.ofNullable(mapper.getPr_photo_id()).isPresent()) {
            AttachDTO attachDTO = new AttachDTO(attachService.toOpenURL(mapper.getPr_photo_id()));
            profileDTO.setAttachDTO(attachDTO);
        }
        channelDTO.setProfile(profileDTO);
        return dto;
    }
}
