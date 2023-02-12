package com.company.YouTubeUz.entity;

import ch.qos.logback.classic.db.names.ColumnName;
import com.company.YouTubeUz.enums.PlaylistStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "playlist")
public class PlaylistEntity extends BaseEntity{

    @Column(name = "channel_id", nullable = false)
    private Integer channelId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", insertable = false, updatable = false)
    private ChannelEntity channel;
    @Column
    private String name;
    @Column(columnDefinition = "text")
    private String description;
    @Column
    @Enumerated(EnumType.STRING)
    private PlaylistStatus status;
    @Column
    private Integer orderNum;
}
