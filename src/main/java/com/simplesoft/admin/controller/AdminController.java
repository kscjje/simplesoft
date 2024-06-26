package com.simplesoft.admin.controller;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.simplesoft.manager.service.ManagerVO;
import com.simplesoft.mapper.admOrder.service.AdmOrderService;
import com.simplesoft.util.GlobalVariable;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value="/admin")
public class AdminController {
	
	@Autowired
	AdmOrderService admOrderService;
	/**
	 * 관리자 로그인 화면
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping({"/login", "", "/"})
	public String adminLogin(HttpServletRequest request, Model model) {
		
		request.getSession().invalidate();
		return "/admin/login";
	}
	
	/**
	 * 관리자 메인
	 * 
	 * @param model
	 * @return
	 * @throws NoSuchAlgorithmException 
	 */
	@GetMapping("/main")
	public String adminMain(Model model,HttpSession session) throws NoSuchAlgorithmException {
		
		ManagerVO loginVO = (ManagerVO)session.getAttribute("adminLoginInfo");
		
		if(loginVO == null) {
			model.addAttribute("returnUrl", "/admin/login");
			return GlobalVariable.REDIRECT_SUBMIT;
		}
		Map<String, Object> resultMap = admOrderService.getDashBoardCnt();
		model.addAttribute("resultMap", resultMap);
		return "/admin/main";
	}
	
	/**
	 * 관리자 메인
	 * 
	 * @param model
	 * @return
	 * @throws NoSuchAlgorithmException 
	 */
	@GetMapping("/orderManage")
	public String orderManage(Model model,HttpSession session, @RequestParam Map<String, Object> paramMap) throws NoSuchAlgorithmException {
		
		ManagerVO loginVO = (ManagerVO)session.getAttribute("adminLoginInfo");
		if(loginVO == null) {
			model.addAttribute("returnUrl", "/admin/login");
			return GlobalVariable.REDIRECT_SUBMIT;
		}
		if(paramMap.get("option") != null) {
			//메인 페이지 대시보드에서 넘어온 파라미터
			model.addAttribute("option", paramMap.get("option"));
		}
//		OrderVO orderVo = new OrderVO();
//		List<OrderVO> orderList = admOrderService.selectOrderApplyList(orderVo);
//		int orderSize = orderList.size();
//		model.addAttribute("orderList", orderList);
//		model.addAttribute("orderSize", orderSize);
		return "/admin/orderManage";
	}
}