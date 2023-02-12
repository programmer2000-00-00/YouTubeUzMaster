package com.company.YouTubeUz.entity;

import com.company.YouTubeUz.enums.ReportType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "reports")
public class ReportEntity extends BaseEntity {
    @Column(columnDefinition = "text")
    private String content;

    @Column
    private Integer toId;
    @Column
    @Enumerated(EnumType.STRING)
    private ReportType type;

    @Column(name = "profile_id")
    private Integer profileId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "profile_id", insertable = false, updatable = false)
    private ProfileEntity profile;


}
