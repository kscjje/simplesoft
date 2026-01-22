package com.simplesoft.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.simplesoft.member.service.AddressService;
import com.simplesoft.member.service.MemberVO;
import com.simplesoft.order.service.OrderService;
import com.simplesoft.order.service.OrderVO;
import com.simplesoft.util.EncryptUtils;
import com.simplesoft.util.GlobalVariable;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value="/mypage")
public class MypageController {
	
	@Value("${apiClientKey}")
	String apiClientKey;
	
	@Value("${Globals.frnt.domain}")
	private String domain;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	AddressService addressService;
	

	/**
	 * 비회원 주문조회
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/noneOrderCheck")
	public String noneOrderCheck(Model model) {
		return "/mypage/orderCheck";
	}
	
	/**
	 * 비회원 주문내역
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/noneOrderDetail")
	public String noneOrderDetail(Model model, @ModelAttribute("orderVO") OrderVO vo) throws Exception {
		String orderNo = String.valueOf(vo.getOrderNo());
		String orderPwd = String.valueOf(vo.getOrderPwd());
		
		if("null".equals(orderNo) || "null".equals(orderPwd)) { 
			return "/mypage/orderCheck";
		}
		vo.setOrderPwd(EncryptUtils.AES256_Encrypt(orderPwd));
		OrderVO order = orderService.selectOrderCheck(vo);
		if(order != null) {
			model.addAttribute("order",order);
			model.addAttribute("receivePhone",EncryptUtils.AES256_Decrypt(order.getReceivePhone()));
			model.addAttribute("refund",orderService.getRefundDetail(order));
		} else {
			return "/mypage/orderCheck";
		}
		return "/mypage/noneOrderDetail";
	}
	
	/**
	 * 주문조회
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/orderList")
	public String orderList(Model model, HttpSession session) {
		MemberVO loginInfo = (MemberVO)session.getAttribute("loginInfo");
		OrderVO vo = new OrderVO();
		if(loginInfo == null) {
			model.addAttribute("returnUrl", "/mypage/orderList");
			return GlobalVariable.REDIRECT_LOGIN;
		}
		BeanUtils.copyProperties(loginInfo, vo);
		List<Map<String, Object>> list = orderService.selectOrderList(vo);
		if(list != null) {
			model.addAttribute("order",list);
		}
		model.addAttribute("activeMenu", "order");
		return "/mypage/orderList";
	}
	/**
	 * 주문상세
	 * 
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@PostMapping("/orderDetail")
	public String orderDetail(Model model, @ModelAttribute("orderVO") OrderVO vo, HttpSession session) throws Exception {
		MemberVO loginInfo = (MemberVO)session.getAttribute("loginInfo");
		if(loginInfo == null) {
			model.addAttribute("returnUrl", "/mypage/orderList");
			return GlobalVariable.REDIRECT_LOGIN;
		}
		BeanUtils.copyProperties(loginInfo, vo);
		OrderVO order = orderService.selectOrderDetail(vo);
		if(order != null) {
			model.addAttribute("order",order);
			model.addAttribute("receivePhone",EncryptUtils.AES256_Decrypt(order.getReceivePhone()));
			model.addAttribute("refund",orderService.getRefundDetail(order));
		} else {
			model.addAttribute("PARAM_MESSAGE", "(ER_1)주문 내역이 없습니다.\n고객센터에 문의해 주세요.");
			model.addAttribute("returnUrl", "/main");
			return GlobalVariable.REDIRECT_SUBMIT;
		}
		model.addAttribute("activeMenu", "order");
		return "/mypage/orderDetail";
	}
	
	/**
	 * 내정보 관리
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/memberInfo")
	public String memberInfo(Model model, HttpSession session) {
		MemberVO loginInfo = (MemberVO)session.getAttribute("loginInfo");
		
		if(loginInfo == null) {
			model.addAttribute("returnUrl", "/mypage/memberInfo");
			return GlobalVariable.REDIRECT_LOGIN;
		}
		
		MemberVO getMember = new MemberVO();
		BeanUtils.copyProperties(loginInfo, getMember);
		
		getMember.setUserNm(EncryptUtils.maskUserName(getMember.getUserNm()));
		getMember.setUserEmail(EncryptUtils.maskEmail(getMember.getUserEmail()));
		getMember.setUserMobile(EncryptUtils.maskPhone(getMember.getUserMobile()));
		
		model.addAttribute("currentDomain", this.domain + "/snsNaverPop"); //
	    model.addAttribute("naverKey", "AT8qxy8ltmI_p6fqXbCr");
	    model.addAttribute("kakaoKey", "194cd76dd3a58aa31ea9b14709481f7a");
		model.addAttribute("getMember", getMember);
		model.addAttribute("activeMenu", "memberInfo");
		return "/mypage/memberInfo";
	}
	
	/**
	 * 배송지 관리
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/address")
	public String address(Model model, HttpSession session, Map<String,Object> paramMap) {
		MemberVO loginInfo = (MemberVO)session.getAttribute("loginInfo");
		
		if(loginInfo == null) {
			model.addAttribute("returnUrl", "/mypage/memberInfo");
			return GlobalVariable.REDIRECT_LOGIN;
		}
		paramMap.put("userNo", loginInfo.getUserNo());
		List<Map<String, Object>> list = addressService.selectAddressList(paramMap);
		
		model.addAttribute("list", list);
		model.addAttribute("activeMenu", "address");
		return "/mypage/address";
	}
	
	/**
	 * 카드 관리
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/cardInfo")
	public String cardInfo(Model model, HttpSession session, Map<String,Object> paramMap) {
		MemberVO loginInfo = (MemberVO)session.getAttribute("loginInfo");
		
		if(loginInfo == null) {
			model.addAttribute("returnUrl", "/mypage/memberInfo");
			return GlobalVariable.REDIRECT_LOGIN;
		}
		paramMap.put("userNo", loginInfo.getUserNo());
		List<Map<String, Object>> list = addressService.selectAddressList(paramMap);
		
		model.addAttribute("apiClientKey", apiClientKey);
		model.addAttribute("list", list);
		model.addAttribute("activeMenu", "cardInfo");
		return "/mypage/cardInfo";
	}
}
