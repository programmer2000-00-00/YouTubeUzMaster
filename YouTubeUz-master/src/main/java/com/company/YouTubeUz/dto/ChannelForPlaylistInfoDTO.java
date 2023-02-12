package com.company.YouTubeUz.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChannelForPlaylistInfoDTO {
    private Integer id;
    private String name;
    private AttachDTO attachDTO;
    private ProfileForPlaylistInfoDTO profile;
}
