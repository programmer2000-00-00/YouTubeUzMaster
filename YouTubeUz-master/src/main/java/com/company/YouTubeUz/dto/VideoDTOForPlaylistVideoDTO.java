package com.company.YouTubeUz.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideoDTOForPlaylistVideoDTO {
    private Integer id;
    private AttachDTO previewAttach;
    private String title;
    private ChannelForVideoShortInfoDTO channel;
    private LocalDateTime createdDate;
}
