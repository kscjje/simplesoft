package com.simplesoft.controller;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.simplesoft.room.service.RoomService;
import com.simplesoft.util.GlobalVariable;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * @author kscjje
 * @description 공통 컨트롤러
 * @since 2024.01.14
 */

@Controller
public class HomeController {
	
	@Autowired
	RoomService roomService;
	
	@RequestMapping({ "/" , "index" })
	public String home(HttpServletResponse res,String value,Model model,@CookieValue(value = "room", required = false) Cookie cookie) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		int hintCnt = 0;
		if(cookie != null) {
			//cookieValue 변수에 쿠키 값을 저장한다.
			paramMap.put("userName", cookie.getValue());
			Map<String, Object> result = roomService.selectRoomDetail(paramMap);
			
			String hint1 = (String)result.get("hint1");
			String hint2 = (String)result.get("hint2");
			String hint3 = (String)result.get("hint3");
			String hint4 = (String)result.get("hint4");
			String hint5 = (String)result.get("hint5");
			String hint6 = (String)result.get("hint6");
			
			if("Y".equals(hint1)) hintCnt ++;	
			if("Y".equals(hint2)) hintCnt ++;	
			if("Y".equals(hint3)) hintCnt ++;	
			if("Y".equals(hint4)) hintCnt ++;	
			if("Y".equals(hint5)) hintCnt ++;	
			if("Y".equals(hint6)) hintCnt ++;	
		} else {
			if(!"".equals(value) && value != null ) {
				Cookie cookie2 = new Cookie("room", value); // 쿠키 이름 지정하여 생성( key, value 개념)
				cookie2.setMaxAge(60*60*24); //쿠키 유효 기간: 하루로 설정(60초 * 60분 * 24시간)
				cookie2.setPath("/"); //모든 경로에서 접근 가능하도록 설정
				res.addCookie(cookie2); //response에 Cookie 추가
				
				paramMap.put("userName", value);
				roomService.insertRoom(paramMap);
				
				Map<String, Object> result = roomService.selectRoomDetail(paramMap);
				
				String hint1 = (String)result.get("hint1");
				String hint2 = (String)result.get("hint2");
				String hint3 = (String)result.get("hint3");
				String hint4 = (String)result.get("hint4");
				String hint5 = (String)result.get("hint5");
				String hint6 = (String)result.get("hint6");
				
				if("Y".equals(hint1)) hintCnt ++;	
				if("Y".equals(hint2)) hintCnt ++;	
				if("Y".equals(hint3)) hintCnt ++;	
				if("Y".equals(hint4)) hintCnt ++;
				if("Y".equals(hint5)) hintCnt ++;
				if("Y".equals(hint6)) hintCnt ++;
				
			} else {
				return "create";
			}
		}
		model.addAttribute("hintCnt", hintCnt);
		return "main";
	}
	@GetMapping({ "/cook"})
	public String cook(HttpServletResponse res, String value,Model model) {
		if(value == null || "".equals(value)) {
			return "main";
		}
		Cookie cookie = new Cookie("room", value); // 쿠키 이름 지정하여 생성( key, value 개념)
		cookie.setMaxAge(60*60*24); //쿠키 유효 기간: 하루로 설정(60초 * 60분 * 24시간)
		cookie.setPath("/"); //모든 경로에서 접근 가능하도록 설정
		res.addCookie(cookie); //response에 Cookie 추가
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userName", value);
		roomService.insertRoom(paramMap);
		
		model.addAttribute("msg", value);
		return "main";
	}
	@PostMapping({ "/hint" , "hint" })
	public String hint(HttpServletRequest request, Model model, HttpSession session,@CookieValue(value = "room", required = false) Cookie cookie) {
		
		
		String hintText = request.getParameter("hintText");						//힌트코드
		String type= "";
		
		if("".equals(hintText) || hintText == null) {
			model.addAttribute("PARAM_MESSAGE", "힌트코드를 올바르게 입력해 주세요.");
			model.addAttribute("returnUrl", "/");
			return GlobalVariable.REDIRECT_SUBMIT;
		} else hintText = hintText.toUpperCase();
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if("HR5149".equals(hintText)) {
			type = "01";
		} else if("HR3575".equals(hintText)) {
			type = "02";
		} else if("HR7031".equals(hintText)) {
			type = "03";
		} else if("HR6078".equals(hintText)) {
			type = "04";
		} else if("HR5771".equals(hintText)) {
			type = "05";
		} else if("HR0283".equals(hintText)) {
			type = "06";
		} else {
			model.addAttribute("PARAM_MESSAGE", "힌트코드를 올바르게 입력해 주세요.");
			model.addAttribute("returnUrl", "/");
			return GlobalVariable.REDIRECT_SUBMIT;
		}
		paramMap.put("type", type);
		if(cookie != null) {
			//cookieValue 변수에 쿠키 값을 저장한다.
			paramMap.put("userName", cookie.getValue());
		}
		roomService.updateRoom(paramMap);
		model.addAttribute("type", type);
		return "hint";
	}
	@GetMapping({ "/hint" , "hint" })
	public String gethint() {
		return "main";
	}
	
	@GetMapping({ "/clear"})
	public String clear(HttpServletResponse res,@CookieValue(value = "room", required = false) Cookie cookie) {
		if(cookie != null) {
			cookie.setMaxAge(0);
			res.addCookie(cookie); //response에 Cookie 추가
		}
		return "create";
	}
	@GetMapping({ "/info"})
	public String info() {
		
		return "info";
	}
	
	@PostMapping(value = "/finalAjax")
	public @ResponseBody Map<String, Object> finalAjax(HttpServletRequest request,
			ModelMap model, HttpSession session,@CookieValue(value = "room", required = false) Cookie cookie) throws NoSuchAlgorithmException {
		if(cookie != null) {
			//cookieValue 변수에 쿠키 값을 저장한다.
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("suc","01");
			paramMap.put("userName", cookie.getValue());
			roomService.updateRoom(paramMap);
		}
		Map<String, Object> returnData = new HashMap<String, Object>();
		String v_final = (String)request.getParameter("final");
		if(v_final != null) {
			v_final = v_final.replaceAll(" ", "");
			if("jesuslovesyou".equals(v_final)) {
				returnData.put("RESULT", "SUCCESS");
				returnData.put("detail", roomService.detail(cookie.getValue()));
			} else {
				returnData.put("RESULT", "FAIL");
			}
		} else {
			returnData.put("RESULT", "FAIL");
		}
		
		return returnData;
	}
	
	@GetMapping({ "/list"})
	public String list(HttpServletResponse res, String value,Model model) {
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list = roomService.list();
		model.addAttribute("list",list);
		return "list";
	}
}
