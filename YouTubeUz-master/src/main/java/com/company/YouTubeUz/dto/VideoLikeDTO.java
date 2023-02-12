package com.company.YouTubeUz.dto;

import com.company.YouTubeUz.enums.LikeStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideoLikeDTO {
    private Integer id;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private LikeStatus status;
    private Integer profileId;
    private Integer videoId;

    private VideoDTOForVideoLikeDTO videoDTO;
}
