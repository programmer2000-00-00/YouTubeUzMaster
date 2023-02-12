package com.company.YouTubeUz.repository;

import com.company.YouTubeUz.mapper.VideoForPlaylistShortInfoMapper;
import com.company.YouTubeUz.entity.PlaylistVideoEntity;
import com.company.YouTubeUz.mapper.ViewCountMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlaylistVideoRepository extends JpaRepository<PlaylistVideoEntity, Integer> {
    Optional<PlaylistVideoEntity> findByVideoId(Integer videoId);
    Optional<PlaylistVideoEntity> findByPlaylistIdAndVideoId(Integer playlistId, Integer videoId);

    List<PlaylistVideoEntity> findByPlaylistId(Integer playlistId);

    @Query(value = "SELECT COUNT (p.*) from playlist_video AS p where p.playlist_id=:playlistId", nativeQuery = true)
    int getCount(@Param("playlistId") Integer playlistId);

    @Query("select v.id as v_id, v.title as v_name , v.key as v_key from PlaylistVideoEntity as plv inner join plv.video as v where plv.playlistId=:playlistId")
    List<VideoForPlaylistShortInfoMapper> findByPlaylistIdForPlaylistShortInfo(@Param("playlistId") Integer playlistId);

    @Query("select sum (v.viewCount) as view_count from PlaylistVideoEntity as pv inner join pv.video as v where pv.playlistId=:playlistId")
    ViewCountMapper getViewCount (@Param("playlistId") Integer playlistId);

}