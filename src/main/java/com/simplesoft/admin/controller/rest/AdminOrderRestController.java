package com.simplesoft.admin.controller.rest;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simplesoft.mapper.admOrder.service.AdmOrderService;
import com.simplesoft.order.service.OrderVO;
import com.simplesoft.reponse.BasicResponse;
import com.simplesoft.reponse.CommonResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/admin/rest/order")
public class AdminOrderRestController {

	@Autowired
	AdmOrderService admOrderService;
	
	
	//주문 내역 조회
	@PostMapping(value = "/searchList")
	public BasicResponse loginAjax(HttpServletRequest request, ModelMap model, HttpSession session,@ModelAttribute("orderVO") OrderVO paramVO) throws NoSuchAlgorithmException {
		Map<String, Object> returnData = new HashMap<String, Object>();
		
		List<OrderVO> orderList = admOrderService.selectOrderApplyList(paramVO);
		int orderSize = admOrderService.selectOrderApplyListCount(paramVO);
		returnData.put("orderList", orderList);
		returnData.put("orderSize", orderSize);
		return new CommonResponse<Map<String, Object>>(returnData);
	}
}
