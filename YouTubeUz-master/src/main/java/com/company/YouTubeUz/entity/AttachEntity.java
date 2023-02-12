package com.company.YouTubeUz.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "attach")
public class AttachEntity  {
    @Id
    private String id; // uuid
    @Column
    private String path;
    @Column
    private String extension;
    @Column()
    private String origenName;
    @Column()
    private Long size;
    @Column
    private LocalDateTime createdDate = LocalDateTime.now();
}
