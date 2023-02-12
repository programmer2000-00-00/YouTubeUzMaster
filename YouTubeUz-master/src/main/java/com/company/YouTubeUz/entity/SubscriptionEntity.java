package com.company.YouTubeUz.entity;

import com.company.YouTubeUz.enums.NotificationType;
import com.company.YouTubeUz.enums.SubscriptionStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "subscriptions")
public class SubscriptionEntity extends BaseEntity {

    @Column(name = "profile_id")
    private Integer profileId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "profile_id", insertable = false, updatable = false)
    private ProfileEntity profile;

    @Column(name = "channel_id", nullable = false)
    private Integer channelId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", insertable = false, updatable = false)
    private ChannelEntity channel;

    @Column
    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status;

    @Column
    @Enumerated(EnumType.STRING)
    private NotificationType type;

}
