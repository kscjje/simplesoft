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
import com.simplesoft.manager.service.ManagerVO;
import com.simplesoft.mapper.admOrder.service.AdmOrderService;
import com.simplesoft.menuboard.service.MenuBoardService;
import com.simplesoft.order.service.OrderVO;
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
	 * 주문자리스트 - 주문관리
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
	 * 주문자리스트 - 엑셀
	 * 
	 * @param model
	 * @return
	 * @throws NoSuchAlgorithmException 
	 * @throws IOException 
	 * @throws WriterException 
	 */
	@GetMapping("/orderExcel")
	public void excel(Model model,HttpSession session, HttpServletResponse response, @ModelAttribute("orderVO") OrderVO paramVO) throws NoSuchAlgorithmException, WriterException, IOException {
		log.info("param: {}",paramVO);
//		ManagerVO loginVO = (ManagerVO)session.getAttribute("adminLoginInfo");
//		if(loginVO == null) {
//			model.addAttribute("returnUrl", "/admin/login");
//			return;
//		}
		
		String excelTitle = "주문리스트";
		
		String[] columnList = {"주문상태", "결제수단", "주문번호", "주문자명", "상품명" , "주문일시", "QR코드"};
		
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
				ExcelUtils.insertImageCell(workbook, sheet, index, 6, qrToTistory(order.getOrderNo()));
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
	 * QR생성
	 * @param orderNo
	 * @return
	 * @throws WriterException
	 * @throws IOException
	 */
	public byte[] qrToTistory(String orderNo) throws WriterException, IOException {
		//QR 정보
		int width = 100;
		int height = 100;
		String url = domain + "delivery/confirm?orderNo="+orderNo;

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