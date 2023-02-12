package com.company.YouTubeUz.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaylistSimpleDTO {
    private Integer id;
    private String name;
    private Integer videoCount;
    private Integer totalViewCount;
    private LocalDateTime lastUpdateDate;
}
