package com.company.YouTubeUz.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaylistVideoDTO {
    private Integer id;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Integer playlistId;
    private Integer  videoId;
    private Integer orderNum;

    private VideoDTOForPlaylistVideoDTO videoDTO;
}
