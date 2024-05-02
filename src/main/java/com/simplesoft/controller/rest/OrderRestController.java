package com.simplesoft.controller.rest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simplesoft.member.service.MemberVO;
import com.simplesoft.order.service.OrderService;
import com.simplesoft.order.service.OrderVO;
import com.simplesoft.reponse.BasicResponse;
import com.simplesoft.reponse.CommonResponse;
import com.simplesoft.util.EncryptUtils;
import com.simplesoft.util.GlobalVariable;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/rest/order")
public class OrderRestController {
	
	@Autowired
	OrderService orderService;
	
	/**
	 * 주문하기
	 * 
	 * @param model
	 * @return
	 */
	@PostMapping("/orderPayment")
	public BasicResponse order(Model model, HttpSession session, @ModelAttribute("orderVO") OrderVO vo) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		int userNo = 0;
		String userNm = "";
		
		MemberVO loginInfo = (MemberVO)session.getAttribute("loginInfo");
		if(loginInfo != null) {
			userNo = loginInfo.getUserNo();
			userNm = loginInfo.getUserNm();
		} else {
			String userSession = (String)session.getAttribute("noneMember");
			if(userSession != null) {
				vo.setUserSession(userSession);
				userNm = "비회원";
			} else {
				result.put("resultCode", "9999");
				result.put("resultMsg", "비회원정보가 없습니다.");
				result.put("returnUrl","/cart");
				return new CommonResponse<Map<String, Object>>(result);
			}
		}
		vo.setRegUser(userNm);
		vo.setUserNo(userNo);
//		vo.setOrderStatus(GlobalVariable.ORDER_STATUS_APPLY); 								//주문상태-주문접수
		
		OrderVO order = orderService.selectOrderApplyInfo(vo);
		if (order == null) {
			result.put("resultCode", "9999");
			result.put("resultMsg", "주문정보가 없습니다.");
			result.put("returnUrl","/cart");
		} else if ("0000".equals(order.getOrderStatus())) {
			result.put("resultCode", "9999");
			result.put("resultMsg", "이미 결제 완료된 주문입니다.");
			result.put("returnUrl","/cart");
		} else {
			try {
				vo.setOrderPhone(EncryptUtils.AES256_Encrypt(vo.getOrderPhone()));
				vo.setReceivePhone(EncryptUtils.AES256_Encrypt(vo.getReceivePhone()));
				if(!"".equals(vo.getOrderTel()) && vo.getOrderTel() != null){
					vo.setOrderTel(EncryptUtils.AES256_Encrypt(vo.getOrderTel()));
				}
				if(!"".equals(vo.getReceiveTel()) && vo.getReceiveTel() != null){
					vo.setReceiveTel(EncryptUtils.AES256_Encrypt(vo.getReceiveTel()));
				}
				vo.setOrderPwd(EncryptUtils.AES256_Encrypt(vo.getOrderPwd()));
				orderService.updateOrderApplyInfo(vo);
				result.put("resultCode", "SUCCESS");
			}catch(Exception e) {
				e.printStackTrace();
				result.put("resultCode", "9999");
				result.put("resultMsg", "오류가 발생하였습니다.");
				result.put("returnUrl","/cart");
			}
		}
		return new CommonResponse<Map<String, Object>>(result);
	}
}
