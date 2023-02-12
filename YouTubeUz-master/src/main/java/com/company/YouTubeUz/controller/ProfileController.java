package com.company.YouTubeUz.controller;

import com.company.YouTubeUz.dto.*;
import com.company.YouTubeUz.enums.ProfileRole;
import com.company.YouTubeUz.service.ProfileService;
import com.company.YouTubeUz.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/profile")
@Api(tags = "Profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @ApiOperation(value = "Create", notes = "Method used for create profile", nickname = "Bilol")
    @PostMapping("/adm")
    public ResponseEntity<ProfileDTO> create(@RequestBody @Valid ProfileDTO dto,
                                    HttpServletRequest request){
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        log.info("Create Profile: {}", dto);
        return ResponseEntity.ok(profileService.create(dto));
    }

    @ApiOperation(value = "Update", notes = "Method used for update profile", nickname = "Bilol")
    @PutMapping("/adm/{id}")
    public ResponseEntity<ProfileDTO> update(@PathVariable("id") Integer profileId,
                                    @RequestBody @Valid ProfileDTO dto,
                                    HttpServletRequest request) {
        JwtUtil.getProfileFromHeader(request, ProfileRole.ADMIN);
        log.info("Update Profile: {}", dto);
        return ResponseEntity.ok(profileService.update(profileId, dto));
    }

    @ApiOperation(value = "Update Password", notes = "Method used for update profile password", nickname = "Bilol")
    @PutMapping("/changePswd")
    public ResponseEntity<ProfileDTO> changePassword(@RequestBody ChangePasswordDTO dto,
                                            HttpServletRequest request){
        Integer id = JwtUtil.getIdFromHeader(request);
        return ResponseEntity.ok(profileService.changePassword(id,dto));

    }

    @ApiOperation(value = "Change Email", notes = "Method used for change profile email", nickname = "Bilol")
    @PutMapping("/changeEmail")
    public ResponseEntity<String> changeEmail(@RequestBody ResetEmailDTO email,
                                         HttpServletRequest request){
        Integer id = JwtUtil.getIdFromHeader(request);
        return ResponseEntity.ok(profileService.changeEmail(email.getEmail(),id));
    }

    @ApiOperation(value = "Get By Id", notes = "Method used for get by profile id for admin", nickname = "Bilol")
    @GetMapping("/adm/{id}")
    public ResponseEntity<ProfileDTO> getById(@PathVariable("id") Integer id,
                                     HttpServletRequest request){
           JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
           return ResponseEntity.ok(profileService.getById(id));
    }

    @ApiOperation(value = "Get All", notes = "Method used for get profile list for admin", nickname = "Bilol")
    @GetMapping("/adm/list")
    public ResponseEntity<List<ProfileDTO>> getAll(@RequestParam(value = "page", defaultValue = "0") int page,
                                                   @RequestParam(value = "size", defaultValue = "5") int size,
                                                   HttpServletRequest request){
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(profileService.getAll(page, size));
    }

    @ApiOperation(value = "Delete", notes = "Method used for delete profile ", nickname = "Bilol")
    @DeleteMapping("/adm/{id}")
    public ResponseEntity<String> delete(@PathVariable("id")Integer profileId,
                                    HttpServletRequest request){
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(profileService.delete(profileId));
    }

    @ApiOperation(value = "Update Image", notes = "Method used for update profile image", nickname = "Bilol")
    @PostMapping("/image/{imageId}")
    public ResponseEntity<?> updateImage(@PathVariable("imageId") String image,
                                         HttpServletRequest request) {
        Integer pId = JwtUtil.getIdFromHeader(request);
        try {
            return ResponseEntity.ok(profileService.updateImage(image, pId));
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            log.warn("Attach Not Found: {}", image);
            return ResponseEntity.badRequest().body("Attach not found");
        }
    }
}
