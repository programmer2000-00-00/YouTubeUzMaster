package com.company.YouTubeUz.controller;

import com.company.YouTubeUz.dto.AttachDTO;
import com.company.YouTubeUz.dto.ChannelDTO;
import com.company.YouTubeUz.dto.ProfileJwtDTO;
import com.company.YouTubeUz.enums.ChannelStatus;
import com.company.YouTubeUz.enums.ProfileRole;
import com.company.YouTubeUz.service.ChannelService;
import com.company.YouTubeUz.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/channel")
@Api(tags = "Channel")
public class ChannelController {
    @Autowired
    private ChannelService channelService;

    @ApiOperation(value = "Create", notes = "Method used for create Channel", nickname = "Bilol")
    @PostMapping("/public")
    public ResponseEntity<ChannelDTO> create(@RequestBody @Valid ChannelDTO dto,
                                    HttpServletRequest request){
        log.info("Channel Created: {}", dto);
        Integer pId = JwtUtil.getIdFromHeader(request, ProfileRole.USER);
        return ResponseEntity.ok(channelService.create(dto, pId));
    }
    @ApiOperation(value = "Update", notes = "Method used for update Channel", nickname = "Bilol")
    @PutMapping("/public/{key}")
    public ResponseEntity<String> update(@PathVariable("key") String key,
                                    @RequestBody ChannelDTO dto,
                                    HttpServletRequest request){
        Integer pId = JwtUtil.getIdFromHeader(request, ProfileRole.USER);
        return ResponseEntity.ok(channelService.update(key, dto, pId));
    }

    @ApiOperation(value = "Update Photo", notes = "Method used for update Channel Foto", nickname = "Bilol")

     @PostMapping("/public/photo/{key}/{attachId}")
    public ResponseEntity<String> updatePhoto(@PathVariable("key") String key,
                                         @PathVariable("attachId") String attachId,
                                         HttpServletRequest request){
        Integer pId = JwtUtil.getIdFromHeader(request, ProfileRole.USER);
        return ResponseEntity.ok(channelService.updatePhoto(key, attachId, pId));
    }

    @ApiOperation(value = "Update Banner", notes = "Method used for update Channel Banner", nickname = "Bilol")
    @PutMapping("/public/banner/{key}/{bannerId}")
    public ResponseEntity<String> updateBanner(@PathVariable("key") String key,
                                               @PathVariable("bannerId") String bannerId,
                                         HttpServletRequest request){
        Integer pId = JwtUtil.getIdFromHeader(request, ProfileRole.USER);
        return ResponseEntity.ok(channelService.updateBanner(key, bannerId, pId));
    }
    @ApiOperation(value = "Update Status", notes = "Method used for update Channel Status", nickname = "Bilol")
    @PutMapping("/public/{key}/status")
    public ResponseEntity<String> updateStatus(@PathVariable("key") String key,
                                          @RequestParam ChannelStatus status,
                                          HttpServletRequest request){
        ProfileJwtDTO pId = JwtUtil.getProfileFromHeader(request);
        return ResponseEntity.ok(channelService.updateStatus(key, status, pId.getId(), pId.getRole()));
    }

    @ApiOperation(value = "List for Admin", notes = "Method used for channel list for admin", nickname = "Bilol")
    @GetMapping("/adm")
    public ResponseEntity<PageImpl<ChannelDTO>> getListForAdmin(@RequestParam(value = "page") int page,
                                                                @RequestParam(value = "size") int size,
                                                                HttpServletRequest request){
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(channelService.getListForAdmin(page, size));
    }
    @ApiOperation(value = "List for user", notes = "Method used for channel list for user", nickname = "Bilol")
    @GetMapping("/public")
    public ResponseEntity<List<ChannelDTO>> getListForUser(HttpServletRequest request){
       Integer pId = JwtUtil.getIdFromHeader(request, ProfileRole.USER);
       return ResponseEntity.ok(channelService.getListForUser(pId));
    }

}
