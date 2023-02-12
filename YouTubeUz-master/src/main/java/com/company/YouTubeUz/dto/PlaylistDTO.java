package com.company.YouTubeUz.dto;

import com.company.YouTubeUz.enums.PlaylistStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaylistDTO  {
    private Integer id;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    @NotNull(message = "Channel required")
    private Integer channelId;
    @NotBlank(message = "Name required")
    private String name;
    private String description;
    private PlaylistStatus status;
    @NotNull(message = "Order Num required")
    private Integer orderNum;
}
