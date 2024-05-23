package com.simplesoft.mapper.admOrder.service.impl;

import java.util.List;

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
	public List<OrderVO> selectOrderApplyList(OrderVO vo) {
		return admOrderMapper.selectOrderApplyList(vo);
	}
}
