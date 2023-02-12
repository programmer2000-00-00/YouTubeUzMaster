package com.company.YouTubeUz.service;

import com.company.YouTubeUz.dto.SubscriptionDTO;
import com.company.YouTubeUz.entity.SubscriptionEntity;
import com.company.YouTubeUz.enums.NotificationType;
import com.company.YouTubeUz.enums.SubscriptionStatus;
import com.company.YouTubeUz.exp.ItemNotFoundException;
import com.company.YouTubeUz.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionService {
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private ChannelService channelService;
    public SubscriptionDTO create(Integer pId, SubscriptionDTO dto) {
        SubscriptionEntity entity = new SubscriptionEntity();
        entity.setProfileId(pId);
        entity.setChannelId(dto.getChannelId());
        entity.setStatus(dto.getStatus());
        entity.setType(dto.getType());
        subscriptionRepository.save(entity);
        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }


    public String updateStatus(Integer subscriptionId, Integer pId, SubscriptionStatus status) {
        Optional<SubscriptionEntity> optional = subscriptionRepository.findByProfileIdAndId(pId, subscriptionId);
        if (optional.isEmpty()){
            throw new ItemNotFoundException("Subscription Not Found");
        }
        int n = subscriptionRepository.updateStatus(status, subscriptionId);
        if (n > 0){
            return "Success";
        }
        throw new ItemNotFoundException("Failed");
    }

    public String updateType(Integer subscriptionId, Integer pId, NotificationType type) {
        Optional<SubscriptionEntity> optional = subscriptionRepository.findByProfileIdAndId(pId, subscriptionId);
        if (optional.isEmpty()){
            throw new ItemNotFoundException("Subscription Not Found");
        }
        int n = subscriptionRepository.updateType(type, subscriptionId);
        if (n > 0){
            return "Success";
        }
        throw new ItemNotFoundException("Failed");
    }

    public List<SubscriptionDTO> getList(Integer pId) {
        List<SubscriptionEntity> subscriptionEntities = subscriptionRepository.findByProfileIdAndStatus(pId, SubscriptionStatus.ACTIVE);
        if (subscriptionEntities.isEmpty()){
            return new LinkedList<>();
        }
        List<SubscriptionDTO> subscriptionDTOS = new ArrayList<>();

        subscriptionEntities.forEach(entity -> {
            subscriptionDTOS.add(toDTO(entity));
        });
        return subscriptionDTOS;
    }


    public SubscriptionDTO toDTO(SubscriptionEntity entity){
        SubscriptionDTO dto = new SubscriptionDTO();
        dto.setId(entity.getId());
        dto.setType(entity.getType());
        dto.setStatus(entity.getStatus());
        dto.setChannelDTO(channelService.toSimpleDTO(entity.getChannelId()));
        return dto;
    }
 }
