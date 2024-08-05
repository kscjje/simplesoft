package com.simplesoft.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.simplesoft.util.GlobalVariable;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author kscjje
 * @description 공통 컨트롤러
 * @since 2024.01.14
 */

@Controller
public class HomeController {
	
	@GetMapping({ "/" , "index" })
	public String home() {
		return "main";
	}
	@PostMapping({ "/hint" , "hint" })
	public String hint(HttpServletRequest request, Model model) {
		
		String hintText = request.getParameter("hintText");						//힌트코드
		String type= "";
		if("".equals(hintText) || hintText == null) {
			model.addAttribute("PARAM_MESSAGE", "힌트코드를 올바르게 입력해 주세요.");
			model.addAttribute("returnUrl", "/");
			return GlobalVariable.REDIRECT_SUBMIT;
		} else hintText = hintText.toUpperCase();
		
		if("HR01".equals(hintText)) {
			type = "01";
		} else if("HR02".equals(hintText)) {
			type = "02";
		} else if("HR03".equals(hintText)) {
			type = "03";
		} else {
			model.addAttribute("PARAM_MESSAGE", "힌트코드를 올바르게 입력해 주세요.");
			model.addAttribute("returnUrl", "/");
			return GlobalVariable.REDIRECT_SUBMIT;
		}
		System.out.println("type:" + type);
		model.addAttribute("type", type);
		return "hint";
	}
	@GetMapping({ "/hint" , "hint" })
	public String gethint() {
		return "main";
	}
	
}
