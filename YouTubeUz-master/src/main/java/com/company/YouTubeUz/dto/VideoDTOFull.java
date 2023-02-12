package com.company.YouTubeUz.dto;

import com.company.YouTubeUz.enums.VideoStatus;
import com.company.YouTubeUz.enums.VideoType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideoDTOFull {
    private Integer id;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private String previewAttachId;
    private String title;
    private String description;
    private Integer channelId;
    private Integer categoryId;
    private String attachId;
    private LocalDateTime publishedDate;
    private VideoStatus status;
    private String key;
    private VideoType type;
    private Integer viewCount;
    private Integer sharedCount;
    private Integer likeCount;
    private Integer dislikeCount;

    private AttachDTO previewAttach;
    private ChannelDTO channel;
}
