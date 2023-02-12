package com.company.YouTubeUz.repository;

import com.company.YouTubeUz.entity.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {
    Page<CommentEntity> findByProfileId(Integer pId, Pageable pageable);

    Page<CommentEntity> findByVideoId(Integer videoId, Pageable pageable);

    Page<CommentEntity> findByReplyId(Integer replyId, Pageable pageable);
}