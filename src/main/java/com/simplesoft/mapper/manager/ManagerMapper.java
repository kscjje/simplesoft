package com.simplesoft.mapper.manager;

import com.simplesoft.config.MysqlConnMapper;
import com.simplesoft.manager.service.ManagerVO;

@MysqlConnMapper
public interface ManagerMapper {
	public ManagerVO selectManagerDetail(ManagerVO vo);
}
