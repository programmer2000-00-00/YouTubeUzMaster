package com.company.YouTubeUz.entity;

import com.company.YouTubeUz.enums.ProfileRole;
import com.company.YouTubeUz.enums.ProfileStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "profile")
public class ProfileEntity extends BaseEntity {
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String surname;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;

    @Column
    private Boolean visible = true;

    @Column
    @Enumerated(EnumType.STRING)
    private ProfileRole role;
    @Column
    @Enumerated(EnumType.STRING)
    private ProfileStatus status;

    @Column(name = "attach_id")
    private String attachId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attach_id", insertable = false, updatable = false)
    private AttachEntity attach;
}
