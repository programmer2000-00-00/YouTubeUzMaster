package com.company.YouTubeUz.dto;

import com.company.YouTubeUz.enums.ReportType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReportDTO {
    private Integer id;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private String content;
    private Integer toId;
    private Integer profileId;
    private ReportType type;

    private ProfileDTO profileDTO;
}
