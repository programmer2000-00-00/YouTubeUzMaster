package com.company.YouTubeUz.repository;

import com.company.YouTubeUz.entity.ChannelEntity;
import com.company.YouTubeUz.enums.ChannelStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ChannelRepository extends JpaRepository<ChannelEntity, Integer> {
    Optional<ChannelEntity> findByName(String name);
    Optional<ChannelEntity> findByKey(String key);
    List<ChannelEntity> findByProfileId(Integer profileId);

    @Transactional
    @Modifying
    @Query("update ChannelEntity as ab set ab.name=:name, ab.description=:descript where ab.key=:key")
    int update(@Param("name") String name, @Param("descript") String descript, @Param("key") String key);

    @Transactional
    @Modifying
    @Query("update ChannelEntity as a set a.profileId=:image where a.key=:key")
    void updatePhoto(@Param("image") String imageId, @Param("key") String key);

    @Transactional
    @Modifying
    @Query("update ChannelEntity as a set a.bannerId=:image where a.key=:key")
    void updateBanner(@Param("image") String imageId, @Param("key") String key);

    List<ChannelEntity> findAllByProfileId(Integer profileId, Sort sort);

    @Transactional
    @Modifying
    @Query("update ChannelEntity as a set a.status=:status where a.key=:key")
    void updateStatus(@Param("status") ChannelStatus status, @Param("key") String key);
}
