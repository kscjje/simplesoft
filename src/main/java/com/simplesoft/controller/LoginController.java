package com.simplesoft.controller;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.simplesoft.member.service.MemberService;
import com.simplesoft.member.service.MemberVO;
import com.simplesoft.util.EncryptUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

	private static final Logger log = LogManager.getLogger(LoginController.class);
	
	
	@Autowired
	MemberService memberService;
	
	/**
	 * 로그인 처리
	 * @param request
	 * @param paramVO
	 * @param model
	 * @return
	 * @throws NoSuchAlgorithmException 
	 */
	@PostMapping(value = "/login/loginAjax")
	public @ResponseBody Map<String, Object> loginAjax(HttpServletRequest request,
			@ModelAttribute(value = "paramVO") MemberVO paramVO, ModelMap model, HttpSession session) throws NoSuchAlgorithmException {
		log.debug("로그인 시도 AJAX");
		MemberVO memDetail = memberService.selectMemberDetail(paramVO);
		Map<String, Object> returnData = new HashMap<String, Object>();
		String userPassword = paramVO.getUserPw();
		if (memDetail == null) {
			returnData.put("RESULT", "FAIL");
			returnData.put("PARAM_MESSAGE", "일치하는 ID가 없습니다.");
		} else {
			if (EncryptUtils.SHA512_Encrypt(userPassword).equals(memDetail.getUserPw())) {
				returnData.put("RESULT", "SUCCESS");
				returnData.put("mberId", memDetail.getUserId());
				session.setAttribute("loginInfo",memDetail);
			} else {
				returnData.put("RESULT", "FAIL");
				returnData.put("PARAM_MESSAGE", "비밀번호가 일치하지 않습니다.");
			}
		}
		return returnData;
	}
	
	/**
	 * 로그아웃 처리
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/logout")
	public String memberLogout(HttpServletRequest request) {
		request.getSession().invalidate();
		return "redirect:/";
	}
}
