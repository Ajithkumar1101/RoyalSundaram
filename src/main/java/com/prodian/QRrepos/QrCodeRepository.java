package com.prodian.QRrepos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prodian.QRcodeModel.qrcodemodel;

@Repository
public interface QrCodeRepository extends JpaRepository<qrcodemodel, Integer>{

	 
}