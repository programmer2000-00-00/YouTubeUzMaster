package com.company.YouTubeUz.repository;

import com.company.YouTubeUz.entity.CommentLikeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLikeEntity, Integer> {
    Optional<CommentLikeEntity> findByCommentIdAndProfileId(Integer commentId, Integer pId);

    Page<CommentLikeEntity> findByProfileId(Integer pId, Pageable pageable);

    Optional<CommentLikeEntity> findByCommentId(Integer commentId);
}