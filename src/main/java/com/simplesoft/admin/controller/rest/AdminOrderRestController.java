package com.simplesoft.admin.controller.rest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.simplesoft.mapper.admOrder.service.AdmOrderService;
import com.simplesoft.menuboard.service.MenuBoardService;
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
	@Autowired
	MenuBoardService menuBoardService;
	
	//주문 내역 조회
	@PostMapping(value = "/searchList")
	public BasicResponse searchListAjax(@ModelAttribute("orderVO") OrderVO paramVO) {
		Map<String, Object> returnData = new HashMap<String, Object>();
		System.out.println(paramVO);
		OrderVO orderVO = admOrderService.selectOrderApplyList(paramVO);
		returnData.put("orderList", orderVO.getOrderList());
		returnData.put("orderSize", orderVO.getOrderCount());
		return new CommonResponse<Map<String, Object>>(returnData);
	}
	
	//식단표 등록 및 수정
	@PostMapping(value = "/saveMenu")
	public BasicResponse saveMenuAjax(@RequestParam Map<String, Object> paramMap){
		Map<String, Object> returnData = new HashMap<String, Object>();
		
		int suc = 0;
		Map<String, Object> detail = menuBoardService.selectMenuBoardDuple(paramMap);
		if(detail != null) {
			paramMap.put("menuBoardSeq", detail.get("menuBoardSeq"));
			suc = menuBoardService.updateMenuBoard(paramMap);
		} else {
			suc = menuBoardService.insertMenuBoard(paramMap);
		}
		log.info("suc:"+suc);
	
		if(suc > 0) returnData.put("resultCode", "success"); 
		return new CommonResponse<Map<String, Object>>(returnData);
	}
	
	//식단표 데이터 삭제
	@PostMapping(value = "/removeMenu")
	public BasicResponse removeMenuAjax(@RequestParam Map<String, Object> paramMap){
		Map<String, Object> returnData = new HashMap<String, Object>();
		if(!"null".equals(String.valueOf(paramMap.get("menuBoardSeq")))){
			int suc = menuBoardService.deleteMenuBoard(paramMap);
			if(suc > 0) returnData.put("resultCode", "0000");
		}
	
		return new CommonResponse<Map<String, Object>>(returnData);
	}
	//식단표 데이터 삭제
	@PostMapping(value = "/useYnUpdate")
	public BasicResponse useYnUpdateAjax(@RequestParam Map<String, Object> paramMap){
		Map<String, Object> returnData = new HashMap<String, Object>();
		if(!"null".equals(String.valueOf(paramMap.get("menuBoardSeq")))){
			int suc = menuBoardService.updateMenuBoardUseYn(paramMap);
			if(suc > 0) returnData.put("resultCode", "0000");
		}
		
		return new CommonResponse<Map<String, Object>>(returnData);
	}
}
