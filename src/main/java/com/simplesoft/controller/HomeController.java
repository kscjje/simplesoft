package com.simplesoft.controller;



import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.simplesoft.common.service.BizBatchService;

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
	BizBatchService bizBatchService;
	
	@GetMapping({ "/" , "index" })
	public String home() {
		return "main";
	}
	@GetMapping("notice")
	public String notice() {
		return "notice";
	}
	@GetMapping("introduce")
	public String introduce() {
		return "introduce";
	}
	@GetMapping("privacy")
	public String privacy() {
		return "privacy";
	}
	@GetMapping("terms")
	public String terms() {
		return "terms";
	}
	@GetMapping("foodtable")
	public String foodtable() {
		return "foodtable";
	}
	@GetMapping("room")
	public String room() {
		return "room";
	}
	@GetMapping("room2")
	public String room2() {
		return "room2";
	}
	@GetMapping("room3")
	public String room3() {
		return "room3";
	}
	@GetMapping("room4")
	public String room4() {
		return "room4";
	}
	@GetMapping("room5")
	public String room5() {
		return "room5";
	}
	@GetMapping("room6")
	public String room6() {
		return "room6";
	}
	
	/**
	 * 메세지 발송 후 결과 받는 url
	 * 
	 * @param comcd
	 * @param params
	 * 
	 */
	@PostMapping("/callback")
	public void callback(
		@RequestBody Map<String, Object> params
	) {
		try {
			bizBatchService.updateBizBatchReport(params);
		}catch (Exception e) {
			log.error("메세지 발송 후 리포트 갱신 실패 : "+e.getMessage());
		}
	}
}
