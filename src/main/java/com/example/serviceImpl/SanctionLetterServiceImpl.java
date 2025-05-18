package com.example.serviceImpl;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dto.DisbursementDto;
import com.example.dto.EmailDTO;
import com.example.dto.LoanSanctionDto;
import com.example.dto.PdfGenerationDto;
import com.example.entity.SanctionLetter;
import com.example.enums.SanctionStatus;
import com.example.exception.ResourceNotFoundException;
import com.example.feignclient.DisbursementFeignClient;
import com.example.feignclient.EmailFeignClient;
import com.example.repository.SanctionLetterRepository;
import com.example.service.PdfGenerationService;
import com.example.service.SanctionLetterService;

@Service
public class SanctionLetterServiceImpl implements SanctionLetterService {

	private static final Logger logger = LoggerFactory.getLogger(SanctionLetterServiceImpl.class);

	@Autowired
	private SanctionLetterRepository sanctionLetterRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private EmailFeignClient emailFiegnClient;

	@Autowired
	private DisbursementFeignClient disbursementFeignClient;

	@Autowired
	private PdfGenerationService pdfGenerationService;

	@Override
	public SanctionLetter createLetter(LoanSanctionDto dto) throws IOException {
	    logger.info("Creating sanction letter for customer: {} {}", dto.getFirstName(), dto.getLastName());

	    Optional<SanctionLetter> existingLetter = sanctionLetterRepository.findByCustomerId(dto.getCustomerId());
	    if (existingLetter.isPresent()) {
	        SanctionLetter letter = existingLetter.get();
	        if (Boolean.TRUE.equals(letter.getIsMailSent())) {
	            logger.info("Customer with ID {} already has a sanction letter and mail sent.", dto.getCustomerId());
	            return letter;
	        } else {
	            logger.info("Customer with ID {} found but mail not sent. Sending mail now.", dto.getCustomerId());
	            sendSanctionLetterMail(letter);
	            letter.setIsMailSent(true);
	            return sanctionLetterRepository.save(letter);
	        }
	    }

	    
	    SanctionLetter letter = modelMapper.map(dto, SanctionLetter.class);

	    float interestRate = calculateInterestRate(dto.getCibilScore());
	    double approvedAmount = calculateApprovedAmount(dto.getMonthlyIncome(), dto.getCibilScore());
	    int tenureMonths = calculateTenureMonths(approvedAmount);
	    double emi = calculateEMI(approvedAmount, interestRate, tenureMonths);

	    letter.setIntrestType("Floating");
	    letter.setIntrestRate(interestRate);
	    letter.setEMIAmount(emi);
	    letter.setModeOfPayment("Online");
	    letter.setSanctionedBy("Loan Officer");
	    letter.setSanctionDate(new Date());
	    letter.setSanctionedAmount(approvedAmount);
	    letter.setSanctionStatus(SanctionStatus.OFFERED);
	    letter.setTenure(tenureMonths);
	    letter.setIsMailSent(true);

	    SanctionLetter savedLetter = sanctionLetterRepository.save(letter);
	    logger.debug("Sanction letter saved: {}", savedLetter);

	    sendSanctionLetterMail(savedLetter);
	    logger.info("Sanction letter email sent to customerId: {}", dto.getCustomerId());

	    return savedLetter;
	}

	private void sendSanctionLetterMail(SanctionLetter letter) throws IOException {
	    PdfGenerationDto pdfGenerationDto = modelMapper.map(letter, PdfGenerationDto.class);
	    byte[] generatePdf = pdfGenerationService.generatePdf(pdfGenerationDto);

	    EmailDTO emailDTO = new EmailDTO();
	    emailDTO.setTo(letter.getCustomerEmail());
	    emailDTO.setFirstName(letter.getFirstName());
	    emailDTO.setSubject("Sanction Letter Status");
	    emailDTO.setAttachmentName("Sanction Letter");
	    emailDTO.setAttachmentData(generatePdf);

	    logger.debug("Sending sanction letter email to: {}", letter.getCustomerEmail());
	    emailFiegnClient.sendEmail(emailDTO);
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
	public LoanSanctionDto updateSanctionLetter(Integer customerId, LoanSanctionDto updateData) {
//		logger.info("Updating sanction letter with ID:", customerId);
//
//		SanctionLetter existing = sanctionLetterRepository.findById(customerId).orElseThrow(() -> {
//			logger.error("Cannot update. Sanction letter not found with ID: ", customerId);
//			return new ResourceNotFoundException("Sanction letter not found with ID: " + customerId);
//		});
//
//		if (updateData.getFirstName() != null)
//			existing.setFirstName(updateData.getFirstName());
//		if (updateData.getLastName() != null)
//			existing.setLastName(updateData.getLastName());
//		if (updateData.getIntrestType() != null)
//			existing.setIntrestType(updateData.getIntrestType());
//		if (updateData.getIntrestRate() != null)
//			existing.setIntrestRate(updateData.getIntrestRate());
//		if (updateData.getEMIAmount() != null)
//			existing.setEMIAmount(updateData.getEMIAmount());
//		if (updateData.getModeOfPayment() != null)
//			existing.setModeOfPayment(updateData.getModeOfPayment());
//		if (updateData.getSanctionedBy() != null)
//			existing.setSanctionedBy(updateData.getSanctionedBy());
//		if (updateData.getSanctionDate() != null)
//			existing.setSanctionDate(updateData.getSanctionDate());
//		if (updateData.getSanctionedAmount() != null)
//			existing.setSanctionedAmount(updateData.getSanctionedAmount());
//		if (updateData.getSanctionStatus() != null)
//			existing.setSanctionStatus(updateData.getSanctionStatus());
//		if (updateData.getTenure() != null)
//			existing.setTenure(updateData.getTenure());
//
//		SanctionLetter updated = sanctionLetterRepository.save(existing);
//		logger.debug("Sanction letter updated: ", updated);
		return null;
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

	@Override
	public String startDisbursement(Boolean isProcessingFeesPaid, Boolean isSanctionLetterAccepted, Integer customerId) {
	    logger.info("Received disbursement request for customerId: {}", customerId);
	    
	    Optional<SanctionLetter> optionalSanctionLetter = sanctionLetterRepository.findByCustomerId(customerId);

	    if (optionalSanctionLetter.isPresent()) {
	        logger.debug("Sanction letter found for customerId: {}", customerId);

	        SanctionLetter sanctionLetter = optionalSanctionLetter.get();
	        sanctionLetter.setIsProcessingFeesPaid(isProcessingFeesPaid);
	        sanctionLetter.setIsSanctionLetterAccepted(isSanctionLetterAccepted);
	        sanctionLetter.setSanctionStatus(SanctionStatus.ACCEPTED);
	        sanctionLetterRepository.save(sanctionLetter);
	        sanctionLetterRepository.flush();
	        logger.info("Sanction letter updated for customerId: {}", customerId);

	        if (Boolean.TRUE.equals(isProcessingFeesPaid) && Boolean.TRUE.equals(isSanctionLetterAccepted)) {
	            logger.info("Processing fees paid and sanction letter accepted for customerId: {}, preparing disbursement", customerId);

	            DisbursementDto disbursementDto = new DisbursementDto();
	            disbursementDto.setCustomerId(sanctionLetter.getCustomerId());
	            disbursementDto.setEMIAmount(sanctionLetter.getEMIAmount());
	            disbursementDto.setTenure(sanctionLetter.getTenure());
	            disbursementDto.setIntrestType(sanctionLetter.getIntrestType());
	            disbursementDto.setIntrestRate(sanctionLetter.getIntrestRate());
	            disbursementDto.setFirstName(sanctionLetter.getFirstName());
	            disbursementDto.setLastName(sanctionLetter.getLastName());
	            disbursementDto.setModeOfPayment(sanctionLetter.getModeOfPayment());
	            disbursementDto.setSanctionedAmount(sanctionLetter.getSanctionedAmount());
	            disbursementDto.setCustomerEmail(sanctionLetter.getCustomerEmail());
	            disbursementDto.setSanctionId(sanctionLetter.getSanctionId());

	            logger.debug("Calling disbursement service for customerId: {}", customerId);
	            disbursementFeignClient.startDisbursement(disbursementDto);

	            logger.info("Disbursement data sent successfully for customerId: {}", customerId);
	            return "Disbursement data sent successfully for customerId: " + customerId;
	        }

	        logger.warn("Disbursement not started due to flags for customerId: {}", customerId);
	        return "Sanction letter updated, but disbursement not started due to flags. customerId: " + customerId;
	    }

	    logger.error("No sanction letter found for customerId: {}", customerId);
	    return "Customer Id not found: " + customerId;
	}


}
