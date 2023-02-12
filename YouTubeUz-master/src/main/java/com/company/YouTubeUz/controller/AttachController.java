package com.company.YouTubeUz.controller;

import com.company.YouTubeUz.dto.AttachDTO;
import com.company.YouTubeUz.enums.ProfileRole;
import com.company.YouTubeUz.service.AttachService;
import com.company.YouTubeUz.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/attach")
@Api(tags = "Attach")
public class AttachController {
    @Autowired
    private AttachService attachService;

    @ApiOperation(value = "Upload", notes = "Method used for upload MultipartFile", nickname = "Bilol")
    @PostMapping("/upload")
    public ResponseEntity<AttachDTO> create(@RequestParam("file") MultipartFile file) {
        log.info("Upload MultipartFile {}",file);
        return ResponseEntity.ok(attachService.upload(file));
    }

    @ApiOperation(value = "Open general", notes = "Method used for open files", nickname = "Bilol")
    @GetMapping(value = "/open_general/{fileName}", produces = MediaType.ALL_VALUE)
    public byte[] open_general(@PathVariable("fileName") String fileName) {
        return attachService.open_general(fileName);
    }

    @ApiOperation(value = "Download", notes = "Method used for download MultipartFile", nickname = "Bilol")
    @GetMapping("/download/{fileName}")
    public ResponseEntity<?> download(@PathVariable("fileName") String fileName) {
        return attachService.download(fileName);
    }

    @ApiOperation(value = "List", notes = "Method used for all attach list for Admin", nickname = "Bilol")
    @GetMapping("/adm/list")
    public ResponseEntity<List<AttachDTO>> list(@RequestParam(value = "page", defaultValue = "0") int page,
                                     @RequestParam(value = "size", defaultValue = "3") int size,
                                     HttpServletRequest request) {
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(attachService.paginationList(page, size));
    }

    @ApiOperation(value = "Delete", notes = "Method used for delete MultipartFile for Admin", nickname = "Bilol")
    @DeleteMapping("/adm/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("id") String key, HttpServletRequest request) {
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(attachService.delete(key));
    }
}
