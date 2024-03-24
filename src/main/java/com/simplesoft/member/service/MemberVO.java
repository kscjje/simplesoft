package com.simplesoft.member.service;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberVO {
	
	private int userNo;
	private String userId;
	private String userPw;
	private String userNm;
	private String userEmail;
	private String userTel;
	private String userMobile;
	private String zipcode;
	private String address;
	private String addressDetail;
	private Date pw_change_dt;
	private Date last_login_dt;
	private String regUser;
	private Date reg_dt;
}
