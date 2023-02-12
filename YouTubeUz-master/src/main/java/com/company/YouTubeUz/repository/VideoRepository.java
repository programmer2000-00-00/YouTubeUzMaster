package com.company.YouTubeUz.repository;

import com.company.YouTubeUz.entity.VideoEntity;
import com.company.YouTubeUz.enums.VideoStatus;
import com.company.YouTubeUz.mapper.VideoFullInfoMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface VideoRepository extends JpaRepository<VideoEntity, Integer> {
    @Transactional
    @Modifying
    @Query(value = "update VideoEntity a set a.viewCount = a.viewCount + 1 where a.id =:id")
    void updateViewCount(@Param("id") Integer id);

    Page<VideoEntity> findByStatusAndCategoryId(VideoStatus status, Integer categoryId, Pageable pageable);

    Page<VideoEntity> findByStatusAndId(VideoStatus status, Integer id, Pageable pageable);

    List<VideoEntity> findByStatusAndTitle(VideoStatus status, String title, Sort sort);

    @Query("select v.id as v_id, v.key as v_key, v.title as v_title, v.description as v_description, v.status as v_status, v.sharedCount as v_shared_count, v.viewCount as v_view_count, " +
            " ch.id as ch_id, ch.name as ch_name, ch.photoId as ch_photo, ch.profileId as ch_profile_id, " +
            " ca.id as ca_id, ca.name as ca_name " +
            " from VideoEntity as v " +
            " inner join v.category as ca " +
            " inner join v.channel as ch " +
            " where v.key=:videoKey")
    Optional<VideoFullInfoMapper> getByVideoKeyFullInfo(@Param("videoKey") String videoKey);

}