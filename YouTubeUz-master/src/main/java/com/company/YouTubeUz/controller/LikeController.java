package com.company.YouTubeUz.controller;

import com.company.YouTubeUz.dto.CommentLikeDTO;
import com.company.YouTubeUz.dto.VideoLikeDTO;
import com.company.YouTubeUz.dto.ProfileJwtDTO;
import com.company.YouTubeUz.enums.ProfileRole;
import com.company.YouTubeUz.service.LikeService;
import com.company.YouTubeUz.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/like")
@Api(tags = "Like")
public class LikeController {
    @Autowired
    private LikeService likeService;
    /**
     * Video_like
     * */
    @ApiOperation(value = "Create video like", notes = "Method used for create video_like", nickname = "Bilol")
    @PostMapping("/video_like")
    public ResponseEntity<VideoLikeDTO> createVideoLike(@RequestBody VideoLikeDTO dto,
                                    HttpServletRequest request){
        Integer pId = JwtUtil.getIdFromHeader(request);
        return ResponseEntity.ok(likeService.createVideoLike(pId, dto));
    }

    @ApiOperation(value = "Delete Video Like", notes = "Method used for delete video_like", nickname = "Bilol")
    @DeleteMapping("/video_like/delete/{id}")
    public ResponseEntity<String> deleteVideoLike(@PathVariable("id")Integer likeId,
                                    HttpServletRequest request){
        ProfileJwtDTO jwtDTO = JwtUtil.getProfileFromHeader(request);
        return ResponseEntity.ok(likeService.deleteVideoLike(likeId, jwtDTO.getId(), jwtDTO.getRole()));
    }

    @ApiOperation(value = "Get Video Like for User", notes = "Method used for get video_like for user", nickname = "Bilol")
    @GetMapping("/video_like")
    public ResponseEntity<PageImpl<VideoLikeDTO>> getVideoLikeByUserId(HttpServletRequest request,
                                                                       @RequestParam(value = "page", defaultValue = "0") int page,
                                                                       @RequestParam(value = "size", defaultValue = "5") int size){
        Integer pId = JwtUtil.getIdFromHeader(request, ProfileRole.USER);
        return ResponseEntity.ok(likeService.getVideoLikeByUserId(pId, page, size));
    }

    @ApiOperation(value = "Get Video_like List", notes = "Method used for get video_like list", nickname = "Bilol")
    @GetMapping("/adm/video_like/{userId}")
    public ResponseEntity<PageImpl<VideoLikeDTO>> getLikeVideoListByUserId(@PathVariable("userId")Integer userId,
                                                      @RequestParam(value = "page", defaultValue = "0") int page,
                                                      @RequestParam(value = "size", defaultValue = "5") int size,
                                                      HttpServletRequest request){
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(likeService.getLikeVideoByUserId(userId, page, size));
    }

    /**
     * Comment_like
     * */

    @ApiOperation(value = "create comment like", notes = "Method used for create video_like", nickname = "Bilol")
    @PostMapping("/comment_like")
    public ResponseEntity<CommentLikeDTO> createCommentLike(@RequestBody CommentLikeDTO dto,
                                    HttpServletRequest request){
        Integer pId = JwtUtil.getIdFromHeader(request);
        return ResponseEntity.ok(likeService.createCommentLike(pId, dto));
    }

    @ApiOperation(value = "Delete comment like", notes = "Method used for delete video_like", nickname = "Bilol")
    @DeleteMapping("/comment_like/delete/{id}")
    public ResponseEntity<String> deleteCommentLike(@PathVariable("id")Integer likeId,
                                    HttpServletRequest request){
        ProfileJwtDTO jwtDTO = JwtUtil.getProfileFromHeader(request);
        return ResponseEntity.ok(likeService.deleteCommentLike(likeId, jwtDTO.getId(), jwtDTO.getRole()));
    }

    @ApiOperation(value = "Get comment like for user", notes = "Method used for get comment like for user", nickname = "Bilol")
    @GetMapping("/comment_like")
    public ResponseEntity<PageImpl<CommentLikeDTO>> getCommentLikeByUserId(HttpServletRequest request,
                                                  @RequestParam(value = "page", defaultValue = "0") int page,
                                                  @RequestParam(value = "size", defaultValue = "5") int size){
        Integer pId = JwtUtil.getIdFromHeader(request, ProfileRole.USER);
        return ResponseEntity.ok(likeService.getCommentLikeByUserId(pId, page, size));
    }

    @ApiOperation(value = "Get comment like by user id", notes = "Method used for get comment like by user id", nickname = "Bilol")
    @GetMapping("/adm/comment_like/{userId}")
    public ResponseEntity<PageImpl<CommentLikeDTO>> getLikeCommentListByUserId(@PathVariable("userId")Integer userId,
                                                      @RequestParam(value = "page", defaultValue = "0") int page,
                                                      @RequestParam(value = "size", defaultValue = "5") int size,
                                                      HttpServletRequest request){
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(likeService.getLikeCommentByUserId(userId, page, size));
    }
}



