package com.simplesoft.mapper.admOrder.service;

import java.util.Map;

import com.simplesoft.order.service.DeliveryVO;
import com.simplesoft.order.service.OrderVO;

public interface AdmOrderService {
	public OrderVO selectOrderApplyList(OrderVO vo);
	public OrderVO selectOrderApplyExcel(OrderVO vo);
	public Map<String, Object> getDashBoardCnt();
	public DeliveryVO getDeliveryList(DeliveryVO vo);
}
