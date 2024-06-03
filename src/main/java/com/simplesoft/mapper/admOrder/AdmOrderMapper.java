package com.simplesoft.mapper.admOrder;

import java.util.List;

import com.simplesoft.config.MysqlConnMapper;
import com.simplesoft.order.service.OrderVO;

@MysqlConnMapper
public interface AdmOrderMapper {
	public List<OrderVO> selectOrderApplyList(OrderVO vo);
	public int selectOrderApplyListCount(OrderVO vo);
}
