package com.example.dto;



import lombok.Data;
@Data
public class EmailDTO {
	    private String to;
	    private String firstName;
	    private String subject;
	    private String body;
	    private String attachmentName;
	    private byte[] attachmentData; 

	
	}


