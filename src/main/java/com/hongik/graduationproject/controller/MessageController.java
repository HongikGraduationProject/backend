package com.hongik.graduationproject.controller;

import com.hongik.graduationproject.domain.dto.ApiResponse;
import com.hongik.graduationproject.domain.dto.MessageDto;
import com.hongik.graduationproject.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MessageController {
    private final MessageService messageService;

    /**
     * Queue로 메시지를 발행
     *
     * @param messageDto 발행할 메시지의 DTO 객체
     * @return ResponseEntity 객체로 응답을 반환
     */
    @PostMapping("/send/message")
    public ApiResponse<?> sendMessage(@RequestBody MessageDto messageDto) {
        messageService.sendMessage(messageDto);
        return ApiResponse.createSuccess("Message sent to RabbitMQ!");
    }
}
