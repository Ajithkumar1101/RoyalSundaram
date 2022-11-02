package com.prodian.QRcodeModel;

 
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "qrcode")
public class qrcodemodel {
 
	 @Id
	 //@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="broucher_master_seq")
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qr_id")
    private Integer qrId;
	
    @Column(name = "qr_link")
    private String qrLink;
    
    @Column(name = "qr_name")
    private String qrname;
    
}
