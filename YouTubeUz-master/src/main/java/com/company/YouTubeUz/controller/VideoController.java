package com.company.YouTubeUz.controller;

import com.company.YouTubeUz.dto.*;
import com.company.YouTubeUz.enums.ProfileRole;
import com.company.YouTubeUz.enums.VideoStatus;
import com.company.YouTubeUz.service.VideoService;
import com.company.YouTubeUz.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/video")
@Api(tags = "Video")
public class VideoController {
    @Autowired
    private VideoService videoService;
    @ApiOperation(value = "Create", notes = "Method used for create Video", nickname = "Bilol")
    @PostMapping("/public") //1
    public ResponseEntity<VideoShortInfoDTO> create(@RequestBody VideoDTO dto,
                                                    HttpServletRequest request){
        JwtUtil.getIdFromHeader(request, ProfileRole.USER);
        return ResponseEntity.ok(videoService.create(dto));
    }

    @ApiOperation(value = "Update", notes = "Method used for update Video", nickname = "Bilol")
    @PutMapping("/public/{id}")//2
    public ResponseEntity<String> update(@PathVariable("id") Integer videoId,
                                    @RequestBody UpdateVideoDTO dto,
                                    HttpServletRequest request){
        Integer pId = JwtUtil.getIdFromHeader(request, ProfileRole.USER);
        return ResponseEntity.ok(videoService.updateVideoDetail(videoId, dto, pId));
    }

    @ApiOperation(value = "Update", notes = "Method used for update video Status", nickname = "Bilol")
    @PutMapping("/public/status/{id}")//3
    public ResponseEntity<String> updateStatus(@PathVariable("id") Integer videoId,
                                          @RequestParam VideoStatus status,
                                          HttpServletRequest request){
        Integer pId = JwtUtil.getIdFromHeader(request, ProfileRole.USER);
        return ResponseEntity.ok(videoService.updateStatus(videoId, status, pId));
    }

    @ApiOperation(value = "Delete", notes = "Method used for delete Video", nickname = "Bilol")
    @DeleteMapping("/public/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id")Integer videoId,
                                    HttpServletRequest request){
        ProfileJwtDTO jwtDTO = JwtUtil.getProfileFromHeader(request);
        return ResponseEntity.ok(videoService.delete(videoId, jwtDTO.getId(), jwtDTO.getRole()));
    }
    @ApiOperation(value = "Increase Count", notes = "Method used for increase video count", nickname = "Bilol")
    @GetMapping("/public/view/{id}")//4
    public ResponseEntity<?> increaseViewCount(@PathVariable("id") Integer id) {
        videoService.updateViewCount(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Get by Category Id", notes = "Method used for Get by Category Id", nickname = "Bilol")
    @GetMapping("/public/category/{id}") //5
    public ResponseEntity<PageImpl<VideoShortInfoDTO>> getByCategoryId(@PathVariable("id")Integer categoryId,
                                                              @RequestParam(value = "page", defaultValue = "0") int page,
                                                              @RequestParam(value = "size", defaultValue = "5") int size,
                                                              HttpServletRequest request){
        JwtUtil.getIdFromHeader(request);
        return ResponseEntity.ok(videoService.getByCategoryId(categoryId, page, size));
    }

    @ApiOperation(value = "Get by Title", notes = "Method used for Get by Title", nickname = "Bilol")
    @GetMapping("/public/title/{title}") //6
    public ResponseEntity<List<VideoShortInfoDTO>> getByTitle(@PathVariable("title")String title,
                                                     HttpServletRequest request){
        JwtUtil.getIdFromHeader(request);
        return ResponseEntity.ok(videoService.getByTitle(title));
    }

    @ApiOperation(value = "Get by Tag Id", notes = "Method used for Get by Tag Id", nickname = "Bilol")
    @GetMapping("/public/tag/{id}") //7
    public ResponseEntity<PageImpl<VideoShortInfoDTO>> getByTagId(@PathVariable("id")Integer tagId,
                                                         @RequestParam(value = "page", defaultValue = "0") int page,
                                                         @RequestParam(value = "size", defaultValue = "5") int size,
                                                         HttpServletRequest request){
        JwtUtil.getIdFromHeader(request);
        return ResponseEntity.ok(videoService.getByTagId(tagId, page, size));
    }

    @GetMapping("/public/videoKey/{key}")
    public ResponseEntity<VideoFullInfoDTO> getVideoByKey(@PathVariable("key") String key,
                                                          HttpServletRequest request){
        ProfileJwtDTO jwtDTO = JwtUtil.getProfileFromHeader(request);
        return ResponseEntity.ok(videoService.getByVideoKey(key, jwtDTO.getId(), jwtDTO.getRole()));
    }

    @ApiOperation(value = "Video List", notes = "Method used for get all video list for admin", nickname = "Bilol")
    @GetMapping("/adm") // 9
    public ResponseEntity<PageImpl<VideoDTOforAdmin>> getAllVideos(HttpServletRequest request,
                                                                   @RequestParam(value = "page", defaultValue = "0") int page,
                                                                   @RequestParam(value = "size", defaultValue = "5") int size
                                          ){
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(videoService.getAllVideos(page, size));
    }





}
