package com.simplesoft.mapper.admOrder.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simplesoft.mapper.admOrder.AdmOrderMapper;
import com.simplesoft.mapper.admOrder.service.AdmOrderService;
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
	//주문신청정보 카운트 
	@Override
	public Map<String, Object> getDashBoardCnt(){
		return admOrderMapper.getDashBoardCnt();
	}
}
