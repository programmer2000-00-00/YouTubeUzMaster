package com.company.YouTubeUz.repository;

import com.company.YouTubeUz.entity.PlaylistEntity;
import com.company.YouTubeUz.enums.PlaylistStatus;
import com.company.YouTubeUz.mapper.PlaylistInfoForAdminMapper;
import com.company.YouTubeUz.mapper.PlaylistShortInfoMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PlaylistRepository extends JpaRepository<PlaylistEntity, Integer> {
    Optional<PlaylistEntity> findByNameAndChannelId(String name, Integer channelId);

    Optional<PlaylistEntity> findByIdAndChannelId(Integer id, Integer channel);

    Page<PlaylistEntity> findByChannelId(Integer channelId, Pageable pageable);

    @Query("SELECT pl.id as pl_id, pl.name as pl_name, pl.description as pl_description, pl.status as pl_status, pl.orderNum as pl_order_num" +
            ", ch.id as ch_id, ch.name as ch_name, ch.photoId as ch_photo_id, " +
            " pr.id as pr_id, pr.name as pr_name, pr.surname as pr_surname, pr.attachId as pr_photo_id " +
            " from PlaylistEntity as pl " +
            " INNER JOIN pl.channel as ch " +
            "INNER JOIN ch.profile as pr" +
            " order by pl.createdDate desc ")
    Page<PlaylistInfoForAdminMapper> getPlaylistInfo(Pageable pageable);

    @Query("SELECT pl.id as pl_id, pl.name as pl_name, pl.description as pl_description, pl.status as pl_status, pl.orderNum as pl_order_num" +
            ", ch.id as ch_id, ch.name as ch_name, ch.photoId as ch_photo_id, " +
            " pr.id as pr_id, pr.name as pr_name, pr.surname as pr_surname, pr.attachId as pr_photo_id " +
            " from PlaylistEntity as pl " +
            " INNER JOIN pl.channel as ch " +
            "INNER JOIN ch.profile as pr" +
            " where pr.id=:userId " +
            " order by pl.createdDate desc ")
    Page<PlaylistInfoForAdminMapper> getPlaylistInfoByUserId(@Param("userId") Integer userId, Pageable pageable);

    @Query("SELECT pl.id as pl_id, pl.name as pl_name, pl.createdDate as pl_createdDate," +
            " ch.id as ch_id, ch.name as ch_name " +
            "from PlaylistEntity as pl " +
            " inner join pl.channel as ch " +
            " where ch.id=:channelId ")
    Page<PlaylistShortInfoMapper> getPlaylistShortInfo(@Param("channelId")Integer channelId, Pageable pageable);

    @Query("SELECT pl.id as pl_id, pl.name as pl_name, pl.createdDate as pl_createdDate," +
            " ch.id as ch_id, ch.name as ch_name " +
            "from PlaylistEntity as pl " +
            " inner join pl.channel as ch " +
            " where ch.id=:channelId and pl.status=:statusss ")
    Page<PlaylistShortInfoMapper> getPlaylistShortInfoForAll(@Param("channelId")Integer channelId, Pageable pageable,@Param("statusss") PlaylistStatus status);
}