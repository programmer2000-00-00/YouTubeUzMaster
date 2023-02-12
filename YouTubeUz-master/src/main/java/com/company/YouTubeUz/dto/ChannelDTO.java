package com.company.YouTubeUz.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@Slf4j
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChannelDTO {
    private Integer id;
    @NotBlank(message = "Name Required")
    private String name;
    private String description;
    private String photoId;
    private String status;
    private String bannerId;
    private Integer userId;
    private String key;

    private AttachDTO photo;
    private AttachDTO banner;
    private ProfileDTO profileDTO;

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
