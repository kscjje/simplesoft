package com.simplesoft.mapper.admOrder.service;

import java.util.List;
import java.util.Map;

import com.simplesoft.order.service.OrderVO;

public interface AdmOrderService {
	public List<OrderVO> selectOrderApplyList(OrderVO vo);
	public int selectOrderApplyListCount(OrderVO vo);
	public Map<String, Object> getDashBoardCnt();
}
