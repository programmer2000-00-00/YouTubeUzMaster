package com.company.YouTubeUz.service;

import com.company.YouTubeUz.dto.TagDTO;
import com.company.YouTubeUz.entity.ProfileEntity;
import com.company.YouTubeUz.entity.TagEntity;
import com.company.YouTubeUz.enums.ProfileRole;
import com.company.YouTubeUz.exp.AppBadRequestException;
import com.company.YouTubeUz.exp.AppForbiddenException;
import com.company.YouTubeUz.exp.ItemAlreadyExistsException;
import com.company.YouTubeUz.exp.ItemNotFoundException;
import com.company.YouTubeUz.repository.TagRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Slf4j
@Service
public class TagService {
    @Autowired
    private ProfileService profileService;
    @Autowired
    private TagRepository tagRepository;
    public TagDTO create(TagDTO dto) {
        TagEntity tag = tagRepository.findByName(dto.getName());
        if (tag != null) {
            log.warn("Tag Already Exists: {}", dto);
            throw new ItemAlreadyExistsException("Tag Already Exists");
        }
        TagEntity entity = new TagEntity();
        entity.setName(dto.getName());
        tagRepository.save(entity);
        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }
    public String update(Integer id, TagDTO dto) {
        Optional<TagEntity> optional = tagRepository.findById(id);
        if (optional.isEmpty()) {
            log.warn("Id Not Found: {}", id);
            throw new AppBadRequestException("Id Not Found");
        }
        TagEntity entity = tagRepository.findByName(dto.getName());
        if (entity != null) {
            log.warn("Tag Already Exists: {}", dto);
            throw new ItemAlreadyExistsException("Tag alredy exists");
        }
        TagEntity tag = optional.get();
        tag.setName(dto.getName());

        tagRepository.save(tag);
        return "Success";
    }
    public PageImpl<TagDTO> getList(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<TagEntity> pagination = tagRepository.findAll(pageable);

        List<TagEntity> tagEntitys = pagination.getContent();
        long totalElement = pagination.getTotalElements();
        List<TagDTO> dtoList = tagEntitys.stream().map(this::toDTO).toList();
        return new PageImpl<TagDTO>(dtoList, pageable, totalElement);
    }

    public TagDTO getById(Integer id) {
        Optional<TagEntity> optional = tagRepository.findById(id);
        if (optional.isEmpty()) {
            log.warn("Id Not Found: {}", id);
            throw new AppBadRequestException("Id Not Found");
        }
        TagEntity tag = optional.get();
        return toDTO(tag);
    }

    public String delete(Integer id) {
        Optional<TagEntity> optional = tagRepository.findById(id);
        if (optional.isEmpty()) {
            log.warn("Id Not Found: {}", id);
            throw new AppBadRequestException("Id Not Found");
        }
        TagEntity entity = optional.get();
        tagRepository.delete(entity);
        return "Success";
    }

    public TagEntity get(Integer id){
        return tagRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Tag Not Found"));
    }

    private TagDTO toDTO(TagEntity entity){
        TagDTO dto = new TagDTO();
        dto.setName(entity.getName());
        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setUpdatedDate(entity.getUpdatedDate());
        return dto;
    }

    public TagDTO toDTOForVideoTag(Integer tagId){
        TagEntity entity = get(tagId);
        TagDTO dto = new TagDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }
}
