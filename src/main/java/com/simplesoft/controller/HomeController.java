package com.simplesoft.controller;



import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
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
	public String home(Model model,RefundVO vo) {
		List<Map<String,Object>> list = orderService.selectAllGames(vo); 
		model.addAttribute("list", list);
		return "game";
	}
	@GetMapping({ "game" })
	public String game(Model model,RefundVO vo) {
		List<Map<String,Object>> list = orderService.selectAllGames(vo); 
		model.addAttribute("list", list);
		return "game";
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
				returnData.put("sucDt", check.getSucDt());
				returnData.put("timeToSuccess", check.getTimeToSuccess());
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
	
	@ResponseBody
	@GetMapping("/success")
	public BasicResponse success(Model model, HttpSession session) throws Exception {
		Map<String, Object> returnData = new HashMap<String, Object>();
		RefundVO vo = new RefundVO();
		String userName = (String)session.getAttribute("userName");
		vo.setUserName(userName);
		orderService.success(vo);
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
	@GetMapping("/quiz")
	public String quiz(Model model, HttpSession session,String option) throws Exception {
		if("AD10".equals(option)) {
			model.addAttribute("check", "Y");
			return "quiz";
		}else if("BF15".equals(option)) {
			model.addAttribute("check", "Y");
			return "quiz2";
		}else if("CZ20".equals(option)) {
			model.addAttribute("check", "Y");
			return "quiz3";
		}else if("MG25".equals(option)) {
			model.addAttribute("check", "Y");
			return "quiz4";
		}else if("JQ30".equals(option)) {
			model.addAttribute("check", "Y");
			return "quiz5";
		}else if("NJ35".equals(option)) {
			model.addAttribute("check", "Y");
			return "quiz6";
		}
		return "quiz";
	}
	
	/**
	 * QR생성
	 * @param orderNo
	 * @return
	 * @throws Exception 
	 */
	public byte[] qrToTistory(String val) throws Exception {
		//QR 정보
		int width = 200;
		int height = 200;
		String url = "http://kscjjeo.cafe24.com/quiz?option="+val;
		//QR Code - BitMatrix: qr code 정보 생성
		BitMatrix encode = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, width, height);
		//QR Code - Image 생성. : 1회성으로 생성해야 하기 때문에
		//stream으로 Generate(1회성이 아니면 File로 작성 가능.)
		try {
			//output Stream
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			//Bitmatrix, file.format, outputStream
			MatrixToImageWriter.writeToStream(encode, "PNG", out);
			return out.toByteArray();
		}catch (Exception e){
			log.warn("QR Code OutputStream 도중 Excpetion 발생, {}", e.getMessage());
		}
		return null;
	}
	@GetMapping("/qr")
	public ResponseEntity<byte[]> getQrCode() throws Exception {
		String val = "AD10";
	    byte[] qrImage = qrToTistory(val); // deliverySeq 예시값
	    if (qrImage == null) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.IMAGE_PNG);
	    return new ResponseEntity<>(qrImage, headers, HttpStatus.OK);
	}
	@GetMapping("/qr2")
	public ResponseEntity<byte[]> getQrCode2() throws Exception {
		String val = "BF15";
		byte[] qrImage = qrToTistory(val); // deliverySeq 예시값
		if (qrImage == null) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_PNG);
		return new ResponseEntity<>(qrImage, headers, HttpStatus.OK);
	}
	@GetMapping("/qr3")
	public ResponseEntity<byte[]> getQrCode3() throws Exception {
		String val = "CZ20";
		byte[] qrImage = qrToTistory(val); // deliverySeq 예시값
		if (qrImage == null) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_PNG);
		return new ResponseEntity<>(qrImage, headers, HttpStatus.OK);
	}
	@GetMapping("/qr4")
	public ResponseEntity<byte[]> getQrCode4() throws Exception {
		String val = "MG25";
		byte[] qrImage = qrToTistory(val); // deliverySeq 예시값
		if (qrImage == null) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_PNG);
		return new ResponseEntity<>(qrImage, headers, HttpStatus.OK);
	}
	@GetMapping("/qr5")
	public ResponseEntity<byte[]> getQrCode5() throws Exception {
		String val = "JQ30";
		byte[] qrImage = qrToTistory(val); // deliverySeq 예시값
		if (qrImage == null) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_PNG);
		return new ResponseEntity<>(qrImage, headers, HttpStatus.OK);
	}
	@GetMapping("/qr6")
	public ResponseEntity<byte[]> getQrCode6() throws Exception {
		String val = "NJ35";
		byte[] qrImage = qrToTistory(val); // deliverySeq 예시값
		if (qrImage == null) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_PNG);
		return new ResponseEntity<>(qrImage, headers, HttpStatus.OK);
	}
	
	@ResponseBody
	@PostMapping("/gameUpdate")
	public BasicResponse gameUpdate(Model model, HttpSession session,@RequestParam Map<String, Object> paramMap) throws Exception {
		Map<String, Object> returnData = new HashMap<String, Object>();
		orderService.gameUpdate(paramMap);
		return new CommonResponse<Map<String, Object>>(returnData);
	}
}
