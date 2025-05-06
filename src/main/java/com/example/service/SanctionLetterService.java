package com.example.service;

import java.util.List;

import com.example.dto.LoanSanctionDto;
import com.example.entity.SanctionLetter;
import com.example.enums.SanctionStatus;

public interface SanctionLetterService {

	SanctionLetter createLetter(LoanSanctionDto loanSanctionDto);

	List<SanctionLetter> getAllActiveSanctionLetters();

	SanctionLetter getSanctionLetterById(Integer id);

	SanctionLetter updateSanctionLetter(Integer id, SanctionLetter updateData);

}
