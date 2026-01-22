package com.simplesoft.admin.controller.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.simplesoft.common.service.impl.TossServiceImpl;
import com.simplesoft.manager.service.ManagerService;
import com.simplesoft.manager.service.ManagerVO;
import com.simplesoft.mapper.admOrder.service.AdmOrderService;
import com.simplesoft.menuboard.service.MenuBoardService;
import com.simplesoft.order.service.DeliveryVO;
import com.simplesoft.order.service.OrderProductVO;
import com.simplesoft.order.service.OrderVO;
import com.simplesoft.payments.service.PaymentsService;
import com.simplesoft.reponse.BasicResponse;
import com.simplesoft.reponse.CommonResponse;
import com.simplesoft.util.EncryptUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/admin/rest/order")
public class AdminOrderRestController {

	@Autowired
	AdmOrderService admOrderService;
	@Autowired
	MenuBoardService menuBoardService;
	@Autowired
	ManagerService managerService;
	@Autowired
	PaymentsService paymentsService;
	@Autowired
	TossServiceImpl tossService;
	
	//주문 내역 조회
	@PostMapping(value = "/searchList")
	public BasicResponse searchListAjax(@ModelAttribute("orderVO") OrderVO paramVO) {
		Map<String, Object> returnData = new HashMap<String, Object>();
		if("0002".equals(paramVO.getOrderStatus())){
			//환불신청
			paramVO.setOrderStatus("");
			paramVO.setRefundStatus("0000");
		} else if ("0003".equals(paramVO.getOrderStatus())){
			//환불완료
			paramVO.setOrderStatus("");
			paramVO.setRefundStatus("2001");
		}  else if ("0004".equals(paramVO.getOrderStatus())){
			//환불반려
			paramVO.setOrderStatus("");
			paramVO.setRefundStatus("1001");
		}
		
		OrderVO orderVO = admOrderService.selectOrderApplyList(paramVO);
		returnData.put("orderList", orderVO.getOrderList());
		returnData.put("orderSize", orderVO.getOrderCount());
		return new CommonResponse<Map<String, Object>>(returnData);
	}
	
	//식단표 등록 및 수정
	@PostMapping(value = "/saveMenu")
	public BasicResponse saveMenuAjax(@RequestParam Map<String, Object> paramMap){
		Map<String, Object> returnData = new HashMap<String, Object>();
		
		int suc = 0;
		Map<String, Object> detail = menuBoardService.selectMenuBoardDuple(paramMap);
		if(detail != null) {
			paramMap.put("menuBoardSeq", detail.get("menuBoardSeq"));
			suc = menuBoardService.updateMenuBoard(paramMap);
		} else {
			suc = menuBoardService.insertMenuBoard(paramMap);
		}
		log.info("suc:"+suc);
	
		if(suc > 0) returnData.put("resultCode", "success"); 
		return new CommonResponse<Map<String, Object>>(returnData);
	}
	//주문 상품 내역 조회
	@PostMapping(value = "/searchProductList")
	public BasicResponse searchProductListAjax(@ModelAttribute("orderVO") OrderProductVO paramVO) {
		Map<String, Object> returnData = new HashMap<String, Object>();
		List<OrderProductVO> orderProductVO = admOrderService.getOrderProductInfo(paramVO);
		returnData.put("orderProductList", orderProductVO);
		return new CommonResponse<Map<String, Object>>(returnData);
	}
	//식단표 데이터 삭제
	@PostMapping(value = "/removeMenu")
	public BasicResponse removeMenuAjax(@RequestParam Map<String, Object> paramMap){
		Map<String, Object> returnData = new HashMap<String, Object>();
		if(!"null".equals(String.valueOf(paramMap.get("menuBoardSeq")))){
			int suc = menuBoardService.deleteMenuBoard(paramMap);
			if(suc > 0) returnData.put("resultCode", "0000");
		}
	
		return new CommonResponse<Map<String, Object>>(returnData);
	}
	//식단표 데이터 삭제
	@PostMapping(value = "/useYnUpdate")
	public BasicResponse useYnUpdateAjax(@RequestParam Map<String, Object> paramMap){
		Map<String, Object> returnData = new HashMap<String, Object>();
		if(!"null".equals(String.valueOf(paramMap.get("menuBoardSeq")))){
			int suc = menuBoardService.updateMenuBoardUseYn(paramMap);
			if(suc > 0) returnData.put("resultCode", "0000");
		}
		
		return new CommonResponse<Map<String, Object>>(returnData);
	}
	
	//배송 내역 조회
	@PostMapping(value = "/deliverySearchList")
	public BasicResponse deliverySearchListAjax(@ModelAttribute("deliveryVO") DeliveryVO paramVO) {
		Map<String, Object> returnData = new HashMap<String, Object>();
		DeliveryVO deliveryVO = admOrderService.getDeliveryList(paramVO);
		returnData.put("deliveryList", deliveryVO.getDeliveryList());
		returnData.put("deliverySize", deliveryVO.getDeliveryCount());
		return new CommonResponse<Map<String, Object>>(returnData);
	}
	
	/**
	 * 배송완료 처리
	 * @param paramMap
	 * @return
	 */
	@PostMapping("/delivery/complete")
	public BasicResponse cartDelete(@RequestParam Map<String, Object> paramMap) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		String[] array = ((String)paramMap.get("checkArray")).split(",");
		paramMap.put("arrSeq", array);
		
		int suc = admOrderService.updateDelivComplete(paramMap);
		if(suc > 0) result.put("resultCode", "SUCCESS");
		
		return new CommonResponse<Map<String, Object>>(result);
	}
	
	/**
	 * 배정 리스트
	 * @param paramMap
	 * @return
	 */
	@PostMapping("/delivery/manageList")
	public BasicResponse manageList(@ModelAttribute("deliveryVO") DeliveryVO paramVO) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		ManagerVO vo = new ManagerVO();
		List<Map<String, Object>> manageList = managerService.selectManagerList(vo);
		List<Map<String, Object>> noneList = admOrderService.selectDeliveryNoneList(paramVO);
		
		result.put("list", manageList);
		result.put("noneList", noneList);
		result.put("resultCode", "SUCCESS");
		return new CommonResponse<Map<String, Object>>(result);
	}
	
	/**
	 * 배정 하기
	 * @param paramMap
	 * @return
	 */
	@PostMapping("/delivery/manageAjax")
	public BasicResponse manageAjax(@RequestParam Map<String, Object> paramMap) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		String[] array = ((String)paramMap.get("checkArray")).split(",");
		paramMap.put("arrSeq", array);
		
		int suc = admOrderService.updateDelivManage(paramMap);
		if(suc > 0) result.put("resultCode", "SUCCESS");
		
		return new CommonResponse<Map<String, Object>>(result);
	}
	
	//복호화
	@PostMapping("/decrypt")
	public BasicResponse decryptData(@RequestParam String encryptedData) throws Exception {
		//Call the utility method to decrypt the data
		Map<String, Object> returnData = new HashMap<String, Object>();
		String decryptedData = EncryptUtils.AES256_Decrypt(encryptedData);
		returnData.put("decryptedData", decryptedData);
		return new CommonResponse<Map<String, Object>>(returnData);
	}
	
	//주문 결제 취소
	@PostMapping(value = "/payCancelAjax")
	public BasicResponse payCancelAjax(@RequestParam Map<String, Object> paramMap) throws ParseException {
		
		JSONObject returnData = new JSONObject();
		paramMap.put("cancelReason", "관리자 취소");
		returnData  = tossService.cancel(paramMap);
		if(returnData.get("cancels") != null) {
			paymentsService.getPayResultCancel(returnData);
		}
		return new CommonResponse<JSONObject>(returnData);
	}
}
