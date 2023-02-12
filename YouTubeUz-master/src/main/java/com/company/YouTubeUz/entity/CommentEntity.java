package com.company.YouTubeUz.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "comments")
public class CommentEntity extends BaseEntity{
    @Column(columnDefinition = "text")
    private String content;
    @Column
    private Integer replyId;

    @Column(name = "profile_id")
    private Integer profileId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "profile_id", insertable = false, updatable = false)
    private ProfileEntity profile;

    @Column(name = "video_id")
    private Integer videoId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id", insertable = false, updatable = false)
    private VideoEntity video;

    @Column
    private Integer like_count;
    @Column
    private Integer dislike_count;
}
