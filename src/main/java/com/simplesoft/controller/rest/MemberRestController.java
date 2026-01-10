package com.simplesoft.controller.rest;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simplesoft.member.service.AddressService;
import com.simplesoft.member.service.AddressVO;
import com.simplesoft.member.service.MemberService;
import com.simplesoft.member.service.MemberVO;
import com.simplesoft.reponse.BasicResponse;
import com.simplesoft.reponse.CommonResponse;
import com.simplesoft.util.EncryptUtils;
import com.simplesoft.util.GlobalVariable;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/rest/member")
public class MemberRestController {

	@Autowired
	MemberService memberService;
	
	@Autowired
	AddressService addressService;
	
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
	 * 내정보 관리 비밀번호 확인
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
	
	/**
	 * 대표 배송지 설정
	 * 
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@PostMapping("/addressChoose")
	public BasicResponse addressChoose(Model model, HttpSession session, @ModelAttribute("addressVO") AddressVO vo) throws Exception {
		
		Map<String, Object> returnData = new HashMap<String, Object>();
		MemberVO loginInfo = (MemberVO)session.getAttribute("loginInfo");
		if(loginInfo == null) {
			return new CommonResponse<Map<String, Object>>(returnData);
		} else {
			vo.setUserNo(loginInfo.getUserNo());
		}
		
		addressService.updateAddressChoose(vo);
		return new CommonResponse<Map<String, Object>>(returnData);
	}
	
	/**
	 * 배송지 삭제
	 * 
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@PostMapping("/addressDelete")
	public BasicResponse addressDelete(Model model, HttpSession session, @ModelAttribute("addressVO") AddressVO vo) throws Exception {
		
		Map<String, Object> returnData = new HashMap<String, Object>();
		MemberVO loginInfo = (MemberVO)session.getAttribute("loginInfo");
		if(loginInfo == null) {
			return new CommonResponse<Map<String, Object>>(returnData);
		} else {
			vo.setUserNo(loginInfo.getUserNo());
		}
		
		addressService.deleteAddress(vo);
		return new CommonResponse<Map<String, Object>>(returnData);
	}
	
	/**
	 * 배송지 리스트
	 * 
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@PostMapping("/addressList")
	public BasicResponse addressList(Model model, HttpSession session, Map<String,Object> paramMap) throws Exception {
		
		Map<String, Object> returnData = new HashMap<String, Object>();
		MemberVO loginInfo = (MemberVO)session.getAttribute("loginInfo");
		if(loginInfo == null) {
			return new CommonResponse<Map<String, Object>>(returnData);
		}
		
		paramMap.put("userNo", loginInfo.getUserNo());
		List<Map<String, Object>> list = addressService.selectAddressList(paramMap);
		returnData.put("list", list);
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
	
	/**
	 * 배송지 등록
	 * 
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@PostMapping("/addressInsert")
	public BasicResponse addressInsert(Model model, HttpSession session, @ModelAttribute("addressVO") AddressVO vo) throws Exception {
		
		Map<String, Object> returnData = new HashMap<String, Object>();
		MemberVO loginInfo = (MemberVO)session.getAttribute("loginInfo");
		if(loginInfo == null) {
			return new CommonResponse<Map<String, Object>>(returnData);
		} else {
			vo.setUserNo(loginInfo.getUserNo());
		}
		System.out.println(vo);
		// 1. 배송지명 중복 체크
		int dupCnt = addressService.selectAddressNmCheck(vo);
		if (dupCnt > 0) {
			//-1 이나 커스텀 예외 / 코드로 처리
			returnData.put("ERROR","이미 등록된 배송지명입니다.");
		} else {
			addressService.insertAddress(vo);
		}
		return new CommonResponse<Map<String, Object>>(returnData);
	}
	
	/**
	 * 배송지 수정
	 * 
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@PostMapping("/addressUpdate")
	public BasicResponse addressUpdate(Model model, HttpSession session, @ModelAttribute("addressVO") AddressVO vo) throws Exception {
		
		Map<String, Object> returnData = new HashMap<String, Object>();
		MemberVO loginInfo = (MemberVO)session.getAttribute("loginInfo");
		if(loginInfo == null) {
			return new CommonResponse<Map<String, Object>>(returnData);
		} else {
			vo.setUserNo(loginInfo.getUserNo());
		}
		System.out.println(vo);
		// 1. 배송지명 중복 체크
		int dupCnt = addressService.selectAddressNmCheck(vo);
		if (dupCnt > 0) {
			//-1 이나 커스텀 예외 / 코드로 처리
			returnData.put("ERROR","이미 등록된 배송지명입니다.");
		} else {
			addressService.updateAddress(vo);
		}
		return new CommonResponse<Map<String, Object>>(returnData);
	}
}
