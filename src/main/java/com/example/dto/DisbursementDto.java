package com.example.dto;

import lombok.Data;

@Data
public class DisbursementDto {
	
	private Integer sanctionId;
	private Integer customerId;
	private String firstName;
	private String lastName;
	private String customerEmail;
	private String intrestType;
	private Float intrestRate;
	private Double EMIAmount;
	private String modeOfPayment;
	private Double sanctionedAmount;
	private Integer tenure;
	

}
