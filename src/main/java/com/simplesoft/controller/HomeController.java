package com.simplesoft.controller;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.simplesoft.order.service.OrderService;
import com.simplesoft.order.service.RefundVO;
import com.simplesoft.reponse.BasicResponse;
import com.simplesoft.reponse.CommonResponse;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

/**
 * @author kscjje
 * @description 공통 컨트롤러
 * @since 2024.01.14
 */
@Slf4j
@Controller
public class HomeController {
	
	@Autowired
	OrderService orderService;
	
	@GetMapping({ "/" , "index" })
	public String home() {
		return "room";
	}
	@GetMapping("room")
	public String room() {
		return "room";
	}
	
	@ResponseBody
	@PostMapping("/insert")
	public BasicResponse insert(Model model, HttpSession session, @ModelAttribute("refundVO") RefundVO vo) throws Exception {
		Map<String, Object> returnData = new HashMap<String, Object>();
		if(vo.getUserName() != null) {
			session.setAttribute("userName", vo.getUserName());
			RefundVO check = orderService.selectOrderCheck(vo);
			if(check == null) {
				orderService.insertUser(vo);
			} else {
				returnData.put("RET_CODE", "DUPLE");
			}
		}
		return new CommonResponse<Map<String, Object>>(returnData);
	}
	@ResponseBody
	@PostMapping("/change")
	public BasicResponse change(Model model, HttpSession session, @ModelAttribute("refundVO") RefundVO vo) throws Exception {
		Map<String, Object> returnData = new HashMap<String, Object>();
		if(vo.getUserName() != null) {
			RefundVO check = orderService.selectOrderCheck(vo);
			if(check == null) {
				returnData.put("RET_CODE", "NOT");
			} else {
				session.setAttribute("userName", vo.getUserName());
			}
		}
		return new CommonResponse<Map<String, Object>>(returnData);
	}
	
	@ResponseBody
	@PostMapping("/start")
	public BasicResponse start(Model model, HttpSession session, @ModelAttribute("refundVO") RefundVO vo) throws Exception {
		Map<String, Object> returnData = new HashMap<String, Object>();
		
		if(session.getAttribute("userName") != null) {
			String userName = (String)session.getAttribute("userName");
			vo.setUserName(userName);
			RefundVO check = orderService.selectOrderCheck(vo);
			if(check != null) {
				if(check.getRegDt() == null) {
					orderService.updateTime(vo);
				} else {
					returnData.put("regDt", check.getRegDt());
				}
			} else {
				returnData.put("RET_CODE","FAIL");
			}
		} else {
			returnData.put("RET_CODE","FAIL");
		}
		return new CommonResponse<Map<String, Object>>(returnData);
	}
	
	@ResponseBody
	@GetMapping("/load")
	public BasicResponse load(Model model, HttpSession session, @ModelAttribute("refundVO") RefundVO vo) throws Exception {
		 
		Map<String, Object> returnData = new HashMap<String, Object>();
		String userName = (String)session.getAttribute("userName");
		if(userName != null) {
			vo.setUserName(userName);
			RefundVO check = orderService.selectOrderCheck(vo);
			if(check != null && check.getRegDt() != null) {
				returnData.put("regDt", check.getRegDt());
				returnData.put("hint1", check.getHint1());
				returnData.put("hint2", check.getHint2());
				returnData.put("hint3", check.getHint3());
				returnData.put("hint4", check.getHint4());
				returnData.put("hint5", check.getHint5());
			}
		}
		return new CommonResponse<Map<String, Object>>(returnData);
	}
	
	@ResponseBody
	@GetMapping("/useHint")
	public BasicResponse useHint(Model model, HttpSession session,@RequestParam String index) throws Exception {
		Map<String, Object> returnData = new HashMap<String, Object>();
		RefundVO vo = new RefundVO();
		String userName = (String)session.getAttribute("userName");
		if("0".equals(index)) {
			vo.setHint1("1");
		}else if("1".equals(index)) {
			vo.setHint2("1");
		}else if("2".equals(index)) {
			vo.setHint3("1");
		}else if("3".equals(index)) {
			vo.setHint4("1");
		}else if("4".equals(index)) {
			vo.setHint5("1");
		}else if("5".equals(index)) {
			vo.setHint6("1");
		}
		vo.setUserName(userName);
		orderService.updateHint(vo);
		RefundVO check = orderService.selectOrderCheck(vo);
		if(check != null && check.getRegDt() != null) {
			returnData.put("regDt", check.getRegDt());
			returnData.put("hint1", check.getHint1());
			returnData.put("hint2", check.getHint2());
			returnData.put("hint3", check.getHint3());
			returnData.put("hint4", check.getHint4());
			returnData.put("hint5", check.getHint5());
		}
		return new CommonResponse<Map<String, Object>>(returnData);
	}
	@GetMapping("/logout")
	public String logout(Model model, HttpSession session) throws Exception {
		
		session.invalidate();
		return "room";
	}
	
	@GetMapping("/kscjje")
	public String admin(Model model, HttpSession session) throws Exception {
		RefundVO vo = new RefundVO();
		List<Map<String,Object>> list  = orderService.selectAll(vo);
		model.addAttribute("list",list);
		return "admin";
	}
}
