package com.company.YouTubeUz.service;

import com.company.YouTubeUz.dto.ProfileDTO;
import com.company.YouTubeUz.repository.ProfileRepository;
import com.company.YouTubeUz.dto.AttachDTO;
import com.company.YouTubeUz.dto.AuthDTO;
import com.company.YouTubeUz.dto.RegistrationDTO;
import com.company.YouTubeUz.entity.AttachEntity;
import com.company.YouTubeUz.entity.ProfileEntity;
import com.company.YouTubeUz.enums.ProfileRole;
import com.company.YouTubeUz.enums.ProfileStatus;
import com.company.YouTubeUz.exp.*;
import com.company.YouTubeUz.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class AuthService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private EmailService emailService;

    public ProfileDTO login(AuthDTO dto) {
        String pswd = DigestUtils.md5Hex(dto.getPassword());
        Optional<ProfileEntity> optional =
                profileRepository.findByEmailAndPassword(dto.getEmail(), pswd);
        if (optional.isEmpty()) {
            log.error("Password or email wrong! : {}",  dto);
            throw new PasswordOrEmailWrongException("Password or email wrong!");
        }

        ProfileEntity entity = optional.get();
        if (!entity.getStatus().equals(ProfileStatus.ACTIVE)) {
            log.error("No Access bratishka.");
            throw new AppForbiddenException("No Access bratishka.");
        }

        ProfileDTO profile = new ProfileDTO();

        profile.setEmail(entity.getEmail());
        profile.setName(entity.getName());
        profile.setSurname(entity.getSurname());
        profile.setRole(entity.getRole());
        profile.setJwt(JwtUtil.encode(entity.getId(), entity.getRole()));
        return profile;
    }

    public void registration(RegistrationDTO dto) {

        Optional<ProfileEntity> optional = profileRepository.findByEmail(dto.getEmail());
        if (optional.isPresent()) {
            ProfileEntity entity = optional.get();
            if (!entity.getStatus().equals(ProfileStatus.ACTIVE)){
                profileRepository.delete(entity);
            } else {
                log.error("Email Already Exits : {}" , dto);
                throw new EmailAlreadyExistsException("Email Already Exits");
            }
        }

        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());

        String pswd = DigestUtils.md5Hex(dto.getPassword());
        entity.setPassword(pswd);

        entity.setRole(ProfileRole.USER);
        entity.setStatus(ProfileStatus.NOT_ACTIVE);
        profileRepository.save(entity);

        try {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    sendVerificationEmail(entity);
                }
            };
            thread.start();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public void verification(String jwt) {
        Integer userId = null;
        try {
            userId = JwtUtil.decodeAndGetId(jwt);
        } catch (JwtException e) {
            log.error("Verification not completed: {}" , userId);
            throw new AppBadRequestException("Verification not completed");
        }
        profileRepository.updateStatus(ProfileStatus.ACTIVE, userId);
    }

    private void sendVerificationEmail(ProfileEntity entity) {
        StringBuilder builder = new StringBuilder();
        String jwt = JwtUtil.encode(entity.getId());
        builder.append("Salom bormsin \n");
        builder.append("To verify your registration click to next link.");
        builder.append("http://localhost:8080/auth/verification/").append(jwt);
        builder.append("\nMazgi!");
        emailService.send(entity.getEmail(), "Activate Your Registration", builder.toString());

    }
}
