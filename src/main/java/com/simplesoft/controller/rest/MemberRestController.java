package com.simplesoft.controller.rest;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simplesoft.member.service.MemberService;
import com.simplesoft.member.service.MemberVO;
import com.simplesoft.reponse.BasicResponse;
import com.simplesoft.reponse.CommonResponse;
import com.simplesoft.util.EncryptUtils;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/rest/member")
public class MemberRestController {

	@Autowired
	MemberService memberService;
	
	private static final String CHARSET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	private static final SecureRandom RANDOM = new SecureRandom();
	
	/**
	 * 아이디 중복체크
	 * 
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@PostMapping("/idCheck")
	public BasicResponse getIdCheck(Model model, HttpSession session, @ModelAttribute("memberVO") MemberVO vo) throws Exception {
		
		Map<String, Object> returnData = new HashMap<String, Object>();
		Map<?,?> id = memberService.selectIdCheck(vo);
		returnData.put("id", id);
		
		return new CommonResponse<Map<String, Object>>(returnData);
	}
	
	/**
	 * 회원 저장
	 * 
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@PostMapping("/save")
	public BasicResponse memberInsert(Model model, HttpSession session, @ModelAttribute("memberVO") MemberVO vo) throws Exception {
		Map<?,?> returnData = memberService.memberSave(vo);
		if("0000".equals(String.valueOf(returnData.get("RESULT")))){
			session.setAttribute("serviceYn", "");
		}
		return new CommonResponse<Map<?, ?>>(returnData);
	}
	
	/**
	 * 아이디 찾기
	 * 
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@PostMapping("/findIdAjax")
	public BasicResponse findIdAjax(Model model, HttpSession session, @ModelAttribute("memberVO") MemberVO vo) throws Exception {
		
		Map<String, Object> returnData = new HashMap<String, Object>();
		Map<?,?>  user = memberService.selectFindIdCheck(vo);
		if(user != null) {
			returnData.put("userId", user.get("userId"));
			returnData.put("regDt", user.get("regDt"));
		}
		return new CommonResponse<Map<String, Object>>(returnData);
	}
	
	/**
	 * 비밀번호 찾기
	 * 
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@PostMapping("/memberInfoAjax")
	public BasicResponse findPasswordAjax(Model model, HttpSession session, @ModelAttribute("memberVO") MemberVO vo) throws Exception {
		
 		Map<String, Object> returnData = new HashMap<String, Object>();
		MemberVO loginInfo = (MemberVO)session.getAttribute("loginInfo");
		if(loginInfo == null) {
			returnData.put("RESULT", "NONE");
			return new CommonResponse<Map<String, Object>>(returnData);
		} else {
			vo.setUserId(loginInfo.getUserId());
		}
		String userPassword = vo.getUserPw();
		MemberVO memDetail = memberService.selectMemberDetail(vo);
		if (EncryptUtils.SHA512_Encrypt(userPassword).equals(memDetail.getUserPw())) {
			returnData.put("RESULT", "SUCCESS");
			session.setAttribute("memberInfo",memDetail);
		} else {
			returnData.put("RESULT", "FAIL");
		}
		return new CommonResponse<Map<String, Object>>(returnData);
	}
	public static String random8() {
		StringBuilder sb = new StringBuilder(8);
		for (int i = 0; i < 8; i++) {
			int idx = RANDOM.nextInt(CHARSET.length());
			sb.append(CHARSET.charAt(idx));
		}
		return sb.toString();
	}
}
