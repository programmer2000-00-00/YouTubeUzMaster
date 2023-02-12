package com.company.YouTubeUz.service;

import com.company.YouTubeUz.dto.CommentDTO;
import com.company.YouTubeUz.dto.UpdateCommentDTO;
import com.company.YouTubeUz.entity.CommentEntity;
import com.company.YouTubeUz.enums.ProfileRole;
import com.company.YouTubeUz.exp.AppForbiddenException;
import com.company.YouTubeUz.exp.ItemNotFoundException;
import com.company.YouTubeUz.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private VideoService videoService;
    public CommentDTO create(Integer pId, CommentDTO dto) {
        videoService.getById(dto.getVideoId());
        CommentEntity entity = new CommentEntity();
        entity.setProfileId(pId);
        if (dto.getVideoId() != null){
            entity.setVideoId(dto.getVideoId());
        }
        entity.setContent(dto.getContent());
        if (dto.getReplyId() != null){
            entity.setReplyId(dto.getReplyId());
        }
        commentRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    public String update(Integer pId, Integer commentId, UpdateCommentDTO dto) {
        CommentEntity comment = getById(commentId);
        if (!comment.getProfileId().equals(pId)){
            throw new AppForbiddenException("Not Access");
        }

        comment.setContent(dto.getContent());
        comment.setUpdatedDate(LocalDateTime.now());
        commentRepository.save(comment);
        return "Success";
    }

    public String delete(Integer commentId, Integer pId, ProfileRole role) {
        CommentEntity entity = getById(commentId);
        if (entity.getProfileId().equals(pId) || role.equals(ProfileRole.ADMIN)){
            commentRepository.delete(entity);
            return "Success";
        }
        throw new AppForbiddenException("Not Access");
    }

    public PageImpl<CommentDTO> getListForProfile(Integer pId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));
        List<CommentDTO> dtoList = new ArrayList<>();
        Page<CommentEntity > pageList = commentRepository.findByProfileId(pId, pageable);
        for (CommentEntity commentEntity : pageList){
            dtoList.add(toDTO(commentEntity));
        }
        return new PageImpl<CommentDTO>(dtoList, pageable, pageList.getTotalElements());

    }

    public PageImpl<CommentDTO> getPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));
        List<CommentDTO> dtoList = new ArrayList<>();
        Page<CommentEntity > pageList = commentRepository.findAll(pageable);
        for (CommentEntity commentEntity : pageList){
            dtoList.add(toDTO(commentEntity));
        }
        return new PageImpl<CommentDTO>(dtoList, pageable, pageList.getTotalElements());

    }

    public PageImpl<CommentDTO> getListByProfileId(Integer profileId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));
        List<CommentDTO> dtoList = new ArrayList<>();
        Page<CommentEntity > pageList = commentRepository.findByProfileId(profileId, pageable);
        for (CommentEntity commentEntity : pageList){
            dtoList.add(toDTO(commentEntity));
        }
        return new PageImpl<CommentDTO>(dtoList, pageable, pageList.getTotalElements());
    }

    public PageImpl<CommentDTO> getByVideoId(Integer videoId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));
        List<CommentDTO> dtoList = new ArrayList<>();
        Page<CommentEntity > pageList = commentRepository.findByVideoId(videoId, pageable);
        for (CommentEntity commentEntity : pageList){
            dtoList.add(toDTO(commentEntity));
        }
        return new PageImpl<CommentDTO>(dtoList, pageable, pageList.getTotalElements());

    }

    public PageImpl<CommentDTO> getReplyCommentsByCommentId(Integer commentId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));
        List<CommentDTO> dtoList = new ArrayList<>();
        Page<CommentEntity > pageList = commentRepository.findByReplyId(commentId, pageable);
        for (CommentEntity commentEntity : pageList){
            dtoList.add(toDTO(commentEntity));
        }
        return new PageImpl<CommentDTO>(dtoList, pageable, pageList.getTotalElements());
    }

    public CommentEntity getById(Integer commentId){
        return commentRepository.findById(commentId).orElseThrow(() -> new ItemNotFoundException("Comment Not Found"));
    }


    public CommentDTO toDTO (CommentEntity entity){
        CommentDTO dto = new CommentDTO();
        dto.setId(entity.getId());
        dto.setContent(entity.getContent());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setLike_count(entity.getLike_count());
        dto.setDislike_count(entity.getDislike_count());
        dto.setVideoDTO(videoService.videoDTOForComment(entity.getVideoId()));
        return dto;
    }
}
