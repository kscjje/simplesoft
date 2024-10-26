package com.simplesoft.mapper.admOrder.service;

import java.util.List;
import java.util.Map;

import com.simplesoft.order.service.DeliveryVO;
import com.simplesoft.order.service.OrderVO;

public interface AdmOrderService {
	public OrderVO selectOrderApplyList(OrderVO vo);
	public OrderVO selectOrderApplyExcel(OrderVO vo);
	public Map<String, Object> getDashBoardCnt();
	public DeliveryVO getDeliveryList(DeliveryVO vo);
	public int updateDelivComplete(Map<String, Object> paramMap);
	public int updateDelivManage(Map<String, Object> paramMap);
	public List<Map<String, Object>> selectDeliveryNoneList();
	public DeliveryVO getDeliveryExcel(DeliveryVO vo);
}
