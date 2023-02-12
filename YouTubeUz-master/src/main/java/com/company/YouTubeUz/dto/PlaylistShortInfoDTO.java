package com.company.YouTubeUz.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaylistShortInfoDTO {
    private Integer id;
    private String name;
    private LocalDateTime createdDate;
    private ChannelForPlaylistShortInfoDTO channel;
    private Integer videoCount;
    private List<VideoForPlaylistShortInfoDTO> video;
}
