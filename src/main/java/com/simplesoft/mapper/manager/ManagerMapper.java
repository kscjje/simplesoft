package com.simplesoft.mapper.manager;

import java.util.List;
import java.util.Map;

import com.simplesoft.config.MysqlConnMapper;
import com.simplesoft.manager.service.ManagerVO;

@MysqlConnMapper
public interface ManagerMapper {
	public ManagerVO selectManagerDetail(ManagerVO vo);
	public List<Map<String, Object>> selectManagerList(ManagerVO vo);
}
