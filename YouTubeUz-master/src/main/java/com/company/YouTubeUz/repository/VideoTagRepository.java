package com.company.YouTubeUz.repository;

import com.company.YouTubeUz.entity.VideoTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VideoTagRepository extends JpaRepository<VideoTagEntity, Integer> {
    List<VideoTagEntity> findByVideoId(Integer videoId);
    List<VideoTagEntity> findByTagId(Integer tagId);

    Optional<VideoTagEntity> findByTagIdAndVideoId(Integer tagId, Integer videoId);
}