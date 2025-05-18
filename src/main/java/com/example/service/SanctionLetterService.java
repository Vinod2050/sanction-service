package com.example.service;

import java.io.IOException;
import java.util.List;

import com.example.dto.LoanSanctionDto;
import com.example.entity.SanctionLetter;

public interface SanctionLetterService {

	SanctionLetter createLetter(LoanSanctionDto loanSanctionDto) throws IOException;

	List<SanctionLetter> getAllActiveSanctionLetters();

	SanctionLetter getSanctionLetterById(Integer id);

	LoanSanctionDto updateSanctionLetter(Integer id, LoanSanctionDto updateData);

	String startDisbursement(Boolean isProcessingFeesPaid, Boolean isSanctionLetterAccepted, Integer customerId);

}
