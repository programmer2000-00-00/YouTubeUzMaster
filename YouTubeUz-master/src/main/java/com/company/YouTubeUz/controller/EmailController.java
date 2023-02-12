package com.company.YouTubeUz.controller;

import com.company.YouTubeUz.dto.EmailDTO;
import com.company.YouTubeUz.enums.ProfileRole;
import com.company.YouTubeUz.service.EmailService;
import com.company.YouTubeUz.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/email")
@Api(tags = "Email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @ApiOperation(value = "Email List", notes = "Method used for email list for admin", nickname = "Bilol")
    @GetMapping("/adm/list")
    public ResponseEntity<List<EmailDTO>> list(@RequestParam(value = "page", defaultValue = "0") int page,
                                               @RequestParam(value = "size", defaultValue = "5") int size,
                                               HttpServletRequest request) {
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(emailService.paginationList(page, size));
    }

    @ApiOperation(value = "Delete Email", notes = "Method used for delete email", nickname = "Bilol")
    @DeleteMapping("/adm/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("id") Integer id, HttpServletRequest request) {
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(emailService.delete(id));
    }
}
