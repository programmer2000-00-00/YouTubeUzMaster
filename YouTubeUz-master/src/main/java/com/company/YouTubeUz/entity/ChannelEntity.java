package com.company.YouTubeUz.entity;

import com.company.YouTubeUz.enums.ChannelStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "channels")
public class ChannelEntity extends BaseEntity {
    @Column
    private String name;
    @Column(columnDefinition = "text")
    private String description;
    @Column
    private String key;

    @Column
    @Enumerated(EnumType.STRING)
    private ChannelStatus status;

    @Column(name = "profile_id", nullable = false)
    private Integer profileId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", insertable = false, updatable = false)
    private ProfileEntity profile;

    @Column(name = "photo_id")
    private String photoId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id", insertable = false, updatable = false)
    private AttachEntity photo;

    @Column(name = "banner_id")
    private String bannerId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "banner_id", insertable = false, updatable = false)
    private AttachEntity banner;
}
