package com.simplesoft.mapper.admOrder.service;

import java.util.Map;

import com.simplesoft.order.service.OrderVO;

public interface AdmOrderService {
	public OrderVO selectOrderApplyList(OrderVO vo);
	public Map<String, Object> getDashBoardCnt();
}
