package com.simplesoft.admin.controller.rest;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simplesoft.manager.service.ManagerService;
import com.simplesoft.manager.service.ManagerVO;
import com.simplesoft.reponse.BasicResponse;
import com.simplesoft.reponse.CommonResponse;
import com.simplesoft.util.EncryptUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/admin/rest")
public class AdminRestController {
	
	@Value("${domain}")
    private String domain;
	
	@Autowired
	ManagerService managerService;
	
	/**
	 * 관리자 로그인 처리
	 * @param request
	 * @param paramVO
	 * @param model
	 * @return
	 * @throws NoSuchAlgorithmException 
	 */
	@PostMapping(value = "/login/loginAjax")
	public BasicResponse loginAjax(HttpServletRequest request, ModelMap model, HttpSession session,@ModelAttribute("managerVO") ManagerVO paramVO) throws NoSuchAlgorithmException {
		log.info("관리자 로그인 시도 AJAX");
		
		ManagerVO managerDetail = managerService.selectManagerDetail(paramVO);
		Map<String, Object> returnData = new HashMap<String, Object>();
		String userPassword = paramVO.getManagerPw();
		if (managerDetail == null) {
			returnData.put("RESULT", "FAIL");
			returnData.put("PARAM_MESSAGE", "일치하는 ID가 없습니다.");
		} else {
			if (EncryptUtils.SHA512_Encrypt(userPassword).equals(managerDetail.getManagerPw())) {
				
				returnData.put("RESULT", "SUCCESS");
				returnData.put("managerId", managerDetail.getManagerId());
				session.setAttribute("adminLoginInfo",managerDetail);
			} else {
				returnData.put("RESULT", "FAIL");
				returnData.put("PARAM_MESSAGE", "비밀번호가 일치하지 않습니다.");
			}
		}
		return new CommonResponse<Map<String, Object>>(returnData);
	}
	
	/*
	@GetMapping("/qr/")
	public ResponseEntity<byte[]> qrToTistory() throws WriterException, IOException {
		//QR 정보
		int width = 100;
		int height = 100;
		String url = domain;

		//QR Code - BitMatrix: qr code 정보 생성
		BitMatrix encode = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, width, height);
		//QR Code - Image 생성. : 1회성으로 생성해야 하기 때문에
		//stream으로 Generate(1회성이 아니면 File로 작성 가능.)
		try {
			//output Stream
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			//Bitmatrix, file.format, outputStream
			MatrixToImageWriter.writeToStream(encode, "PNG", out);
			return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(out.toByteArray());
		}catch (Exception e){
			log.warn("QR Code OutputStream 도중 Excpetion 발생, {}", e.getMessage());
		}
		return null;
	}
	*/
}
