package com.company.YouTubeUz.repository;

import com.company.YouTubeUz.entity.VideoLikeEntity;
import com.company.YouTubeUz.enums.LikeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VideoLikeRepository extends JpaRepository<VideoLikeEntity, Integer> {
    Optional<VideoLikeEntity> findByVideoIdAndProfileId(Integer videoId, Integer profileId);
    Page<VideoLikeEntity> findByProfileId(Integer pId, Pageable pageable);

    Integer countByVideoIdAndStatus(Integer videoId, LikeStatus status);
    Boolean findByVideoIdAndProfileIdAndStatus(Integer videoId, Integer profileId, LikeStatus status);
}