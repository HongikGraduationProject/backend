package com.hongik.graduationproject.controller;

import com.hongik.graduationproject.domain.dto.Response;
import com.hongik.graduationproject.domain.oauth.KaKaoRequestDto;
import com.hongik.graduationproject.repository.TestEntityRepository;
import com.hongik.graduationproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final TestEntityRepository testEntityRepository;
    private final UserService userService;

    @PostMapping("/oauth/token")
    public Response<?> getLogin(@RequestBody KaKaoRequestDto kaKaoRequestDto){
        return Response.createSuccess(userService.saveUser(kaKaoRequestDto));
    }

    @GetMapping("/test")
    public Response<?> test(){
        return Response.createSuccess(testEntityRepository.findById(1L).get());
    }
}