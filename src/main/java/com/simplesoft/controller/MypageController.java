package com.simplesoft.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.simplesoft.mapper.admOrder.service.AdmOrderService;
import com.simplesoft.order.service.OrderService;
import com.simplesoft.order.service.OrderVO;
import com.simplesoft.util.EncryptUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value="/mypage")
public class MypageController {
	
	@Autowired
	OrderService orderService;

	@Autowired
	AdmOrderService admOrderService;
	
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
		return "/mypage/orderDetail";
	}
}
