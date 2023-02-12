package com.company.YouTubeUz.service;

import com.company.YouTubeUz.dto.ChangePasswordDTO;
import com.company.YouTubeUz.dto.JwtDTO;
import com.company.YouTubeUz.dto.ProfileDTO;
import com.company.YouTubeUz.entity.ProfileEntity;
import com.company.YouTubeUz.enums.ProfileStatus;
import com.company.YouTubeUz.exp.EmailAlreadyExistsException;
import com.company.YouTubeUz.exp.ItemNotFoundException;
import com.company.YouTubeUz.exp.PasswordOrEmailWrongException;
import com.company.YouTubeUz.repository.ProfileRepository;
import com.company.YouTubeUz.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private AttachService attachService;
    @Autowired
    private EmailService emailService;
    @Value("${server.domain.name}")
    private String domainName;
    public ProfileDTO create(ProfileDTO dto) {
        Optional<ProfileEntity> optional = profileRepository.findByEmail(dto.getEmail());
        if (optional.isPresent()) {
            log.error("Email Already Exits : {}" , dto);
            throw new EmailAlreadyExistsException("Email Already Exits");
        }
        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        String pswd = DigestUtils.md5Hex(dto.getPassword());
        entity.setPassword(pswd);
        entity.setRole(dto.getRole());
        entity.setStatus(ProfileStatus.ACTIVE);
        profileRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    public ProfileDTO update(Integer profileId, ProfileDTO dto) {
        Optional<ProfileEntity> optional = profileRepository.findByEmail(dto.getEmail());
        if (optional.isPresent()) {
            log.error("Email Already Exits : {}" , dto);
            throw new EmailAlreadyExistsException("This Email already used!");
        }

        ProfileEntity entity = profileRepository.findById(profileId).orElseThrow(() -> new ItemNotFoundException("Not Found!"));

        if (!entity.getVisible()) {
            log.error("Not Found!");
            throw new ItemNotFoundException("Not Found!");
        }

        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setPassword(dto.getPassword());
        entity.setRole(dto.getRole());
        entity.setUpdatedDate(LocalDateTime.now());
        profileRepository.save(entity);

        return toDTO(entity);
    }

    public ProfileDTO changePassword(Integer id, ChangePasswordDTO dto){
        ProfileEntity entity = get(id);
        String pswd = DigestUtils.md5Hex(dto.getOldPassword());
        if (!entity.getPassword().equals(pswd)) throw new PasswordOrEmailWrongException("Password wrong");
        String newPswd = DigestUtils.md5Hex(dto.getNewPassword());
        profileRepository.changePassword(id,newPswd);
        return toDTO(entity);
    }

    public ProfileDTO getById(Integer id) {

        ProfileEntity entity = profileRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Not Found!"));
        if (!entity.getVisible()) {
            log.error("Not Found!");
            throw new ItemNotFoundException("Not Found!");
        }
        return toDTO(entity);
    }

    public ProfileEntity get(Integer id) {
        log.error(  "Not found {}{}" , id, ProfileService.class);
        return profileRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Not Found!"));
    }

    public List<ProfileDTO> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));

        List<ProfileDTO> list = new ArrayList<>();
        profileRepository.findByVisible(true, pageable).forEach(profileEntity -> {
            list.add(toDTO(profileEntity));
        });
        if (list.isEmpty()) {
            log.error(  "Not found {}" ,  ProfileService.class);
            throw new ItemNotFoundException("Not Found!");
        }
        return list;
    }

    public String delete(Integer id) {
        ProfileEntity entity = profileRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Not Found!"));

        if (!entity.getVisible()) {
            log.error("Not Found!");
            throw new ItemNotFoundException("Not Found!");
        }

        int n = profileRepository.updateVisible(false, id);
        if (n > 0){
            return "Success";
        } else return "Failed";
    }

    public boolean updateImage(String attachId, Integer pId) {
        ProfileEntity profileEntity = get(pId);

        if (profileEntity.getAttach() != null) {
            attachService.delete(profileEntity.getAttachId());
        }
        profileRepository.updateAttach(attachId, pId);
        return true;
    }

    private ProfileDTO toDTO(ProfileEntity entity) {
        ProfileDTO dto = new ProfileDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setEmail(entity.getEmail());
        dto.setPassword(entity.getPassword());
        dto.setUpdatedDate(entity.getUpdatedDate());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    public ProfileDTO toDTOSimple(ProfileEntity entity){
        ProfileDTO dto = new ProfileDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        return dto;
    }

    public ProfileDTO toDTOForReport(Integer profileId){
        ProfileEntity entity = get(profileId);
        ProfileDTO dto = new ProfileDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setImage(attachService.toOpenURLDTO(entity.getAttachId()));
        return dto;
    }

    public String changeEmail(String email,Integer id){
        ProfileEntity entity = get(id);
        sendVerificationEmail(id,email);
        StringBuilder builder = new StringBuilder();
        String jwt = JwtUtil.doEncode(id,30,email);
        builder.append(jwt);
        return builder.toString();
    }

    private void sendVerificationEmail(Integer id,String email) {
        StringBuilder builder = new StringBuilder();
        String jwt = JwtUtil.doEncode(id,30,email);
        builder.append(domainName).append("/auth/activate/").append(jwt);
        emailService.send(email, "Activate Your Email", builder.toString());
    }

    public String changeEmail(JwtDTO dto){
       int n = profileRepository.changeEmail(dto.getId(), dto.getEmail());
       if (n > 0){
           return "SUCCESS";
       } return "FAILED";
    }
}
