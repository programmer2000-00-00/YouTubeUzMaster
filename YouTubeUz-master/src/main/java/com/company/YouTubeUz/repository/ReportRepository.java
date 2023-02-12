package com.company.YouTubeUz.repository;

import com.company.YouTubeUz.entity.ReportEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<ReportEntity, Integer> {
    Page<ReportEntity> findByProfileId(Integer profileId, Pageable pageable);
}