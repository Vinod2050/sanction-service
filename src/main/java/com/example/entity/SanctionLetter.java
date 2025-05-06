package com.example.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.example.enums.SanctionStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SanctionLetter {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer sanctionId;
	private String firstName;
	private String lastName;
	private Long contactNumber;
	private String intrestType;
	private Float intrestRate;
	private Double EMIAmount;
	private String modeOfPayment;
	private String sanctionedBy;
	private Date sanctionDate;
	private Double sanctionedAmount;
	@Enumerated(EnumType.STRING)
	private SanctionStatus sanctionStatus;
	private Integer tenure;
    

}
