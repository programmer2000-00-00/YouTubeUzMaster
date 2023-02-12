package com.company.YouTubeUz.entity;

import com.company.YouTubeUz.enums.VideoStatus;
import com.company.YouTubeUz.enums.VideoType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "video")
public class VideoEntity extends BaseEntity{
    @Column(columnDefinition = "text")
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @Column(name = "channel_id")
    private Integer channelId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", insertable = false, updatable = false)
    private ChannelEntity channel;

    @Column(name = "category_id")
    private Integer categoryId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private CategoryEntity category;

    @Column(name = "attach_id")
    private String attachId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attach_id", insertable = false, updatable = false)
    private AttachEntity attach;

    @Column(name = "preview_attach_id")
    private String previewAttachId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "preview_attach_id", insertable = false, updatable = false)
    private AttachEntity previewAttach;

    @Column
    private LocalDateTime publishedDate;

    @Column
    @Enumerated(EnumType.STRING)
    private VideoStatus status;

    @Column
    private String key;

    @Column
    @Enumerated(EnumType.STRING)
    private VideoType type;

    @Column
    private Integer viewCount;
    @Column
    private Integer sharedCount;
    @Column
    private Integer likeCount;
    @Column
    private Integer dislikeCount;
}
