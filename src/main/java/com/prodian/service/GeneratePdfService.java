package com.prodian.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.stereotype.Service;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;




@Service
public class GeneratePdfService {

//	public void generatePdf() {
//		String filePdf = "C:Users/Lenovo/Desktop/pdf/sample.pdf";
//		
//		try {
//			PdfWriter writer = new PdfWriter(filePdf);
//			PdfDocument pdfDoc = new PdfDocument(writer);
//			Document document = new Document(pdfDoc);
//			document.close();
//			
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
	
	public static final String source = "src/main/resources/templates/getPdf1.html";
	public static final String dest = "target/sample.pdf";
	
	public void convertPdf() {

    	try {
			HtmlConverter.convertToPdf(new FileInputStream(source), 
			    new FileOutputStream(dest));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

        System.out.println( "PDF Created!" );
	}
	
	
}
