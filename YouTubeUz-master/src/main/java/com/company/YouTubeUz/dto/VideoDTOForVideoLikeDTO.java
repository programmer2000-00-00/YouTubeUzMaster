package com.company.YouTubeUz.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideoDTOForVideoLikeDTO {
    private Integer id;
    private String title;
    private String key;
    private ChannelForVideoShortInfoDTO channel;
    private String previewAttachId;
}
