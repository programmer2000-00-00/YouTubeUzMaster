package com.company.YouTubeUz.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChannelForVideoShortInfoDTO {
    private Integer id;
    private String key;
    private String name;
    private AttachDTO photo;
}
