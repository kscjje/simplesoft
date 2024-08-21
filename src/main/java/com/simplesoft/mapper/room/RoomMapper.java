package com.simplesoft.mapper.room;

import java.util.List;
import java.util.Map;

import com.simplesoft.config.MysqlConnMapper;
import com.simplesoft.payments.service.PaymentsVO;

@MysqlConnMapper
public interface RoomMapper {
	public int insertRoom(Map<String, Object> paramMap);
	public Map<String, Object> selectRoomDetail(Map<String, Object> paramMap);
	public int updateRoom(Map<String, Object> paramMap);
	public List<Map<String, Object>> list();
	public Map<String, Object> detail(String userName);
}
