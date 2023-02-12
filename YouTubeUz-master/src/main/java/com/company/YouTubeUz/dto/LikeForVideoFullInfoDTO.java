package com.company.YouTubeUz.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LikeForVideoFullInfoDTO {
    private Integer likeCount;
    private Integer dislikeCount;
    private Boolean isUserLiked;
    private Boolean isUserDisliked;
}
