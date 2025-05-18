package com.example.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.stereotype.Service;

import com.example.dto.PdfGenerationDto;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;

@Service
public class PdfGenerationService {

	public byte[] generatePdf(PdfGenerationDto request) throws IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		PdfWriter writer = new PdfWriter(baos);

		PdfDocument pdfDoc = new PdfDocument(writer);

		Document document = new Document(pdfDoc);

		// ✅ Colors
		com.itextpdf.kernel.colors.Color headerColor = new DeviceRgb(194, 24, 91);
		com.itextpdf.kernel.colors.Color tableHeaderColor = new DeviceRgb(63, 81, 181); 
		com.itextpdf.kernel.colors.Color grayBackground = new DeviceRgb(240, 240, 240);

		// ✅ Add Logo (Assuming logo.png is in src/main/resources)
		String logoPath = "src/main/resources/static/unifiedHomeLoan.png";
		ImageData data = ImageDataFactory.create(logoPath);

		Image img = new Image(data).scaleToFit(80, 80);

		img.setHorizontalAlignment(HorizontalAlignment.CENTER);

		document.add(img);

		// ✅ Bank Header
		Paragraph bankName = new Paragraph("Unified Home Loans").setBold().setFontSize(20).setFontColor(headerColor)
				.setTextAlignment(TextAlignment.CENTER);

		document.add(bankName);

		Paragraph branch = new Paragraph("Registered Office: Mumbai, Maharashtra, India").setFontSize(10)
				.setTextAlignment(TextAlignment.CENTER);

		document.add(branch);

		document.add(new LineSeparator(new SolidLine()));

		// ✅ Title
		document.add(new Paragraph("Sanction Letter").setBold().setFontSize(16).setTextAlignment(TextAlignment.CENTER)
				.setMarginTop(10).setUnderline());

		// ✅ Date and Ref
		document.add(new Paragraph("Date: " + request.getSanctionDate()).setMarginTop(10));
		document.add(new Paragraph("Sanction Ref No.: HL/" + request.getSanctionId()));

		// ✅ Customer Info
		document.add(new Paragraph("\nTo,"));
		document.add(new Paragraph(request.getFirstName() + " " + request.getLastName()).setBold());
	
		// ✅ Subject
		document.add(new Paragraph("Subject: Sanction of Home Loan of ₹" + request.getSanctionedAmount() + "/-")
				.setUnderline());

		// ✅ Loan Details Table with Borders & Colors
		float[] columnWidths = { 300f, 400f };
		Table table = new Table(columnWidths);
		table.setMarginTop(15);
		table.setWidth(500);

		// ✅ Table Border Style
		table.setBorder(new SolidBorder(ColorConstants.BLACK, 1));

		// Table Rows with Header Color + Borders
		table.addCell(new Cell().add(new Paragraph("Sanctioned Loan Amount").setBold().setFontColor(tableHeaderColor))
				.setBackgroundColor(grayBackground).setBorder(new SolidBorder(ColorConstants.BLACK, 1)));

		table.addCell(new Cell().add(new Paragraph("₹" + request.getSanctionedAmount()))
				.setBorder(new SolidBorder(ColorConstants.BLACK, 1)));

		table.addCell(new Cell().add(new Paragraph("Interest Rate").setBold().setFontColor(tableHeaderColor))
				.setBackgroundColor(grayBackground).setBorder(new SolidBorder(ColorConstants.BLACK, 1)));

		table.addCell(new Cell().add(new Paragraph(request.getIntrestRate() + "% p.a."))
				.setBorder(new SolidBorder(ColorConstants.BLACK, 1)));

		table.addCell(new Cell().add(new Paragraph("Loan Tenure").setBold().setFontColor(tableHeaderColor))
				.setBackgroundColor(grayBackground).setBorder(new SolidBorder(ColorConstants.BLACK, 1)));

		table.addCell(new Cell().add(new Paragraph(request.getTenure() + " Months"))
				.setBorder(new SolidBorder(ColorConstants.BLACK, 1)));

		table.addCell(new Cell().add(new Paragraph("Interest Type").setBold().setFontColor(tableHeaderColor))
				.setBackgroundColor(grayBackground).setBorder(new SolidBorder(ColorConstants.BLACK, 1)));

		table.addCell(new Cell().add(new Paragraph(request.getIntrestType()))
				.setBorder(new SolidBorder(ColorConstants.BLACK, 1)));

		table.addCell(new Cell().add(new Paragraph("EMI (Approx.)").setBold().setFontColor(tableHeaderColor))
				.setBackgroundColor(grayBackground).setBorder(new SolidBorder(ColorConstants.BLACK, 1)));

		table.addCell(new Cell().add(new Paragraph("₹" + request.getEMIAmount()))
				.setBorder(new SolidBorder(ColorConstants.BLACK, 1)));

		table.addCell(new Cell().add(new Paragraph("Processing Fees").setBold().setFontColor(tableHeaderColor))
				.setBackgroundColor(grayBackground).setBorder(new SolidBorder(ColorConstants.BLACK, 1)));

		table.addCell(new Cell().add(new Paragraph("₹" + request.getProcessingFees() + " + GST"))
				.setBorder(new SolidBorder(ColorConstants.BLACK, 1)));
		document.add(table);

		// ✅ Terms
		document.add(new Paragraph("\nKey Terms & Conditions:").setBold().setFontColor(headerColor).setFontSize(12));

		document.add(new Paragraph("1. Loan disbursement subject to legal and technical clearance."));
		document.add(new Paragraph("2. Original property documents to be deposited before disbursement."));
		document.add(new Paragraph("3. Sanction valid for 90 days from the date of this letter."));
		document.add(new Paragraph("4. Interest rate linked to Repo Rate and subject to revision."));

		// ✅ Footer
		document.add(new Paragraph(
				"\nPlease visit our nearest branch to complete further formalities and initiate disbursement."));

		document.add(new Paragraph("\nAuthorized Signatory").setBold());
		document.add(new Paragraph("Loan Officer"));
		document.add(new Paragraph("Unified Home Loans"));

		document.close();

		return baos.toByteArray();
	}

}
