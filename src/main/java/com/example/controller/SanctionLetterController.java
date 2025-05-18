package com.example.controller;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.example.dto.LoanSanctionDto;
import com.example.entity.SanctionLetter;
import com.example.service.SanctionLetterService;

@RestController
@RequestMapping("/api/sanctions")
public class SanctionLetterController {
	private static final Logger logger = LoggerFactory.getLogger(SanctionLetterController.class);

	@Autowired
	private SanctionLetterService sanctionLetterService;

	@PostMapping("/sanctionLetters")
	private ResponseEntity<SanctionLetter> createSanctionLetter(@RequestBody LoanSanctionDto loanSanctionDto) throws IOException {
		SanctionLetter sanctionLetter = sanctionLetterService.createLetter(loanSanctionDto);

		logger.info("Created sanction letter for loan ");
		return new ResponseEntity<SanctionLetter>(sanctionLetter, HttpStatus.CREATED);
	}

	@GetMapping("/sanctionLetters")
	public List<SanctionLetter> getAllSanctionLetters() {
		logger.info("Fetching all active sanction letters");
		return sanctionLetterService.getAllActiveSanctionLetters();
	}

	@GetMapping("/sanctionLetter/{id}")
	public ResponseEntity<SanctionLetter> getSanctionLetterById(@PathVariable Integer id) {
		logger.info("Fetching sanction letter by ID:", id);
		SanctionLetter letter = sanctionLetterService.getSanctionLetterById(id);
		if (letter != null) {
			return new ResponseEntity<>(letter, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

//	@PatchMapping("/sanctionLetters/{customerId}")
//	public ResponseEntity<LoanSanctionDto> updateSanctionLetter(@PathVariable Integer customerId,
//	        @RequestBody LoanSanctionDto updateData) {
//	    logger.info("Updating sanction letter with ID: {}", customerId);
//	    LoanSanctionDto updated = sanctionLetterService.updateSanctionLetter(customerId, updateData);
//	    return updated != null ? new ResponseEntity<LoanSanctionDto>(updated, HttpStatus.OK)
//	                           : new ResponseEntity<>(HttpStatus.NOT_FOUND);
//	}

	@PatchMapping("/disbursement/{customerId}")
	public ResponseEntity<String> startDisbursement(
	        @RequestParam Boolean isProcessingFeesPaid,
	        @RequestParam Boolean isSanctionLetterAccepted,
	        @PathVariable Integer customerId) {
	    logger.info("Starting disbursement for customer ID: {}", customerId);
	    String msg = sanctionLetterService.startDisbursement(isProcessingFeesPaid, isSanctionLetterAccepted, customerId);
	    return new ResponseEntity<>(msg, HttpStatus.OK);
	}

}
