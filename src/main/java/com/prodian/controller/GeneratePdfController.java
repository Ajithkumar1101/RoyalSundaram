package com.prodian.controller;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Base64.Encoder;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.google.zxing.WriterException;
import com.prodian.service.GeneratePdfService;
import com.prodian.service.QRCODEGenerator;

@RestController
public class GeneratePdfController {

	private GeneratePdfService generatePdfService;

	@Autowired
	public GeneratePdfController(GeneratePdfService generatePdfService) {

		this.generatePdfService = generatePdfService;
	}

	@GetMapping("/pdf")
	public ModelAndView generatePdf() {
		ModelAndView c = new ModelAndView();
		c.setViewName("getPdf.html");
		return c;

	}

	@GetMapping("/createPdf")
	public String pdfFile() {
		try {
			generatePdfService.convertPdf();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return "PDF CREATED";
	}

	@PostMapping(value = "/excel" ,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String excelReader(@RequestParam("excel") MultipartFile excel) {
		
		
		try {
			HSSFWorkbook workbook = new HSSFWorkbook(excel.getInputStream());
			HSSFSheet sheet = workbook.getSheetAt(0);
			
			for(int i = 0; i < sheet.getPhysicalNumberOfRows();i++) {
				HSSFRow row = sheet.getRow(i);
				for(int j = 0; j< row.getPhysicalNumberOfCells();j++) {
					HSSFCell rownum = row.getCell(0);
					//System.out.print(rownum+ " ");
				}
				HSSFCell rownum = row.getCell(0);
//				System.out.println(rownum);
				String link = "https://my.royalsundaram.in/health-insurance/lifeline?agent_code=AG000954";
				Encoder encoder = Base64.getEncoder();
				String originalString = rownum.toString();
				String encodedString = encoder.encodeToString(originalString.getBytes());
				link = link.split("=")[0] + "=" + encodedString + "&&encoded=true";
				System.out.println(link);
				// Generate and Return Qr Code in Byte Array
				byte[] image = new byte[0];
//				image = QRCODEGenerator.getQRCodeImage(link, 250, 250);
//				Create directory
				String path = null;
				path = "D://QR/"+originalString;
				new File(path).mkdir();
				// Generate and Save Qr Code Image in static/image folder
				QRCODEGenerator.generateQRCodeImage(link, 250, 250, path+"/"+originalString+".png");

			//	System.out.println("  ");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "SUCCESS";
	}
	
}
