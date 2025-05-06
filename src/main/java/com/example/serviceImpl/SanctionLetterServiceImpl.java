package com.example.serviceImpl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dto.LoanSanctionDto;
import com.example.entity.SanctionLetter;
import com.example.enums.SanctionStatus;
import com.example.repository.SanctionLetterRepository;
import com.example.service.SanctionLetterService;

@Service
public class SanctionLetterServiceImpl implements SanctionLetterService {

	@Autowired
	private SanctionLetterRepository sanctionLetterRepository;

	@Override
	public SanctionLetter createLetter(LoanSanctionDto dto) {
		SanctionLetter letter = new SanctionLetter();

		float interestRate = calculateInterestRate(dto.getCibilScore());

		double approvedAmount = calculateApprovedAmount(dto.getMonthlyIncome(), dto.getCibilScore());

		int tenureMonths = calculateTenureMonths(approvedAmount);

		double emi = calculateEMI(approvedAmount, interestRate, tenureMonths);

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

		return sanctionLetterRepository.save(letter);
	}

	private double calculateApprovedAmount(double monthlyIncome, int cibilScore) {
		int multiplier = 40;

		if (cibilScore >= 750) {
			multiplier = 60;
		}

		double approvedAmount = monthlyIncome * multiplier;

		if (approvedAmount > 1_00_00_000) {
			approvedAmount = 1_00_00_000;
		}

		return approvedAmount;
	}

	private float calculateInterestRate(int cibilScore) {
		if (cibilScore >= 750) {
			return 7.9f;
		} else {
			return 8.5f;
		}
	}

	private int calculateTenureMonths(double approvedAmount) {
		if (approvedAmount <= 25_00_000) {
			return 10 * 12;
		} else if (approvedAmount <= 50_00_000) {
			return 15 * 12;
		} else {
			return 20 * 12;
		}
	}

	private double calculateEMI(double principal, float annualRate, int tenureMonths) {
		double monthlyRate = annualRate / (12 * 100);
		return (principal * monthlyRate * Math.pow(1 + monthlyRate, tenureMonths))
				/ (Math.pow(1 + monthlyRate, tenureMonths) - 1);
	}

	@Override
	public List<SanctionLetter> getAllActiveSanctionLetters() {
		return sanctionLetterRepository.findAll();
	}

	@Override
	public SanctionLetter getSanctionLetterById(Integer id) {
		return sanctionLetterRepository.findById(id).get();

	}

	@Override
	public SanctionLetter updateSanctionLetter(Integer id, SanctionLetter updateData) {

		SanctionLetter existingSanctionLetter = sanctionLetterRepository.findById(id).orElse(null);

		if (existingSanctionLetter == null) {

			return null;
		}

		if (updateData.getFirstName() != null) {
			existingSanctionLetter.setFirstName(updateData.getFirstName());
		}
		if (updateData.getLastName() != null) {
			existingSanctionLetter.setLastName(updateData.getLastName());
		}
		if (updateData.getContactNumber() != null) {
			existingSanctionLetter.setContactNumber(updateData.getContactNumber());
		}
		if (updateData.getIntrestType() != null) {
			existingSanctionLetter.setIntrestType(updateData.getIntrestType());
		}
		if (updateData.getIntrestRate() != null) {
			existingSanctionLetter.setIntrestRate(updateData.getIntrestRate());
		}
		if (updateData.getEMIAmount() != null) {
			existingSanctionLetter.setEMIAmount(updateData.getEMIAmount());
		}
		if (updateData.getModeOfPayment() != null) {
			existingSanctionLetter.setModeOfPayment(updateData.getModeOfPayment());
		}
		if (updateData.getSanctionedBy() != null) {
			existingSanctionLetter.setSanctionedBy(updateData.getSanctionedBy());
		}
		if (updateData.getSanctionDate() != null) {
			existingSanctionLetter.setSanctionDate(updateData.getSanctionDate());
		}
		if (updateData.getSanctionedAmount() != null) {
			existingSanctionLetter.setSanctionedAmount(updateData.getSanctionedAmount());
		}
		if (updateData.getSanctionStatus() != null) {
			existingSanctionLetter.setSanctionStatus(updateData.getSanctionStatus());
		}
		if (updateData.getTenure() != null) {
			existingSanctionLetter.setTenure(updateData.getTenure());
		}

		return sanctionLetterRepository.save(existingSanctionLetter);
	}

}
