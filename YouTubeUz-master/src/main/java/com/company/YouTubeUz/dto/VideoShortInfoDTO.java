package com.company.YouTubeUz.dto;

import com.company.YouTubeUz.entity.ChannelEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideoShortInfoDTO {
    private Integer id;
    private String key;
    private AttachDTO previewAttach;
    private LocalDateTime publishedDate;
    private ChannelForVideoShortInfoDTO channel;
    private Integer viewCount;
}
