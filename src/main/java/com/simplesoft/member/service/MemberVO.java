package com.simplesoft.member.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberVO {
	
	private String userNo = "";
	private String pwd = "";
	private String gender = "";
	private String memNm = "";
}
