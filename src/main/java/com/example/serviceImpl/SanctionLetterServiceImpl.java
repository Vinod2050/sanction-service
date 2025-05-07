package com.example.serviceImpl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dto.LoanSanctionDto;
import com.example.entity.SanctionLetter;
import com.example.enums.SanctionStatus;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.SanctionLetterRepository;
import com.example.service.SanctionLetterService;

@Service
public class SanctionLetterServiceImpl implements SanctionLetterService {

	private static final Logger logger = LoggerFactory.getLogger(SanctionLetterServiceImpl.class);

	@Autowired
	private SanctionLetterRepository sanctionLetterRepository;

	@Override
	public SanctionLetter createLetter(LoanSanctionDto dto) {
		logger.info("Creating sanction letter for:", dto.getFirstName(), dto.getLastName());

		float interestRate = calculateInterestRate(dto.getCibilScore());
		double approvedAmount = calculateApprovedAmount(dto.getMonthlyIncome(), dto.getCibilScore());
		int tenureMonths = calculateTenureMonths(approvedAmount);
		double emi = calculateEMI(approvedAmount, interestRate, tenureMonths);

		SanctionLetter letter = new SanctionLetter();
		letter.setFirstName(dto.getFirstName());
		letter.setLastName(dto.getLastName());
		letter.setContactNumber(9876543210L);
		letter.setIntrestType("Floating");
		letter.setIntrestRate(interestRate);
		letter.setEMIAmount(emi);
		letter.setModeOfPayment("Online");
		letter.setSanctionedBy("Loan Officer");
		letter.setSanctionDate(new Date());
		letter.setSanctionedAmount(approvedAmount);
		letter.setSanctionStatus(SanctionStatus.OFFERED);
		letter.setTenure(tenureMonths);

		SanctionLetter savedLetter = sanctionLetterRepository.save(letter);
		logger.debug("Sanction letter created:", savedLetter);
		return savedLetter;
	}

	@Override
	public List<SanctionLetter> getAllActiveSanctionLetters() {
		logger.info("Fetching all sanction letters");
		return sanctionLetterRepository.findAll();
	}

	@Override
	public SanctionLetter getSanctionLetterById(Integer id) {
		logger.info("Fetching sanction letter with ID:", id);
		return sanctionLetterRepository.findById(id).orElseThrow(() -> {
			logger.error("Sanction letter not found with ID: ", id);
			return new ResourceNotFoundException("Sanction letter not found with ID: " + id);
		});
	}

	@Override
	public SanctionLetter updateSanctionLetter(Integer id, SanctionLetter updateData) {
		logger.info("Updating sanction letter with ID:", id);

		SanctionLetter existing = sanctionLetterRepository.findById(id).orElseThrow(() -> {
			logger.error("Cannot update. Sanction letter not found with ID: ", id);
			return new ResourceNotFoundException("Sanction letter not found with ID: " + id);
		});

		if (updateData.getFirstName() != null)
			existing.setFirstName(updateData.getFirstName());
		if (updateData.getLastName() != null)
			existing.setLastName(updateData.getLastName());
		if (updateData.getContactNumber() != null)
			existing.setContactNumber(updateData.getContactNumber());
		if (updateData.getIntrestType() != null)
			existing.setIntrestType(updateData.getIntrestType());
		if (updateData.getIntrestRate() != null)
			existing.setIntrestRate(updateData.getIntrestRate());
		if (updateData.getEMIAmount() != null)
			existing.setEMIAmount(updateData.getEMIAmount());
		if (updateData.getModeOfPayment() != null)
			existing.setModeOfPayment(updateData.getModeOfPayment());
		if (updateData.getSanctionedBy() != null)
			existing.setSanctionedBy(updateData.getSanctionedBy());
		if (updateData.getSanctionDate() != null)
			existing.setSanctionDate(updateData.getSanctionDate());
		if (updateData.getSanctionedAmount() != null)
			existing.setSanctionedAmount(updateData.getSanctionedAmount());
		if (updateData.getSanctionStatus() != null)
			existing.setSanctionStatus(updateData.getSanctionStatus());
		if (updateData.getTenure() != null)
			existing.setTenure(updateData.getTenure());

		SanctionLetter updated = sanctionLetterRepository.save(existing);
		logger.debug("Sanction letter updated: ", updated);
		return updated;
	}

	private double calculateApprovedAmount(double monthlyIncome, int cibilScore) {
		int multiplier = cibilScore >= 750 ? 60 : 40;
		double approvedAmount = monthlyIncome * multiplier;
		return Math.min(approvedAmount, 1_00_00_000);
	}

	private float calculateInterestRate(int cibilScore) {
		return cibilScore >= 750 ? 7.9f : 8.5f;
	}

	private int calculateTenureMonths(double approvedAmount) {
		if (approvedAmount <= 25_00_000)
			return 10 * 12;
		else if (approvedAmount <= 50_00_000)
			return 15 * 12;
		else
			return 20 * 12;
	}

	private double calculateEMI(double principal, float annualRate, int tenureMonths) {
		double monthlyRate = annualRate / (12 * 100);
		return (principal * monthlyRate * Math.pow(1 + monthlyRate, tenureMonths))
				/ (Math.pow(1 + monthlyRate, tenureMonths) - 1);
	}
}
