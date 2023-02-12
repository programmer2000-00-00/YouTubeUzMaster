package com.company.YouTubeUz.mapper;

import com.company.YouTubeUz.enums.VideoStatus;

public interface VideoFullInfoMapper {
    Integer getV_id();
    String getV_key();
    String getV_title();
    String getV_description();
    VideoStatus getV_status();
    Integer getV_shared_count();
    Integer getV_view_count();

    Integer getCh_id();
    String getCh_name();
    String getCh_photo();
    Integer getCh_profile_id();

    Integer getCa_id();
    String getCa_name();
}
