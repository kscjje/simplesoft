package com.simplesoft.menuboard.service;

import java.util.List;
import java.util.Map;

public interface MenuBoardService {
	public int insertMenuBoard(Map<String, Object> paraMap);
	public List<Map<String, Object>> selectMenuBoardList(Map<String, Object> paraMap);
	public Map<String, Object> selectMenuBoardDetail(Map<String, Object> paraMap);
}
