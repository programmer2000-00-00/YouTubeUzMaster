package com.company.YouTubeUz.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "tags")
public class TagEntity extends BaseEntity {
    @Column
    private String name;
}
