package com.sample;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@SpringBootApplication
public class DemoQrCodeApplication {
	
	private static final Logger LOG = LoggerFactory.getLogger(DemoQrCodeApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(DemoQrCodeApplication.class, args);
		try {
			generateQrCode();
		} catch (FileNotFoundException e) {
			LOG.error(e.getCause().getCause().getMessage());
		} catch (DocumentException e) {
			LOG.error(e.getCause().getCause().getMessage());
		}
	}

	public static Image generateImage(String value) throws Exception {
		//Set value of QRCode and dimension
		BarcodeQRCode barcodeQRCode = new BarcodeQRCode(value, 1000, 1000, null);
		Image img = barcodeQRCode.getImage();
		//Set image scale
		img.scaleAbsolute(100, 100);
		return img;
	}
	
	public static void generateQrCode() throws FileNotFoundException, DocumentException {
		//Set page
		Document document = new Document();
		document.setPageSize(PageSize.A4.rotate());
		document.setMargins(0, 0, 25, 25);
        //Set name file
		PdfWriter.getInstance(document, new FileOutputStream("Barcode.pdf"));
		document.open();
		//Set font, alignment, title
		Font font = new Font(FontFamily.TIMES_ROMAN, 16, Font.BOLD);
		Paragraph title = new Paragraph("List QRCode", font);
		title.setAlignment(Element.ALIGN_CENTER);
		title.add(new Paragraph(""));
		//Set pdf table with 10 column
		PdfPTable table = new PdfPTable(10);
		//Set table borderless
		table.getDefaultCell().setBorder(0);
		//Set width percentage
        table.setWidthPercentage(97f);
        //Fill table with static data
    	for (int j = 0; j < 100; j++) {
    		Image image;
			try {
				image = generateImage("Random String Generated " + j);
				table.addCell(image);
			} catch (Exception e) {
				LOG.error(e.getCause().getCause().getMessage());
			}
        }
    	document.add(title);
        document.add(table);
        document.close();
        LOG.info("Generated file Barcode.pdf success.");
	}
}
