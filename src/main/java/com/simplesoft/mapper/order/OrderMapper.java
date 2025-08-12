package com.simplesoft.mapper.order;

import java.util.List;
import java.util.Map;

import com.simplesoft.config.MysqlConnMapper;
import com.simplesoft.order.service.RefundVO;

@MysqlConnMapper
public interface OrderMapper {
	public RefundVO selectOrderCheck(RefundVO vo);
	public int insertUser(RefundVO vo);
	public int updateTime(RefundVO vo);
	public int updateHint(RefundVO vo);
	public int success(RefundVO vo);
	public List<Map<String,Object>> selectAll(RefundVO vo);
	public List<Map<String,Object>> selectAllGames(RefundVO vo);
	public int gameUpdate(Map<String, Object> param);
}
