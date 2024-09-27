package com.simplesoft.mapper.menuboard;

import java.util.List;
import java.util.Map;

import com.simplesoft.config.MysqlConnMapper;

@MysqlConnMapper
public interface MenuBoardMapper {
	public int insertMenuBoard(Map<String, Object> paramMap);
	public int updateMenuBoard(Map<String, Object> paramMap);
	public List<Map<String, Object>> selectMenuBoardList(Map<String, Object> paramMap);
	public Map<String, Object> selectMenuBoardDetail(Map<String, Object> paramMap);
	public Map<String, Object> selectMenuBoardDuple(Map<String, Object> paramMap);
}
