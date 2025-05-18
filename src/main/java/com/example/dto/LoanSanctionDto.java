package com.example.dto;

import java.util.Date;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import lombok.Data;

@Data
public class LoanSanctionDto {
	private Integer applicationId;
	private Integer customerId;
	private String firstName;
	private String lastName;
	private String customerEmail;
	@Enumerated(EnumType.STRING)
	private String loanType;
	private Double requestedLoanAmount;
	private Date applicationDate;
	private Double interestRate;
	private Integer requestedTenure;
	private Integer cibilScore;
	private Double monthlyIncome;
	
	

}
