package com.simplesoft.mapper.bizBatch;

import java.util.Map;

import com.simplesoft.common.service.BizBatchVO;
import com.simplesoft.config.MysqlConnMapper;

@MysqlConnMapper
public interface BizBatchMapper {
	public int updateBizBatchReport(BizBatchVO bizBatchVO);
	public Map<String, Object> selectBizMessage(Map<String, Object> paramMap);
	public int insertBizBatch(BizBatchVO params);
}
