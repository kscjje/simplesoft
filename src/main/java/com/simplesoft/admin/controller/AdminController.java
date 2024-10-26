package com.simplesoft.admin.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.simplesoft.manager.service.ManagerService;
import com.simplesoft.manager.service.ManagerVO;
import com.simplesoft.mapper.admOrder.AdmOrderMapper;
import com.simplesoft.mapper.admOrder.service.AdmOrderService;
import com.simplesoft.menuboard.service.MenuBoardService;
import com.simplesoft.order.service.DeliveryVO;
import com.simplesoft.order.service.OrderVO;
import com.simplesoft.util.EncryptUtils;
import com.simplesoft.util.ExcelUtils;
import com.simplesoft.util.GlobalVariable;
import com.simplesoft.util.StringUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value="/admin")
public class AdminController {
	
	@Value("${domain}")
    private String domain;
	
	@Autowired
	MenuBoardService menuboardService;
	
	@Autowired
	AdmOrderService admOrderService;
	
	@Autowired
	ManagerService managerService;
	
	@Autowired
	AdmOrderMapper admOrderMapper;
	
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
	 */
	@GetMapping("/main")
	public String adminMain(Model model,HttpSession session) {
		
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
	 * 주문자리스트 - 주문관리
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/orderManage")
	public String orderManage(Model model,HttpSession session, @RequestParam Map<String, Object> paramMap) {
		
		ManagerVO loginVO = (ManagerVO)session.getAttribute("adminLoginInfo");
		if(loginVO == null) {
			model.addAttribute("returnUrl", "/admin/login");
			return GlobalVariable.REDIRECT_SUBMIT;
		}
		if(paramMap.get("option") != null) {
			//메인 페이지 대시보드에서 넘어온 파라미터
			model.addAttribute("option", paramMap.get("option"));
		}
		return "/admin/orderManage";
	}
	
	/**
	 * 배송관리
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/deliveryManage")
	public String deliveryManage(Model model,HttpSession session, @RequestParam Map<String, Object> paramMap) {
		
		ManagerVO loginVO = (ManagerVO)session.getAttribute("adminLoginInfo");
		if(loginVO == null) {
			model.addAttribute("returnUrl", "/admin/login");
			return GlobalVariable.REDIRECT_SUBMIT;
		}
		if(paramMap.get("option") != null) {
			//메인 페이지 대시보드에서 넘어온 파라미터
			model.addAttribute("option", paramMap.get("option"));
		}
		ManagerVO vo = new ManagerVO();
		List<Map<String, Object>> manageList = managerService.selectManagerList(vo);
		model.addAttribute("manageList", manageList);
		return "/admin/deliveryManage";
	}
	/**
	 * 식단수정
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/menuReg")
	public String menuReg(Model model,HttpSession session, @RequestParam Map<String, Object> paramMap){
		
		ManagerVO loginVO = (ManagerVO)session.getAttribute("adminLoginInfo");
		if(loginVO == null) {
			model.addAttribute("returnUrl", "/admin/login");
			return GlobalVariable.REDIRECT_SUBMIT;
		}
		List<Map<String, Object>> resultMap = menuboardService.selectMenuBoardList(paramMap);
		model.addAttribute("customDate", paramMap.get("customDate"));
		model.addAttribute("resultMap", resultMap);
		return "/admin/menuReg";
	}
	/**
	 * QR코드 배송완료 처리
	 * 
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@GetMapping("/delivery/confirm")
	public String confirm(Model model,@RequestParam Map<String, Object> paramMap) throws Exception{
		
		String resultMsg = "올바르지 않은 주소입니다.";
		String param = String.valueOf(paramMap);
		if(param.indexOf(" ") > 1) param = paramMap.toString().replace(" ", "+");
		if(EncryptUtils.AES256_Decrypt(param).indexOf("deliverySeq=") > -1){
			String[] array = new String[1]; 
			array[0] = EncryptUtils.AES256_Decrypt(param).split("deliverySeq=")[1];
			paramMap.put("arrSeq", array);
			
			Map<String, Object> result = admOrderMapper.selectDeliveryDuple(paramMap);
			if(result.size() > 0) {
				if("1000".equals(result.get("status"))) {
					resultMsg = "정상적으로 배송완료 처리하였습니다.";
					admOrderService.updateDelivComplete(paramMap);
				} else if("0000".equals(result.get("status"))){
					resultMsg = "이미 완료 처리된 건 입니다.";
				}
			} else {
				log.info("배송 데이터:"+EncryptUtils.AES256_Decrypt(paramMap.toString()));
				resultMsg = "해당 데이터가 없습니다.";
			}
		}
		model.addAttribute("PARAM_MESSAGE", resultMsg);
		model.addAttribute("returnUrl", domain);
		return GlobalVariable.REDIRECT_SUBMIT;
	}
	
	/**
	 * 주문자리스트 - 엑셀
	 * 
	 * @param model
	 * @return
	 * @throws NoSuchAlgorithmException 
	 * @throws IOException 
	 * @throws WriterException 
	 */
	@GetMapping("/orderExcel")
	public void orderExcel(Model model,HttpSession session, HttpServletResponse response, @ModelAttribute("orderVO") OrderVO paramVO) throws NoSuchAlgorithmException, WriterException, IOException {
		log.info("param: {}",paramVO);
//		ManagerVO loginVO = (ManagerVO)session.getAttribute("adminLoginInfo");
//		if(loginVO == null) {
//			model.addAttribute("returnUrl", "/admin/login");
//			return;
//		}
		
		String excelTitle = "주문리스트";
		
		String[] columnList = {"주문상태", "결제수단", "주문번호", "주문자명", "상품명" , "주문일시"};
		
		//Excel 생성
		XSSFWorkbook workbook = ExcelUtils.createExcel(response, excelTitle, columnList);
		
		Row row = null; 		//행
		Cell cell = null;		//셀
		XSSFSheet sheet = workbook.getSheetAt(0);		//sheet
		XSSFCellStyle titleStyleStringCenter = workbook.getCellStyleAt(2);
		titleStyleStringCenter.setWrapText(true);
		titleStyleStringCenter.setFillForegroundColor(IndexedColors.TAN.getIndex());  
		titleStyleStringCenter.setFillPattern(FillPatternType.SOLID_FOREGROUND);  
		titleStyleStringCenter.setBorderBottom(BorderStyle.THIN);
		titleStyleStringCenter.setBorderLeft(BorderStyle.THIN);
		titleStyleStringCenter.setBorderRight(BorderStyle.THIN);
		
		Font font = workbook.createFont();
		font.setBold(true);
		titleStyleStringCenter.setFont(font);
		
		Row titleRow = sheet.getRow(0);
		titleRow.setHeight((short) 800);
		
		for(int i=0; i < columnList.length; i++) {
			titleRow.getCell(i).setCellStyle(titleStyleStringCenter);
		}
		XSSFCellStyle styleDate = workbook.getCellStyleAt(3);
		OrderVO orderVO = admOrderService.selectOrderApplyExcel(paramVO);
		if( orderVO.getOrderCount() > 0 ) {
			int index = 1;
			for(OrderVO order : orderVO.getOrderList()) {
				row = sheet.createRow(index);
				row.setHeight((short) 1200);
				for(int i=0; i < columnList.length; i++) {
					cell = row.createCell(i);
					cell.setCellStyle(styleDate);
				}
				
				if("0000".equals(order.getOrderStatus())){
					row.getCell(0).setCellValue("주문완료");
				} else if ("0000".equals(order.getOrderStatus())){
					row.getCell(0).setCellValue("주문취소");
				}
				row.getCell(1).setCellValue(order.getPayMethod());
				row.getCell(2).setCellValue(order.getOrderNo());
				row.getCell(3).setCellValue(order.getOrderName());
				if (order.getProductList().size() > 1) {
					row.getCell(4).setCellValue(order.getProductList().get(0).getMenuDay() + " 식단 외 "+ (order.getProductList().size() -1) + "건");
				} else {
					row.getCell(4).setCellValue(order.getProductList().get(0).getMenuDay());
				}
				row.getCell(5).setCellValue(order.getOrderPayDt());
				index++;
			}
		}
		
		for(int i=0;i< columnList.length;i++) {
			switch(columnList[i]) {
				case "상품명":
					sheet.setColumnWidth(i, 9000);
					break;
				default :
					sheet.setColumnWidth(i, 4300);
					break;
			}
		}
		String excelFileName = excelTitle + "_" + StringUtils.getTimestamp() +".xlsx";
		
		ExcelUtils.writeExcel(response, workbook, excelFileName);
	}
	/**
	 * 배송리스트 - 엑셀
	 * 
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@GetMapping("/delivExcel")
	public void delivExcel(Model model,HttpSession session, HttpServletResponse response, @ModelAttribute("deliveryVO") DeliveryVO paramVO) throws Exception {
		log.info("param: {}",paramVO);
//		ManagerVO loginVO = (ManagerVO)session.getAttribute("adminLoginInfo");
//		if(loginVO == null) {
//			model.addAttribute("returnUrl", "/admin/login");
//			return;
//		}
		
		String excelTitle = "배송리스트";
		
		String[] columnList = {"식단일자", "배송상태", "배송일시", "담당자", "수량" , "주문번호", "받는이", "연락처", "주소", "등록일시", "QR코드"};
		
		//Excel 생성
		XSSFWorkbook workbook = ExcelUtils.createExcel(response, excelTitle, columnList);
		
		Row row = null; 		//행
		Cell cell = null;		//셀
		XSSFSheet sheet = workbook.getSheetAt(0);		//sheet
		XSSFCellStyle titleStyleStringCenter = workbook.getCellStyleAt(2);
		titleStyleStringCenter.setWrapText(true);
		titleStyleStringCenter.setFillForegroundColor(IndexedColors.TAN.getIndex());  
		titleStyleStringCenter.setFillPattern(FillPatternType.SOLID_FOREGROUND);  
		titleStyleStringCenter.setBorderBottom(BorderStyle.THIN);
		titleStyleStringCenter.setBorderLeft(BorderStyle.THIN);
		titleStyleStringCenter.setBorderRight(BorderStyle.THIN);
		
		Font font = workbook.createFont();
		font.setBold(true);
		titleStyleStringCenter.setFont(font);
		
		Row titleRow = sheet.getRow(0);
		titleRow.setHeight((short) 800);
		
		for(int i=0; i < columnList.length; i++) {
			titleRow.getCell(i).setCellStyle(titleStyleStringCenter);
		}
		XSSFCellStyle styleDate = workbook.getCellStyleAt(3);
		DeliveryVO deliveryVO = admOrderService.getDeliveryExcel(paramVO);
		if( deliveryVO.getDeliveryCount() > 0 ) {
			int index = 1;
			for(DeliveryVO delivery : deliveryVO.getDeliveryList()) {
				row = sheet.createRow(index);
				row.setHeight((short) 1300);
				for(int i=0; i < columnList.length; i++) {
					cell = row.createCell(i);
					cell.setCellStyle(styleDate);
				}
				
				row.getCell(0).setCellValue(delivery.getMenuDay());
				row.getCell(1).setCellValue(delivery.getDeliveryStatusNm());
				row.getCell(2).setCellValue(delivery.getDeliveryDt());
				row.getCell(3).setCellValue(delivery.getManagerNm());
				row.getCell(4).setCellValue(delivery.getOrderQty()+"개");
				row.getCell(5).setCellValue(delivery.getOrderNo());
				row.getCell(6).setCellValue(delivery.getReceiveName());
				row.getCell(7).setCellValue(EncryptUtils.AES256_Decrypt(delivery.getReceivePhone()));
				row.getCell(8).setCellValue("("+delivery.getReceivePostNum()+") "+delivery.getReceiveAddr()+" "+delivery.getReceiveAddrDetail());
				row.getCell(9).setCellValue(delivery.getRegDt());
				ExcelUtils.insertImageCell(workbook, sheet, index, 10, qrToTistory(delivery.getDeliverySeq()));
				index++;
			}
		}
		
		for(int i=0;i< columnList.length;i++) {
			switch(columnList[i]) {
			case "주소":
				sheet.setColumnWidth(i, 9000);
				break;
			case "QR코드":
				sheet.setColumnWidth(i, 3800);
				break;
			default :
				sheet.setColumnWidth(i, 4300);
				break;
			}
		}
		String excelFileName = excelTitle + "_" + StringUtils.getTimestamp() +".xlsx";
		
		ExcelUtils.writeExcel(response, workbook, excelFileName);
	}
	
	/**
	 * QR생성
	 * @param orderNo
	 * @return
	 * @throws Exception 
	 */
	public byte[] qrToTistory(int deliverySeq) throws Exception {
		//QR 정보
		int width = 100;
		int height = 100;
		String url = domain + "admin/delivery/confirm?";
		url += EncryptUtils.AES256_Encrypt("deliverySeq="+deliverySeq);
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
}