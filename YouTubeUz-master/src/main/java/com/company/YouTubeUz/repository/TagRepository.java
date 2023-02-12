package com.company.YouTubeUz.repository;

import com.company.YouTubeUz.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<TagEntity, Integer> {
    TagEntity findByName(String name);
}
