package com.simplesoft.util;

import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class MailTemplateSender {
	
	@Autowired
	private TemplateEngine htmlTemplateEngine;
	
	/**
	 * 주문완료 메일
	* @methodName : orderCompleteMail
	* @author     : KYC
	* @date       : 2024.04.10
	*
	* @param request
	* @param resultMap
	* @throws Exception
	 */
	public void orderCompleteMail(HttpServletRequest request, Map<String, Object> resultMap) throws Exception {
		Map<String,String> map = new HashMap<String,String>();
		Map<String,Object> mailMap = new HashMap<String,Object>();
		Context context = new Context();
		
		mailMap.put("orderName", resultMap.get("orderName"));
		mailMap.put("orderNo", resultMap.get("orderNo"));
		mailMap.put("receiveName", resultMap.get("receiveName"));
		mailMap.put("receivePhone", resultMap.get("receivePhone"));
		mailMap.put("receivePostNum", resultMap.get("receivePostNum"));
		mailMap.put("receiveAddr", resultMap.get("receiveAddr"));
		mailMap.put("receiveAddrDetail", resultMap.get("receiveAddrDetail"));
		mailMap.put("bigo", resultMap.get("bigo"));
		mailMap.put("orderDt", StringUtils.toDateFormat(new Date(), "yyyy.MM.dd HH:mm"));
		
		context.setVariables(mailMap);
		
		String htmlTemplate = htmlTemplateEngine.process("mail/Mail_01.html", context);

		map.put("to", (String) resultMap.get("orderEmail"));
		map.put("subject",  "[밥수니반찬] 주문완료 안내");
		map.put("body", htmlTemplate);
		
		MailSender.send(request, map);
	}
	
	/**
	 * 임시비밀번호 메일
	* @methodName : passwordMail
	* @author     : KYC
	* @date       : 2025.12.20
	*
	* @param request
	* @param resultMap
	* @throws Exception
	 */
	public void passwordMail(HttpServletRequest request, Map<String, Object> resultMap) throws Exception {
		Map<String,String> map = new HashMap<String,String>();
		Map<String,Object> mailMap = new HashMap<String,Object>();
		Context context = new Context();
		
		mailMap.put("password", resultMap.get("password"));
		context.setVariables(mailMap);
		
		String htmlTemplate = htmlTemplateEngine.process("mail/Mail_02.html", context);

		map.put("to", (String) resultMap.get("orderEmail"));
		map.put("subject",  "[밥수니반찬] 임시 비밀번호 발급");
		map.put("body", htmlTemplate);
		
		MailSender.send(request, map);
	}
}
