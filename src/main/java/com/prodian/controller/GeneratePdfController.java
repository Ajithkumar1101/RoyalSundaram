package com.prodian.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Base64.Encoder;
import org.apache.commons.io.IOUtils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.google.zxing.WriterException;
import com.itextpdf.text.DocumentException;
import com.prodian.service.GeneratePdfService;
import com.prodian.service.QRCODEGenerator;

@RestController
public class GeneratePdfController {

	@Autowired
	SpringTemplateEngine templateEngine;
	
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
	public String excelReader(@RequestParam("excel") MultipartFile excel) throws DocumentException {
		
		
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
				String link = "https://my.royalsundaram.in/health-insurance/arogya-sanjeevani?agent_code=QUcwMzk5OTk";
				Encoder encoder = Base64.getEncoder();
				String originalString = rownum.toString();
				String encodedString = encoder.encodeToString(originalString.getBytes());
				link = link.split("=")[0] + "=" + encodedString + "&&encoded=true";
			//	System.out.println(originalString);
			//	System.out.println(link);
				// Generate and Return Qr Code in Byte Array
				byte[] image = new byte[0];
//				Create directory
				String path = null;
				path = "D://QR/";
				new File(path).mkdir();
				// Generate and Save Qr Code Image in static/image folder
				QRCODEGenerator.generateQRCodeImage(link, 250, 250, path+"/"+"index_"+originalString+".png");

				// FOR PDF 
				Path pdfPath = Paths.get("src\\main\\resources\\templates\\Arogya Sanjeevani Policy_pages-to-jpg-0001.jpg");
				Path pdfPath1 = Paths.get("src\\main\\resources\\templates\\Arogya Sanjeevani Policy_pages-to-jpg-0002.jpg");
				//call converttobase64 method for change image to base64 format
				String base64Image = convertToBase64(pdfPath);
				String base64Image1 = convertToBase64(pdfPath1);
				String insertImage = "data:image/png;base64, " + base64Image;
				String insertImage1 = "data:image/png;base64, " + base64Image1;
				
				Context context = new Context();
				
				context.setVariable("link", link);
				context.setVariable("image", insertImage);
				context.setVariable("image1", insertImage1);
				ITextRenderer renderer = new ITextRenderer();
				
				String htmlContentToRender = templateEngine.process("royalsundaram", context);

				renderer.setDocumentFromString(htmlContentToRender);
				renderer.layout();
				OutputStream outputStream = new FileOutputStream("D://PDF/"+"index_"+originalString + ".pdf");
				final ByteArrayOutputStream baos = new ByteArrayOutputStream();
				baos.writeTo(outputStream);
				renderer.createPDF(outputStream);
				outputStream.close();
			}
			return "successsssss";
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "SUCCESS";
	}
	
	private String convertToBase64(Path path) {
		byte[] imageAsBytes = new byte[0];
		try {
			Resource resource = new UrlResource(path.toUri());
			InputStream inputStream = resource.getInputStream();
			imageAsBytes = IOUtils.toByteArray(inputStream);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return Base64.getEncoder().encodeToString(imageAsBytes);
	}
	
}
