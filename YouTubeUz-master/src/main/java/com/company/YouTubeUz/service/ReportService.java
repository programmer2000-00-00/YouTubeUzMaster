package com.company.YouTubeUz.service;

import com.company.YouTubeUz.dto.ReportDTO;
import com.company.YouTubeUz.entity.ReportEntity;
import com.company.YouTubeUz.enums.ReportType;
import com.company.YouTubeUz.exp.ItemNotFoundException;
import com.company.YouTubeUz.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class ReportService {
    @Autowired
    private ReportRepository reposrtRepository;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private VideoService videoService;
    @Autowired
    private ProfileService profileService;
    public ReportDTO create(Integer pId, ReportDTO dto) {
        ReportEntity entity = new ReportEntity();
        entity.setContent(dto.getContent());
        entity.setProfileId(pId);
        if (dto.getType().equals(ReportType.CHANNEL)){
            channelService.get(dto.getToId());
            entity.setToId(dto.getToId());
        } else if (dto.getType().equals(ReportType.VIDEO)){
            videoService.getById(dto.getToId());
            entity.setToId(dto.getToId());
        }
        reposrtRepository.save(entity);
        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    public PageImpl<ReportDTO> getListPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));
        List<ReportDTO> dtoList = new ArrayList<>();
        Page<ReportEntity> pageList = reposrtRepository.findAll(pageable);
        if (pageList.isEmpty()){
            return new PageImpl<>(new LinkedList<>());
        }
        for (ReportEntity reportEntity : pageList){
            dtoList.add(reportInfo(reportEntity));
        }
        return new PageImpl<ReportDTO>(dtoList, pageable, pageList.getTotalElements());
    }

    public ReportDTO getByReportId(Integer reportId) {
        Optional<ReportEntity> optional = reposrtRepository.findById(reportId);
        if (optional.isEmpty()){
            throw new ItemNotFoundException("Report Not Found");
        }
        return reportInfo(optional.get());
    }

    public PageImpl<ReportDTO> getByProfileId(Integer profileId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));
        List<ReportDTO> dtoList = new ArrayList<>();
        Page<ReportEntity > pageList = reposrtRepository.findByProfileId(profileId, pageable);
        if (pageList.isEmpty()){
            return new PageImpl<>(new LinkedList<>());
        }
        for (ReportEntity commentEntity : pageList){
            dtoList.add(reportInfo(commentEntity));
        }
        return new PageImpl<ReportDTO>(dtoList, pageable, pageList.getTotalElements());
    }

    public ReportDTO reportInfo(ReportEntity entity){
        ReportDTO dto = new ReportDTO();
        dto.setId(entity.getId());
        dto.setProfileDTO(profileService.toDTOForReport(entity.getProfileId()));
        dto.setContent(entity.getContent());
        dto.setToId(entity.getToId());
        dto.setType(entity.getType());
        return dto;
    }
}
