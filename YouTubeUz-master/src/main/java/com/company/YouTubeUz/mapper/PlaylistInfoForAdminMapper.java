package com.company.YouTubeUz.mapper;

public interface PlaylistInfoForAdminMapper {
    Integer getPl_id();
    String getPl_name();
    String getPl_description();
    String getPl_status();
    Integer getOrder_num();

    Integer getCh_id();
    String getCh_name();
    String getCh_photo_id();

    Integer getPr_id();
    String getPr_name();
    String getPr_surname();
    String getPr_photo_id();
}
