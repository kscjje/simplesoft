package com.simplesoft.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.simplesoft.member.service.MemberService;
import com.simplesoft.member.service.MemberVO;

@Controller
public class MainController {

	@Autowired
	MemberService memberService;
	
	@RequestMapping("/main")
	public String main() {
		MemberVO vo = new MemberVO();
		vo.setUserId("user");
		vo.setPwd("1234");
		vo.setGender("M");
		vo.setMemNm("홍길동");
		int a = memberService.insertMember(vo);
		return "main";
	}
}
