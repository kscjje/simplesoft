package com.simplesoft.mapper.admOrder;

import java.util.List;
import java.util.Map;

import com.simplesoft.config.MysqlConnMapper;
import com.simplesoft.order.service.DeliveryVO;
import com.simplesoft.order.service.OrderVO;

@MysqlConnMapper
public interface AdmOrderMapper {
	public List<OrderVO> selectOrderApplyList(OrderVO vo);
	public List<OrderVO> selectOrderApplyExcel(OrderVO vo);
	public int selectOrderApplyListCount(OrderVO vo);
	public Map<String, Object> getDashBoardCnt();
	
	public List<DeliveryVO> selectDeliveryList(DeliveryVO vo);
	public int selectDeliveryListCount(DeliveryVO vo);
}
