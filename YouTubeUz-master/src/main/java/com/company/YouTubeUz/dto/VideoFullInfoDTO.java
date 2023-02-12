package com.company.YouTubeUz.dto;

import com.company.YouTubeUz.enums.VideoStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideoFullInfoDTO {
    private Integer id;
    private String key;
    private String title;
    private String description;
    private LocalDateTime publishedDate;
    private Integer viewCount;
    private Integer sharedCount;
    private AttachDTO previewAttach;
    private AttachDTO attach;
    private VideoStatus status;
    private ChannelForVideoFullInfoDTO channel;
    private CategoryForVideoFullInfoDTO category;
    private LikeForVideoFullInfoDTO like;
    private List<TagForVideoFullInfoDTO> tagList;
}
