package com.company.YouTubeUz.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDTO {
    private Integer id;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private String content;
    private Integer replyId;
    private Integer profileId;
    private Integer videoId;
    private Integer like_count;
    private Integer dislike_count;

    private VideoDTOForVideoLikeDTO videoDTO;
}
