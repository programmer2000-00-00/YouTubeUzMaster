package com.company.YouTubeUz.dto;

import com.company.YouTubeUz.enums.NotificationType;
import com.company.YouTubeUz.enums.SubscriptionStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubscriptionDTO {
    private Integer id;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Integer profileId;
    private Integer channelId;
    private SubscriptionStatus status;
    private NotificationType type;

    private ChannelForVideoShortInfoDTO channelDTO;
}
