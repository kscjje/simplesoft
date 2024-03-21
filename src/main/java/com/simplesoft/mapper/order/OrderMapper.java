package com.simplesoft.mapper.order;

import com.simplesoft.config.MysqlConnMapper;
import com.simplesoft.order.service.OrderVO;

@MysqlConnMapper
public interface OrderMapper {
	public int insertOrderMst(OrderVO vo);
}
