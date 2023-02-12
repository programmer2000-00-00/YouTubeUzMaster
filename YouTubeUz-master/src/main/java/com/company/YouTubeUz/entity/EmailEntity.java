package com.company.YouTubeUz.entity;

import com.company.YouTubeUz.enums.EmailType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "email_history")
@Getter
@Setter
public class EmailEntity extends BaseEntity{
    @Column
    private String toEmail;
    @Column
    @Enumerated(EnumType.STRING)
    private EmailType type;
}
