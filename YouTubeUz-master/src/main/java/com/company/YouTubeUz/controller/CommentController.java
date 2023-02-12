package com.company.YouTubeUz.controller;

import com.company.YouTubeUz.dto.CommentDTO;
import com.company.YouTubeUz.dto.ProfileJwtDTO;
import com.company.YouTubeUz.dto.UpdateCommentDTO;
import com.company.YouTubeUz.enums.ProfileRole;
import com.company.YouTubeUz.service.CommentService;
import com.company.YouTubeUz.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/comment")
@Api(tags = "Comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @ApiOperation(value = "Create", notes = "Method used for create comment", nickname = "Bilol")
    @PostMapping("/public")
    public ResponseEntity<CommentDTO> create(@RequestBody @Valid CommentDTO dto,
                                     HttpServletRequest request){
        Integer pId = JwtUtil.getIdFromHeader(request, ProfileRole.USER);
        return ResponseEntity.ok(commentService.create(pId, dto));
    }

    @ApiOperation(value = "Update", notes = "Method used for update comment", nickname = "Bilol")
    @PutMapping("/public/{commentId}")
    public ResponseEntity<String> update(@PathVariable("commentId") Integer commentId,
                                     @RequestBody UpdateCommentDTO dto,
                                     HttpServletRequest request){
        Integer pId = JwtUtil.getIdFromHeader(request, ProfileRole.USER);
        return ResponseEntity.ok(commentService.update(pId, commentId, dto));
    }
    @ApiOperation(value = "Delete", notes = "Method used for delete comment", nickname = "Bilol")
    @DeleteMapping("/public/{commentId}")
    public ResponseEntity<String> delete(@PathVariable("commentId") Integer commentId,
                                    HttpServletRequest request){
        ProfileJwtDTO jwtDTO = JwtUtil.getProfileFromHeader(request);
        return ResponseEntity.ok(commentService.delete(commentId, jwtDTO.getId(), jwtDTO.getRole()));
    }
    @ApiOperation(value = "Comment List", notes = "Method used for get comment list", nickname = "Bilol")
    @GetMapping("/public")
    public ResponseEntity<PageImpl<CommentDTO>> getCommentList(HttpServletRequest request,
                                                               @RequestParam(value = "page", defaultValue = "0") int page,
                                                               @RequestParam(value = "size", defaultValue = "5") int size){
        Integer pId = JwtUtil.getIdFromHeader(request, ProfileRole.USER);
        return ResponseEntity.ok(commentService.getListForProfile(pId, page,size));
    }
    @ApiOperation(value = "Comment pagination List", notes = "Method used for get comment pagination list for admin", nickname = "Bilol")
    @GetMapping("/adm/pagination")
    public ResponseEntity<PageImpl<CommentDTO>> paginationList(HttpServletRequest request,
                                            @RequestParam(value = "page", defaultValue = "0") int page,
                                            @RequestParam(value = "size", defaultValue = "5") int size){
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(commentService.getPagination(page, size));
    }

    @ApiOperation(value = "Comment List by Profile", notes = "Method used for get comment list by profile id", nickname = "Bilol")
    @GetMapping("/adm/{profileId}")
    public ResponseEntity<PageImpl<CommentDTO>> getListByProfileId(@PathVariable("profileId")Integer profileId,
                                                @RequestParam(value = "page", defaultValue = "0") int page,
                                                @RequestParam(value = "size", defaultValue = "5") int size,
                                                HttpServletRequest request){
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(commentService.getListByProfileId(profileId, page, size));
    }

    @ApiOperation(value = "Comment List by Video", notes = "Method used for get comment list by video id", nickname = "Bilol")
    @GetMapping("/public/videoId/{videoId}")
    public ResponseEntity<PageImpl<CommentDTO>> getByVideoId(@PathVariable("videoId") Integer videoId,
                                          @RequestParam(value = "page", defaultValue = "0") int page,
                                          @RequestParam(value = "size", defaultValue = "5") int size,
                                          HttpServletRequest request){
        JwtUtil.getIdFromHeader(request);
        return ResponseEntity.ok(commentService.getByVideoId(videoId, page, size));
    }

    @ApiOperation(value = "Reply comment list by Comment", notes = "Method used for get reply comment list by comment id", nickname = "Bilol")
    @GetMapping("/public/comment/{commentId}")
    public ResponseEntity<PageImpl<CommentDTO>> getReplyCommentsByCommentId(@PathVariable("commentId")Integer commentId,
                                                         @RequestParam(value = "page", defaultValue = "0") int page,
                                                         @RequestParam(value = "size", defaultValue = "5") int size,
                                                         HttpServletRequest request){
        JwtUtil.getIdFromHeader(request);
        return ResponseEntity.ok(commentService.getReplyCommentsByCommentId(commentId, page, size));
    }
}
