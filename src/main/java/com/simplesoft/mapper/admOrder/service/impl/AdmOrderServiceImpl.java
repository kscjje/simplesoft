package com.simplesoft.mapper.admOrder.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simplesoft.mapper.admOrder.AdmOrderMapper;
import com.simplesoft.mapper.admOrder.service.AdmOrderService;
import com.simplesoft.order.service.DeliveryVO;
import com.simplesoft.order.service.OrderProductVO;
import com.simplesoft.order.service.OrderVO;

@Service
public class AdmOrderServiceImpl implements AdmOrderService{
	
	@Autowired
	AdmOrderMapper admOrderMapper;
	
	//주문신청정보
	@Override
	public OrderVO selectOrderApplyList(OrderVO vo) {
		OrderVO result = new OrderVO();
		result.setOrderList(admOrderMapper.selectOrderApplyList(vo));
		result.setOrderCount(admOrderMapper.selectOrderApplyListCount(vo));
		return result;
	}
	//주문신청정보 엑셀 
	@Override
	public OrderVO selectOrderApplyExcel(OrderVO vo) {
		OrderVO result = new OrderVO();
		result.setOrderList(admOrderMapper.selectOrderApplyExcel(vo));
		result.setOrderCount(admOrderMapper.selectOrderApplyListCount(vo));
		return result;
	}
	//대시보드 카운트
	@Override
	public Map<String, Object> getDashBoardCnt(){
		return admOrderMapper.getDashBoardCnt();
	}
	//배송 정보
	@Override
	public DeliveryVO getDeliveryList(DeliveryVO vo) {
		DeliveryVO result = new DeliveryVO();
		result.setDeliveryList(admOrderMapper.selectDeliveryList(vo));
		result.setDeliveryCount(admOrderMapper.selectDeliveryListCount(vo));
		return result;
	}
	//배송 완료처리
	@Override
	public int updateDelivComplete(Map<String, Object> paramMap){
		return admOrderMapper.updateDelivComplete(paramMap);
	}
}
