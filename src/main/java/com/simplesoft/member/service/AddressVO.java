package com.simplesoft.member.service;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddressVO {
	private int addressSeq;
	private int userNo;
	private String addressNm;
	private String zipcode;
	private String address;
	private String addressDetail;
	private String commonPwd;
	private String bigo;
	private String chooseYn;
	private Date regDt;
	private Date uptDt;
}
