package com.company.YouTubeUz.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideoDTOforAdmin {
    private VideoShortInfoDTO video;
    private ProfileDTO profile;
    private PlaylistDTO playlist;
}
