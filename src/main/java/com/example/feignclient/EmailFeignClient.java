package com.example.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.dto.EmailDTO;

@FeignClient(name = "email-service")
public interface EmailFeignClient {

    @PostMapping("/api/email/send")
    ResponseEntity<String> sendEmail(@RequestBody EmailDTO emailDTO);
}
