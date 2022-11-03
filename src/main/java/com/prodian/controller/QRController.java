package com.prodian.controller;

import com.google.zxing.WriterException;
import com.prodian.QRcodeModel.qrcodemodel;
import com.prodian.QRrepos.QrCodeRepository;
import com.prodian.service.QRCODEGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Base64;
import java.util.Base64.Encoder;

@RestController
public class QRController {

	@Autowired
	private QrCodeRepository qrCodeRepository;

	@Autowired
	GeneratePdfController generatePdfController;
	
	private static final String QR_CODE_IMAGE_PATH = "D://image.png";

	@PostMapping("/createqr")
	public qrcodemodel getQRCode(@RequestBody qrcodemodel model, Model modal) {

		String link = model.getQrLink();
		System.out.println(link + "demo hh23");

		String name = model.getQrname();
		qrcodemodel action = qrCodeRepository.save(model);

		qrcodemodel response = action;

		byte[] image = new byte[0];
		try {
			// Convert Byte Array into Base64 Encode String
//          String qrcode = Base64.getEncoder().encodeToString(image);
					
			
			Encoder encoder = Base64.getEncoder();
			String originalString = link.split("=")[1];
			String encodedString = encoder.encodeToString(originalString.getBytes());
			link = link.split("=")[0] + "=" + encodedString + "&&encoded=true";

			// Generate and Return Qr Code in Byte Array
			image = QRCODEGenerator.getQRCodeImage(link, 250, 250);

			// Generate and Save Qr Code Image in static/image folder
			QRCODEGenerator.generateQRCodeImage(link, 250, 250, "D:\\QR\\DEM\\" + name + ".png");

		} catch (WriterException | IOException e) {
			e.printStackTrace();
		}

		modal.addAttribute(link);
		modal.addAttribute(link);
		modal.addAttribute(link);
		response.setQrId(action.getQrId());
		response.setQrname(action.getQrname());
		response.setQrLink(link);
		return response;
	}
}
