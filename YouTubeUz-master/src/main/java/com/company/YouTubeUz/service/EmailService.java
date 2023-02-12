package com.company.YouTubeUz.service;

import com.company.YouTubeUz.dto.EmailDTO;
import com.company.YouTubeUz.entity.EmailEntity;
import com.company.YouTubeUz.enums.EmailType;
import com.company.YouTubeUz.exp.ItemNotFoundException;
import com.company.YouTubeUz.repository.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private EmailRepository emailRepository;

    public void send(String toEmail, String title, String content) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            message.setSubject(title);
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(toEmail);
            helper.setText(content, true);
            javaMailSender.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        EmailEntity entity = new EmailEntity();
        entity.setToEmail(toEmail);
        entity.setType(EmailType.VERIFICATION);
        emailRepository.save(entity);
    }

    public List<EmailDTO> paginationList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));

        List<EmailDTO> dtoList = new ArrayList<>();
        emailRepository.findAll(pageable).stream().forEach(entity -> {
            dtoList.add(toDTO(entity));
        });

        return dtoList;
    }

    public Boolean delete(Integer id) {
        emailRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Not found!"));
        emailRepository.deleteById(id);
        return true;
    }

    private EmailDTO toDTO(EmailEntity entity) {
        EmailDTO dto = new EmailDTO();
        dto.setId(entity.getId());
        dto.setToEmail(entity.getToEmail());
        dto.setType(entity.getType());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }
}
