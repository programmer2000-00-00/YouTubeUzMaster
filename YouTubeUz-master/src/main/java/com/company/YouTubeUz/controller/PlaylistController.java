package com.company.YouTubeUz.controller;

import com.company.YouTubeUz.dto.PlaylistDTO;
import com.company.YouTubeUz.dto.PlaylistInfoForAdminDTO;
import com.company.YouTubeUz.dto.PlaylistShortInfoDTO;
import com.company.YouTubeUz.dto.PlaylistSimpleDTO;
import com.company.YouTubeUz.enums.PlaylistStatus;
import com.company.YouTubeUz.enums.ProfileRole;
import com.company.YouTubeUz.service.PlaylistService;
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
@RequestMapping("/playlist")
@Api(tags = "PlayList")
public class PlaylistController {
    @Autowired
    private PlaylistService playlistService;
    @ApiOperation(value = "Create", notes = "Method used for create playlist", nickname = "Bilol")
    @PostMapping("/public")
    public ResponseEntity<PlaylistDTO> create(@RequestBody @Valid PlaylistDTO dto,
                                    HttpServletRequest request){
       Integer pId = JwtUtil.getIdFromHeader(request, ProfileRole.USER);
        return ResponseEntity.ok(playlistService.create(pId, dto));
    }

    @ApiOperation(value = "Update", notes = "Method used for update playlist", nickname = "Bilol")
    @PutMapping("/public/{id}")
    public ResponseEntity<String> update(@PathVariable("id") Integer id,
                                    @RequestBody @Valid PlaylistDTO dto,
                                    HttpServletRequest request){
        Integer pId = JwtUtil.getIdFromHeader(request, ProfileRole.USER);
        return ResponseEntity.ok(playlistService.update(id, dto, pId));
    }

    @ApiOperation(value = "Update Status", notes = "Method used for update playlist status", nickname = "Bilol")
    @PutMapping("/public/status/{id}")
    public ResponseEntity<String> updateStatus(@PathVariable("id")Integer id,
                                          @RequestParam PlaylistStatus status,
                                          HttpServletRequest request){
        Integer pId = JwtUtil.getIdFromHeader(request, ProfileRole.USER);
        return ResponseEntity.ok(playlistService.updateStauts(id, status, pId));
    }

    @ApiOperation(value = "Delete", notes = "Method used for delete playlist", nickname = "Bilol")
    @DeleteMapping("/public/{id}")
    public ResponseEntity<String> delete(@PathVariable("id")Integer id,
                                    HttpServletRequest request){
        Integer pId = JwtUtil.getIdFromHeader(request, ProfileRole.USER);
        return ResponseEntity.ok(playlistService.delete(id, pId));
    }

    @ApiOperation(value = "Get Pagination List", notes = "Method used for Get Pagination List for admin", nickname = "Bilol")
    @GetMapping("/adm")
    public ResponseEntity<PageImpl<PlaylistInfoForAdminDTO>> getListPagination(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                              @RequestParam(value = "size", defaultValue = "5") int size,
                                                                               HttpServletRequest request){
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(playlistService.getListPagination(page, size));
    }

    @ApiOperation(value = "Get list by user id", notes = "Method used for Get List by user id for admin", nickname = "Bilol")
    @GetMapping("/adm/{userId}")
    public ResponseEntity<PageImpl<PlaylistInfoForAdminDTO>> getListPaginationByUserId(@PathVariable("userId") Integer userId,
                                                       @RequestParam(value = "page", defaultValue = "0") int page,
                                                       @RequestParam(value = "size", defaultValue = "5") int size,
                                                       HttpServletRequest request){
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(playlistService.getListByUserId(userId, page, size));
    }

    @ApiOperation(value = "Get list by user id", notes = "Method used for Get List by user id for admin", nickname = "Bilol")
    @GetMapping("/public/paginationList")
    public ResponseEntity<PageImpl<PlaylistInfoForAdminDTO>> getListPaginationForUser( @RequestParam(value = "page", defaultValue = "0") int page,
                                                       @RequestParam(value = "size", defaultValue = "5") int size,
                                                       HttpServletRequest request){
        Integer pId = JwtUtil.getIdFromHeader(request, ProfileRole.USER);
        return ResponseEntity.ok(playlistService.getListByUserId(pId, page, size));
    }

    @ApiOperation(value = "Get list by channel key", notes = "Method used for Get list by channel key", nickname = "Bilol")
    @GetMapping("/public/{channelKey}")
    public ResponseEntity<PageImpl<PlaylistShortInfoDTO> > getListByChannelKey(@PathVariable("channelKey") String channelKey,
                                                                               @RequestParam(value = "page", defaultValue = "0") int page,
                                                                               @RequestParam(value = "size", defaultValue = "5") int size,
                                                                               HttpServletRequest request){
        Integer pId = JwtUtil.getIdFromHeader(request);
        return ResponseEntity.ok(playlistService.getListByChannelKey(pId, channelKey, page, size));
    }

    @ApiOperation(value = "Get list by playlist id", notes = "Method used for Get list by playlist id", nickname = "Bilol")
    @GetMapping("/public/playlist/{playlistId}")
    public ResponseEntity<PlaylistSimpleDTO> getListByPlaylistId(@PathVariable("playlistId") Integer playlistId,
                                                                 HttpServletRequest request){
        Integer pId = JwtUtil.getIdFromHeader(request, ProfileRole.USER);
        return ResponseEntity.ok(playlistService.getListByPlaylistId(pId, playlistId));
    }


}
