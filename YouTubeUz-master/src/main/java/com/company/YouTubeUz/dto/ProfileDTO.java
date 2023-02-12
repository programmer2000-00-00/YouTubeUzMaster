package com.company.YouTubeUz.dto;

import com.company.YouTubeUz.enums.ProfileRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileDTO {
    private Integer id;
    @NotBlank(message = "Name required")
    private String name;
    @NotBlank(message = "Surname required")
    private String surname;
    @NotNull(message = "email required")
    @Email(message = "Email required")
    private String email;
    @NotBlank(message = "Password required")
    @Size(min = 4, max = 15, message = "About Me must between 10 and 20 characters")
    private String password;
    private ProfileRole role;
    private LocalDateTime updatedDate;
    private LocalDateTime createdDate;
    private String jwt;
    private String imageId;
    private AttachDTO image;
}
