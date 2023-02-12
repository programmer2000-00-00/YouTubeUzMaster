package com.company.YouTubeUz.controller;

import com.company.YouTubeUz.dto.TagDTO;
import com.company.YouTubeUz.enums.ProfileRole;
import com.company.YouTubeUz.service.TagService;
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

@Slf4j
@RestController
@RequestMapping("/tag")
@Api(tags = "Tag")
public class TagController {
    @Autowired
    private TagService tagService;

    @ApiOperation(value = "Create", notes = "Method used for create Tag", nickname = "Bilol")
    @PostMapping("/public")
    public ResponseEntity<TagDTO> create(@RequestBody @Valid TagDTO dto, HttpServletRequest request) {
        log.info("Create Tag : {}", dto);
        JwtUtil.getIdFromHeader(request);
        return ResponseEntity.ok(tagService.create(dto));
    }
    @ApiOperation(value = "Update", notes = "Method used for update Tag", nickname = "Bilol")
    @PutMapping("/adm/{id}")
    public ResponseEntity<String> update(@PathVariable("id") Integer id, @RequestBody @Valid TagDTO dto, HttpServletRequest request) {
        log.info("Update Tag : {}", dto);
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(tagService.update(id, dto));
    }
    @ApiOperation(value = "Find All", notes = "Method used for all Tag", nickname = "Bilol")
    @GetMapping("/public/pagination")
    public ResponseEntity<PageImpl<TagDTO>> findAll(@RequestParam(value = "page", defaultValue = "0") int page,
                                                    @RequestParam(value = "size", defaultValue = "3") int size) {
        return ResponseEntity.ok(tagService.getList(page, size));
    }
    @ApiOperation(value = "Get By Id", notes = "Method used for get by Tag Id", nickname = "Bilol")
    @GetMapping("/public{id}")
    public ResponseEntity<TagDTO> getById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(tagService.getById(id));
    }
    @ApiOperation(value = "Delete", notes = "Method used for delete Tag", nickname = "Bilol")
    @DeleteMapping("/adm/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(tagService.delete(id));
    }
}
