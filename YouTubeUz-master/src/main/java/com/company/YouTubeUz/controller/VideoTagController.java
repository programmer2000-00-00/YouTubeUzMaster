package com.company.YouTubeUz.controller;

import com.company.YouTubeUz.dto.VideoTagDTO;
import com.company.YouTubeUz.enums.ProfileRole;
import com.company.YouTubeUz.service.VideoTagService;
import com.company.YouTubeUz.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("video_tag")
@Api(tags = "Video Tag")
public class VideoTagController {
    @Autowired
    private VideoTagService videoTagService;

    @ApiOperation(value = "Create", notes = "Method used for create video_tag", nickname = "Bilol")
    @PostMapping("/public")
    public ResponseEntity<VideoTagDTO> create(@RequestBody VideoTagDTO dto,
                                    HttpServletRequest request){
        Integer pId = JwtUtil.getIdFromHeader(request, ProfileRole.USER);
        return ResponseEntity.ok(videoTagService.create(pId, dto));
    }
    @ApiOperation(value = "Delete", notes = "Method used for delete video_tag", nickname = "Bilol")
    @DeleteMapping("/public/{videoId}")
    public ResponseEntity<String> delete(@PathVariable("videoId") Integer videoId,
                                    HttpServletRequest request){
        Integer pId = JwtUtil.getIdFromHeader(request, ProfileRole.USER);
        return ResponseEntity.ok(videoTagService.delete(pId, videoId));
    }
    @ApiOperation(value = "Get By Id", notes = "Method used for get by id 2 video_tag", nickname = "Bilol")
    @GetMapping("/public/{videoId}")
    public ResponseEntity<List<VideoTagDTO>> getById(@PathVariable("videoId") Integer videoId,
                                                     HttpServletRequest request){
        JwtUtil.getIdFromHeader(request, ProfileRole.USER);
        return ResponseEntity.ok(videoTagService.getByVideoId(videoId));
    }
}
