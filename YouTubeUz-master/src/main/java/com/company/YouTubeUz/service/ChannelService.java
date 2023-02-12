package com.company.YouTubeUz.service;

import com.company.YouTubeUz.dto.ChannelDTO;
import com.company.YouTubeUz.dto.ChannelForPlaylistShortInfoDTO;
import com.company.YouTubeUz.dto.ChannelForVideoShortInfoDTO;
import com.company.YouTubeUz.entity.AttachEntity;
import com.company.YouTubeUz.entity.ChannelEntity;
import com.company.YouTubeUz.enums.ChannelStatus;
import com.company.YouTubeUz.enums.ProfileRole;
import com.company.YouTubeUz.exp.AppForbiddenException;
import com.company.YouTubeUz.exp.ItemAlreadyExistsException;
import com.company.YouTubeUz.exp.ItemNotFoundException;
import com.company.YouTubeUz.repository.AttachRepository;
import com.company.YouTubeUz.repository.ChannelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class ChannelService {
    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private AttachRepository attachRepository;
    @Autowired
    private AttachService attachService;
    public ChannelDTO create(ChannelDTO dto, Integer pId) {
        Optional<ChannelEntity> optional = channelRepository.findByName(dto.getName());
        if (optional.isPresent()){
            throw new ItemAlreadyExistsException("Channel name already exists");
        }
        ChannelEntity entity = new ChannelEntity();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        Optional<AttachEntity> banner = attachRepository.findById(dto.getBannerId());
        if (banner.isPresent()){
            entity.setBannerId(dto.getBannerId());
        }
        Optional<AttachEntity> photo = attachRepository.findById(dto.getPhotoId());
        if (photo.isPresent()){
            entity.setPhotoId(dto.getPhotoId());
        }
        entity.setProfileId(pId);
        entity.setStatus(ChannelStatus.ACTIVE);
        String uuid = UUID.randomUUID().toString();
        entity.setKey(uuid);
        channelRepository.save(entity);
        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setKey(uuid);
        return dto;
    }

    private ChannelDTO toDTO(ChannelEntity entity){
        ChannelDTO dto = new ChannelDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setKey(entity.getKey());
        dto.setBanner(attachService.toOpenURLDTO(entity.getBannerId()));
        dto.setPhoto(attachService.toOpenURLDTO(entity.getPhotoId()));
        dto.setDescription(entity.getDescription());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setUpdatedDate(entity.getUpdatedDate());
        dto.setUserId(entity.getProfileId());
        return dto;
    }

    public ChannelForVideoShortInfoDTO toSimpleDTO(Integer channelId){
        ChannelEntity entity = get(channelId);
        ChannelForVideoShortInfoDTO dto = new ChannelForVideoShortInfoDTO();
        dto.setId(entity.getId());
        dto.setKey(entity.getKey());
        dto.setName(entity.getName());
        if (entity.getPhotoId() != null){
            dto.setPhoto(attachService.toOpenURLDTO(entity.getPhotoId()));
        }
        return dto;
    }

    public ChannelForPlaylistShortInfoDTO toDTOForPlaylistShortInfo(Integer channelId){
        ChannelEntity entity = get(channelId);
        ChannelForPlaylistShortInfoDTO dto = new ChannelForPlaylistShortInfoDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }

    public String update(String key, ChannelDTO dto, Integer pId) {
        ChannelEntity entity = get(key);
        check(entity, pId);
        int n = channelRepository.update(dto.getName(), dto.getDescription(), key);
        if (n > 0){
            return "Success";
        } else return "Failed";
    }

    public String  updatePhoto(String key, String imageId, Integer pId) {
        ChannelEntity entity = get(key);
        check(entity, pId);
        if (entity.getPhoto() != null){
            attachService.delete(entity.getPhoto().getId());
        }
        channelRepository.updatePhoto(imageId, key);
        return "Success";
    }

    public String updateBanner(String key, String imageId, Integer pId){
        ChannelEntity entity = get(key);
        check(entity, pId);
        if (entity.getBanner() != null){
            attachService.delete(entity.getBanner().getId());
        }
        channelRepository.updateBanner(imageId, key);
        return "Success";
    }

    public ChannelEntity get(String key){
        Optional<ChannelEntity> optional = channelRepository.findByKey(key);
        if (optional.isEmpty()){
            log.error("Channel Not Found: {}", key);
            throw new ItemNotFoundException("Channel Not Found");
        }
        return optional.get();
    }
    public List<ChannelEntity> getByProfileId(Integer pId){
        List<ChannelEntity> list = channelRepository.findByProfileId(pId);
        if (list.isEmpty()){
            log.error("Channel Not Found: {}", pId);
            throw new ItemNotFoundException("Channel Not Found");
        }
        return list;
    }

    public ChannelEntity get(Integer id){
        Optional<ChannelEntity> optional = channelRepository.findById(id);
        if (optional.isEmpty()){
            log.error("Channel Not Found: {}", id);
            throw new ItemNotFoundException("Channel Not Found");
        }
        return optional.get();
    }

    public void check(ChannelEntity entity, Integer pId){
        if (!entity.getProfileId().equals(pId)){
            log.error("Not Access: {}", pId);
            throw new AppForbiddenException("Not Access");
        }
    }

    public void check(Integer chId, Integer pId){
        ChannelEntity entity = get(chId);
        if (!entity.getProfileId().equals(pId)){
            log.error("Not Access: {}", pId);
            throw new AppForbiddenException("Not Access");
        }
    }

    public PageImpl<ChannelDTO> getListForAdmin(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ChannelEntity> pagination = channelRepository.findAll(pageable);

        List<ChannelEntity> channelEntityList = pagination.getContent();
        long totalElement = pagination.getTotalElements();
        List<ChannelDTO> dtoList = channelEntityList.stream().map(this::toDTO).toList();
        return new PageImpl<ChannelDTO>(dtoList, pageable, totalElement);
    }

    public List<ChannelDTO> getListForUser(Integer pId) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        List<ChannelEntity> list = channelRepository.findAllByProfileId(pId, sort);
        List<ChannelDTO> channelDTOS = new ArrayList<>();
        list.forEach(entity -> {
            channelDTOS.add(toDTO(entity));
        });
        return channelDTOS;
    }

    public String updateStatus(String key,ChannelStatus status, Integer pId, ProfileRole role) {
        ChannelEntity entity = get(key);
        if(!entity.getProfileId().equals(pId) || role.equals(ProfileRole.ADMIN)){
            log.error("Not Access: {}", pId);
            throw new AppForbiddenException("Not Access");
        }
        channelRepository.updateStatus(status, key);
        return "Success";
    }
}
