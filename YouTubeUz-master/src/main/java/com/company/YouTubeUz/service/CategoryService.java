package com.company.YouTubeUz.service;

import com.company.YouTubeUz.dto.CategoryDTO;
import com.company.YouTubeUz.entity.CategoryEntity;
import com.company.YouTubeUz.entity.ProfileEntity;
import com.company.YouTubeUz.enums.ProfileRole;
import com.company.YouTubeUz.exp.AppBadRequestException;
import com.company.YouTubeUz.exp.AppForbiddenException;
import com.company.YouTubeUz.exp.CategoryAlredyExistsException;
import com.company.YouTubeUz.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CategoryService {
    @Autowired
    private ProfileService profileService;
    @Autowired
    private CategoryRepository categoryRepository;
    public CategoryDTO create(CategoryDTO dto, Integer pId) {
        ProfileEntity profile = profileService.get(pId);
        if (!profile.getRole().equals(ProfileRole.ADMIN)) {
            log.warn("Not access");
            throw new AppForbiddenException("Not access");
        }
        CategoryEntity category = categoryRepository.findByName(dto.getName());
        if (category != null) {
            log.warn("Category Already Exists: {}", dto);
            throw new CategoryAlredyExistsException("Category Already Exists");
        }
        CategoryEntity entity = new CategoryEntity();
        entity.setName(dto.getName());
        categoryRepository.save(entity);
        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }
    public String update(Integer id, CategoryDTO dto, Integer pId) {
        ProfileEntity profile = profileService.get(pId);
        if (!profile.getRole().equals(ProfileRole.ADMIN)) {
            log.warn("Not access");
            throw new AppForbiddenException("Not access");
        }
        Optional<CategoryEntity> optional = categoryRepository.findById(id);
        if (optional.isEmpty()) {
            log.warn("Id Not Found: {}", id);
            throw new AppBadRequestException("Id Not Found");
        }
        CategoryEntity entity = categoryRepository.findByName(dto.getName());
        if (entity != null) {
            log.warn("Category Already Exists: {}", dto);
            throw new CategoryAlredyExistsException("Category alredy exists");
        }
        CategoryEntity category = optional.get();
        category.setName(dto.getName());

        categoryRepository.save(category);
        return "Success";
    }
    public PageImpl<CategoryDTO> getList(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<CategoryEntity> pagination = categoryRepository.findAll(pageable);

        List<CategoryEntity> profileEntityList = pagination.getContent();
        long totalElement = pagination.getTotalElements();
        List<CategoryDTO> dtoList = profileEntityList.stream().map(this::toDTO).toList();
        return new PageImpl<CategoryDTO>(dtoList, pageable, totalElement);
    }

    public CategoryDTO getById(Integer id) {
        Optional<CategoryEntity> optional = categoryRepository.findById(id);
        if (optional.isEmpty()) {
            log.warn("Id Not Found: {}", id);
            throw new AppBadRequestException("Id Not Found");
        }
        CategoryEntity category = optional.get();
        return toDTO(category);
    }

    public String delete(Integer id) {
        Optional<CategoryEntity> optional = categoryRepository.findById(id);
        if (optional.isEmpty()) {
            log.warn("Id Not Found: {}", id);
            throw new AppBadRequestException("Id Not Found");
        }
        CategoryEntity entity = optional.get();
        categoryRepository.delete(entity);
        return "Success";
    }

    private CategoryDTO toDTO(CategoryEntity entity){
        CategoryDTO dto = new CategoryDTO();
        dto.setName(entity.getName());
        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setUpdatedDate(entity.getUpdatedDate());
        return dto;
    }
}
