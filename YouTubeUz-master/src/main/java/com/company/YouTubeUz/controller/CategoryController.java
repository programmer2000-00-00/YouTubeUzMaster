package com.company.YouTubeUz.controller;

import com.company.YouTubeUz.dto.CategoryDTO;
import com.company.YouTubeUz.enums.ProfileRole;
import com.company.YouTubeUz.service.CategoryService;
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
@RequestMapping("/category")
@Api(tags = "Category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @ApiOperation(value = "Create", notes = "Method used for create Category", nickname = "Bilol")
    @PostMapping("/adm")
    public ResponseEntity<CategoryDTO> create(@RequestBody @Valid CategoryDTO dto, HttpServletRequest request) {
        log.info("Create Category : {}", dto);
        return ResponseEntity.ok(categoryService.create(dto, JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN)));
    }

    @ApiOperation(value = "Update", notes = "Method used for update Category", nickname = "Bilol")
    @PutMapping("/adm/{id}")
    public ResponseEntity<String> update(@PathVariable("id") Integer id, @RequestBody @Valid CategoryDTO dto, HttpServletRequest request) {
        log.info("Update Category : {}", dto);
        Integer pId = JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(categoryService.update(id, dto, pId));
    }

    @ApiOperation(value = "Find All", notes = "Method used for all Category ", nickname = "Bilol")
    @GetMapping("/public/pagination")
    public ResponseEntity<PageImpl<CategoryDTO>> findAll(@RequestParam(value = "page", defaultValue = "0") int page,
                                                         @RequestParam(value = "size", defaultValue = "3") int size) {
        return ResponseEntity.ok(categoryService.getList(page, size));
    }
    @ApiOperation(value = "Get By Id", notes = "Method used for get by Category Id", nickname = "Bilol")
    @GetMapping("/public/{id}")
    public ResponseEntity<CategoryDTO> getById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(categoryService.getById(id));
    }
    @ApiOperation(value = "Delete", notes = "Method used for delete Category", nickname = "Bilol")
    @DeleteMapping("/adm/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(categoryService.delete(id));
    }
}
