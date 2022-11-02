package com.prodian.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.prodian.service.GeneratePdfService;

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

	
}
