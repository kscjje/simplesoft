package com.simplesoft.admin.controller;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simplesoft.manager.service.ManagerService;
import com.simplesoft.manager.service.ManagerVO;
import com.simplesoft.member.service.MemberVO;
import com.simplesoft.reponse.BasicResponse;
import com.simplesoft.reponse.CommonResponse;
import com.simplesoft.util.EncryptUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/rest/adm")
public class AdminRestController {
	
	@Autowired
	ManagerService managerService;
	
	/**
	 * 관리자 로그인 처리
	 * @param request
	 * @param paramVO
	 * @param model
	 * @return
	 * @throws NoSuchAlgorithmException 
	 */
	@PostMapping(value = "/login/loginAjax")
	public BasicResponse loginAjax(HttpServletRequest request, ModelMap model, HttpSession session,@ModelAttribute("managerVO") ManagerVO paramVO) throws NoSuchAlgorithmException {
		log.info("관리자 로그인 시도 AJAX");
		
		ManagerVO managerDetail = managerService.selectManagerDetail(paramVO);
		Map<String, Object> returnData = new HashMap<String, Object>();
		String userPassword = paramVO.getManagerPw();
		if (managerDetail == null) {
			returnData.put("RESULT", "FAIL");
			returnData.put("PARAM_MESSAGE", "일치하는 ID가 없습니다.");
		} else {
			if (EncryptUtils.SHA512_Encrypt(userPassword).equals(managerDetail.getManagerPw())) {
				returnData.put("RESULT", "SUCCESS");
				returnData.put("managerId", managerDetail.getManagerId());
				session.setAttribute("adminLoginInfo",managerDetail);
			} else {
				returnData.put("RESULT", "FAIL");
				returnData.put("PARAM_MESSAGE", "비밀번호가 일치하지 않습니다.");
			}
		}
		return new CommonResponse<Map<String, Object>>(returnData);
	}
}
