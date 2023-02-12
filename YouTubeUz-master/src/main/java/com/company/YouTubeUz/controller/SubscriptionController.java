package com.company.YouTubeUz.controller;

import com.company.YouTubeUz.dto.SubscriptionDTO;
import com.company.YouTubeUz.enums.NotificationType;
import com.company.YouTubeUz.enums.ProfileRole;
import com.company.YouTubeUz.enums.SubscriptionStatus;
import com.company.YouTubeUz.service.SubscriptionService;
import com.company.YouTubeUz.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/subscriptions")
@Api(tags = "Subscription")
public class SubscriptionController {
    @Autowired
    private SubscriptionService subscriptionService;

    @ApiOperation(value = "Create", notes = "Method used for create subscriptions", nickname = "Bilol")
    @PostMapping("/public")
    public ResponseEntity<SubscriptionDTO> create(@RequestBody @Valid SubscriptionDTO dto,
                                    HttpServletRequest request){
        Integer pId = JwtUtil.getIdFromHeader(request, ProfileRole.USER);
        return ResponseEntity.ok(subscriptionService.create(pId, dto));
    }

    @ApiOperation(value = "Update Status", notes = "Method used for update status subscriptions", nickname = "Bilol")
    @PutMapping("/public/status/{subscriptionId}")
    public ResponseEntity<String> updateStatus(@PathVariable("subscriptionId") Integer subscriptionId,
                                          @RequestParam SubscriptionStatus status,
                                          HttpServletRequest request){
        Integer pId = JwtUtil.getIdFromHeader(request, ProfileRole.USER);
        return ResponseEntity.ok(subscriptionService.updateStatus(subscriptionId, pId, status));
    }

    @ApiOperation(value = "Update type", notes = "Method used for update type subscriptions", nickname = "Bilol")
    @PutMapping("/public/type/{subscriptionId}")
    public ResponseEntity<String> updateType(@PathVariable("subscriptionId") Integer subscriptionId,
                                          @RequestParam NotificationType type,
                                          HttpServletRequest request){
        Integer pId = JwtUtil.getIdFromHeader(request, ProfileRole.USER);
        return ResponseEntity.ok(subscriptionService.updateType(subscriptionId, pId, type));
    }

    @ApiOperation(value = "Get List", notes = "Method used for get list subscriptions", nickname = "Bilol")
    @GetMapping("/public/list")
    public ResponseEntity<List<SubscriptionDTO>> getList(HttpServletRequest request){
        Integer pId = JwtUtil.getIdFromHeader(request, ProfileRole.USER);
        return ResponseEntity.ok(subscriptionService.getList(pId));
    }

    @ApiOperation(value = "Get By User Id", notes = "Method used for get by user id subscriptions", nickname = "Bilol")
    @GetMapping("/adm/list/{userId}")
    public ResponseEntity<List<SubscriptionDTO>> getListByUserId(@PathVariable("userId") Integer userId,
                                             HttpServletRequest request){
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(subscriptionService.getList(userId));
    }
}
