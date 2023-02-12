package com.company.YouTubeUz.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileForPlaylistInfoDTO {
    private Integer id;
    private String name;
    private String surname;
    private AttachDTO attachDTO;
}
