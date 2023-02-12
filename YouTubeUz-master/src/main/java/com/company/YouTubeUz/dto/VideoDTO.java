package com.company.YouTubeUz.dto;

import com.company.YouTubeUz.enums.VideoStatus;
import com.company.YouTubeUz.enums.VideoType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class VideoDTO {
    private Integer id;
    private String title;
    private String attachId;
    private Integer categoryId;
    private Integer channelId;
    private String description;
    private VideoType type;
    private VideoStatus status;
    private String previewAttachId;
    private LocalDateTime publishedDate;
}
