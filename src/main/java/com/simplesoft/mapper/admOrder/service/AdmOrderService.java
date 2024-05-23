package com.simplesoft.mapper.admOrder.service;

import java.util.List;

import com.simplesoft.order.service.OrderVO;

public interface AdmOrderService {
	public List<OrderVO> selectOrderApplyList(OrderVO vo);
}
