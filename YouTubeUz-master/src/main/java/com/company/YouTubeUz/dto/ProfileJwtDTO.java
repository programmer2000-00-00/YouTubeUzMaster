package com.company.YouTubeUz.dto;

import com.company.YouTubeUz.enums.ProfileRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileJwtDTO {
    private Integer id;
    private ProfileRole role;
}
