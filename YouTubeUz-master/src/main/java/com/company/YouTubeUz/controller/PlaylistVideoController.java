package com.company.YouTubeUz.controller;

import com.company.YouTubeUz.dto.PlaylistVideoDTO;
import com.company.YouTubeUz.enums.ProfileRole;
import com.company.YouTubeUz.service.PlaylistVideoService;
import com.company.YouTubeUz.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("playlist_video")
@Api(tags = "Playlist Video")
public class PlaylistVideoController {
    @Autowired
    private PlaylistVideoService playlistVideoService;

    @ApiOperation(value = "Create" ,notes = "Method used for create Playlist Video", nickname = "Bilol")
    @PostMapping("/public")
    public ResponseEntity<PlaylistVideoDTO> create(@RequestBody PlaylistVideoDTO dto,
                                    HttpServletRequest request){
        Integer pId = JwtUtil.getIdFromHeader(request);
        return ResponseEntity.ok(playlistVideoService.create(pId, dto));
    }

    @ApiOperation(value = "Create" ,notes = "Method used for create Playlist Video", nickname = "Bilol")
    @PutMapping("/public")
    public ResponseEntity<String> update(@RequestBody PlaylistVideoDTO dto,
                                    HttpServletRequest request){
        Integer pId = JwtUtil.getIdFromHeader(request);
        return ResponseEntity.ok(playlistVideoService.update(pId, dto));
    }

    @ApiOperation(value = "Delete", notes = "Method used for deleted playlistVideo ", nickname = "Bilol")
    @DeleteMapping("/public")
    public ResponseEntity<String> delete(@RequestParam(value = "playlistId") Integer playlistId,
                                    @RequestParam(value = "videoId") Integer videoId,
                                    HttpServletRequest request){
        Integer pId = JwtUtil.getIdFromHeader(request, ProfileRole.USER);
        return ResponseEntity.ok(playlistVideoService.delete(pId, playlistId, videoId));
    }

    @ApiOperation(value = "Get By Playlist Id", notes = "Method used for Get By Playlist Id ", nickname = "Bilol")
    @GetMapping("/public/{playlistId}")
    public ResponseEntity<List<PlaylistVideoDTO>> getByPlaylistId(@PathVariable("playlistId") Integer playlistId,
                                                                  HttpServletRequest request){
        JwtUtil.getIdFromHeader(request);
        return ResponseEntity.ok(playlistVideoService.getByPlaylistId(playlistId));
    }


}
