package com.company.YouTubeUz.controller;

import com.company.YouTubeUz.dto.AuthDTO;
import com.company.YouTubeUz.dto.JwtDTO;
import com.company.YouTubeUz.dto.ProfileDTO;
import com.company.YouTubeUz.dto.RegistrationDTO;
import com.company.YouTubeUz.service.AuthService;
import com.company.YouTubeUz.service.ProfileService;
import com.company.YouTubeUz.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/auth")
@Api(tags = "Auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private ProfileService profileService;

    @ApiOperation(value = "Login", notes = "Method used for login and getting token", nickname = "Bilol")
    @PostMapping("/login")
    public ResponseEntity<ProfileDTO> create(@RequestBody @Valid AuthDTO dto) {
        log.info("Authtorization : {}", dto);
        return ResponseEntity.ok(authService.login(dto));
    }

    @ApiOperation(value = "Registration", notes = "Method used for registration ", nickname = "Bilol")
    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody @Valid RegistrationDTO dto) {
        log.info("Registration : {}", dto);
        authService.registration(dto);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Verification", notes = "Method used for verification", nickname = "Bilol")
    @GetMapping("/verification/{jwt}")
    public ResponseEntity<?> verification(@PathVariable("jwt") String jwt) {
        authService.verification(jwt);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Change Email", notes = "Method used for change Email and activate", nickname = "Bilol")
    @GetMapping("/activate/{jwt}")
    public ResponseEntity<String> activateEmail(@PathVariable("jwt") String jwt){
        JwtDTO dto = JwtUtil.decodeJwtDto(jwt);
        return ResponseEntity.ok(profileService.changeEmail(dto));
    }
}
