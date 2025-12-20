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
public class MemberVO {
	
	private int userNo;
	private String userId;
	private String userPw;
	private String onepass;
	private String userNm;
	private String userEmail;
	private String userTel;
	private String userMobile;
	private String zipcode;
	private String address;
	private String addressDetail;
	private Date pwChangeDt;
	private Date lasLoginDt;
	private String regUser;
	private Date regDt;
}
