package com.company.YouTubeUz.mapper;

import java.time.LocalDateTime;

public interface PlaylistShortInfoMapper {
    Integer getPl_id();
    String getPl_name();
    LocalDateTime getPl_createdDate();

    Integer getCh_id();
    String getCh_name();
}
