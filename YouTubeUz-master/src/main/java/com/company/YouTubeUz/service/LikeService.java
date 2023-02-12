package com.company.YouTubeUz.service;

import com.company.YouTubeUz.dto.*;
import com.company.YouTubeUz.entity.CommentLikeEntity;
import com.company.YouTubeUz.entity.VideoLikeEntity;
import com.company.YouTubeUz.enums.LikeStatus;
import com.company.YouTubeUz.enums.ProfileRole;
import com.company.YouTubeUz.exp.AppForbiddenException;
import com.company.YouTubeUz.exp.ItemNotFoundException;
import com.company.YouTubeUz.repository.CommentLikeRepository;
import com.company.YouTubeUz.repository.VideoLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LikeService {
    @Autowired
    private VideoLikeRepository videoLikeRepository;
    @Autowired
    private VideoService videoService;
    @Autowired
    private CommentLikeRepository commentLikeRepository;

    public VideoLikeDTO createVideoLike(Integer pId, VideoLikeDTO dto) {
        Optional<VideoLikeEntity> oldLikeOptional = videoLikeRepository.findByVideoIdAndProfileId(dto.getVideoId(), pId);
        if (oldLikeOptional.isPresent()){
            oldLikeOptional.get().setStatus(dto.getStatus());
            videoLikeRepository.save(oldLikeOptional.get());
            return dto;
        }
        VideoLikeEntity entity = new VideoLikeEntity();
        entity.setStatus(dto.getStatus());
        entity.setProfileId(pId);
        entity.setVideoId(dto.getVideoId());
        videoLikeRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    public String  deleteVideoLike(Integer likeId, Integer pId, ProfileRole role) {
        VideoLikeEntity entity = get(likeId);
        if (entity.getProfileId().equals(pId) || role.equals(ProfileRole.ADMIN)){
            videoLikeRepository.delete(entity);
        }
        throw new AppForbiddenException("Not Access");
    }

    public PageImpl<VideoLikeDTO> getVideoLikeByUserId(Integer pId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<VideoLikeEntity> pageList = videoLikeRepository.findByProfileId(pId, pageable);
        List<VideoLikeDTO> videoLikeDTOS = new ArrayList<>();
        for(VideoLikeEntity entity : pageList.getContent()){
            videoLikeDTOS.add(videoLikeInfo(entity));
        }
        return new PageImpl<>(videoLikeDTOS, pageable, pageList.getTotalElements());
    }

    public PageImpl<VideoLikeDTO> getLikeVideoByUserId(Integer userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<VideoLikeEntity> pageList = videoLikeRepository.findByProfileId(userId, pageable);
        List<VideoLikeDTO> videoLikeDTOS = new ArrayList<>();
        for(VideoLikeEntity entity : pageList.getContent()){
            videoLikeDTOS.add(videoLikeInfo(entity));
        }
        return new PageImpl<>(videoLikeDTOS, pageable, pageList.getTotalElements());
    }

    public LikeForVideoFullInfoDTO getByVideoIdAndProfileId(Integer videoId, Integer profileId){
        Integer likeCount = videoLikeRepository.countByVideoIdAndStatus(videoId, LikeStatus.LIKE);
        Integer dislikeCount = videoLikeRepository.countByVideoIdAndStatus(videoId, LikeStatus.DISLIKE);
        Boolean isUserLiked = videoLikeRepository.findByVideoIdAndProfileIdAndStatus(videoId, profileId, LikeStatus.LIKE);
        Boolean isUserDisliked = videoLikeRepository.findByVideoIdAndProfileIdAndStatus(videoId, profileId, LikeStatus.DISLIKE);
        LikeForVideoFullInfoDTO dto = new LikeForVideoFullInfoDTO();
        if (likeCount != null){
            dto.setLikeCount(likeCount);
        }
        if(dislikeCount != null){
            dto.setDislikeCount(dislikeCount);
        }
        if (isUserLiked != null){
            dto.setIsUserLiked(isUserLiked);
        }
        if (isUserDisliked != null){
            dto.setIsUserDisliked(isUserDisliked);
        }
        return dto;
    }

    public VideoLikeEntity get(Integer likeId){
        return videoLikeRepository.findById(likeId).orElseThrow(() -> new ItemNotFoundException("Like Not Found"));
    }

    public VideoLikeDTO videoLikeInfo(VideoLikeEntity entity){
        VideoLikeDTO dto = new VideoLikeDTO();
        dto.setId(entity.getId());
        VideoDTOForVideoLikeDTO videoDTO = videoService.videoToDTOForVIdeoLike(entity.getVideoId());
        dto.setVideoDTO(videoDTO);
        dto.setStatus(entity.getStatus());
        return dto;
    }

    /**
     * Comment_like
     * */

    public CommentLikeDTO createCommentLike(Integer pId, CommentLikeDTO dto) {
        Optional<CommentLikeEntity> oldLikeOptional = commentLikeRepository.findByCommentIdAndProfileId(dto.getCommentId(), pId);
        if (oldLikeOptional.isPresent()){
            oldLikeOptional.get().setStatus(dto.getStatus());
            commentLikeRepository.save(oldLikeOptional.get());
            return dto;
        }
        CommentLikeEntity entity = new CommentLikeEntity();
        entity.setStatus(dto.getStatus());
        entity.setProfileId(pId);
        entity.setCommentId(dto.getCommentId());
        commentLikeRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    public String  deleteCommentLike(Integer commentId, Integer pId, ProfileRole role) {
        CommentLikeEntity entity = getCommentById(commentId);
        if (entity.getProfileId().equals(pId) || role.equals(ProfileRole.ADMIN)){
            commentLikeRepository.delete(entity);
            return "Success";
        }
        throw new AppForbiddenException("Not Access");
    }

    public PageImpl<CommentLikeDTO> getCommentLikeByUserId(Integer pId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<CommentLikeEntity> pageList = commentLikeRepository.findByProfileId(pId, pageable);
        List<CommentLikeDTO> videoLikeDTOS = new ArrayList<>();
        for(CommentLikeEntity entity : pageList.getContent()){
            videoLikeDTOS.add(commentLikeInfo(entity));
        }
        return new PageImpl<>(videoLikeDTOS, pageable, pageList.getTotalElements());
    }

    public PageImpl<CommentLikeDTO> getLikeCommentByUserId(Integer userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<CommentLikeEntity> pageList = commentLikeRepository.findByProfileId(userId, pageable);
        List<CommentLikeDTO> videoLikeDTOS = new ArrayList<>();
        for(CommentLikeEntity entity : pageList.getContent()){
            videoLikeDTOS.add(commentLikeInfo(entity));
        }
        return new PageImpl<>(videoLikeDTOS, pageable, pageList.getTotalElements());
    }

    public CommentLikeEntity getCommentById(Integer commentId){
        return commentLikeRepository.findByCommentId(commentId).orElseThrow(() -> new ItemNotFoundException("Like Not Found"));
    }

    public CommentLikeDTO commentLikeInfo(CommentLikeEntity entity){
        CommentLikeDTO dto = new CommentLikeDTO();
        dto.setId(entity.getId());
        dto.setCommentId(entity.getCommentId());
        dto.setStatus(entity.getStatus());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setProfileId(entity.getProfileId());
        return dto;
    }
}
