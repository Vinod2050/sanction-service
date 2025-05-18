package com.example.dto;

import java.util.Date;

import lombok.Data;

@Data
public class PdfGenerationDto {

	private Integer sanctionId;
	private String firstName;
	private String lastName;
	private String intrestType;
	private Float intrestRate;
	private Double EMIAmount;
	private String sanctionedBy;
	private Date sanctionDate;
	private Double sanctionedAmount;
	private Integer tenure;
	private Double processingFees=2000.0;
	

}
