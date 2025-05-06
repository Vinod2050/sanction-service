package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.LoanSanctionDto;
import com.example.entity.SanctionLetter;
import com.example.service.SanctionLetterService;

@RestController
@RequestMapping("/api/sanctions")
public class SanctionLetterController {

	@Autowired
	private SanctionLetterService sanctionLetterService;

	@PostMapping("/sanctionLetters")
	private ResponseEntity<SanctionLetter> createSanctionLetter(@RequestBody LoanSanctionDto loanSanctionDto) {
		SanctionLetter sanctionLetter = sanctionLetterService.createLetter(loanSanctionDto);
		return new ResponseEntity<SanctionLetter>(sanctionLetter, HttpStatus.CREATED);
	}

	@GetMapping("/sanctionLetters")
	public List<SanctionLetter> getAllSanctionLetters() {
		return sanctionLetterService.getAllActiveSanctionLetters();
	}

	@GetMapping("/sanctionLetter/{id}")
	public ResponseEntity<SanctionLetter> getSanctionLetterById(@PathVariable Integer id) {
		SanctionLetter letter = sanctionLetterService.getSanctionLetterById(id);
		if (letter != null) {
			return new ResponseEntity<>(letter, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PatchMapping("/sanctionLetters/{id}")
	public ResponseEntity<SanctionLetter> updateSanctionLetter(@PathVariable Integer id,
			@RequestBody SanctionLetter updateData) {

		SanctionLetter updated = sanctionLetterService.updateSanctionLetter(id, updateData);

		if (updated != null) {
			return new ResponseEntity<SanctionLetter>(updated, HttpStatus.OK);
		} else {
			return new ResponseEntity<SanctionLetter>(HttpStatus.NOT_FOUND);
		}
	}

}
