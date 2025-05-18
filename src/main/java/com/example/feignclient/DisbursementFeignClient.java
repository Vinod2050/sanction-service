package com.example.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.dto.DisbursementDto;


@FeignClient(name = "disbursement-service")
public interface DisbursementFeignClient {
	
	
	@PostMapping("api/disbursement/start-disburse")
	public ResponseEntity<String>startDisbursement(@RequestBody DisbursementDto disbursementDto);
}
