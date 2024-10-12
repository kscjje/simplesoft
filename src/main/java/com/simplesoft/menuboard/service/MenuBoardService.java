package com.simplesoft.menuboard.service;

import java.util.List;
import java.util.Map;

public interface MenuBoardService {
	public int insertMenuBoard(Map<String, Object> paraMap);
	public int updateMenuBoard(Map<String, Object> paraMap);
	public int deleteMenuBoard(Map<String, Object> paramMap);
	public int updateMenuBoardUseYn(Map<String, Object> paramMap);
	public List<Map<String, Object>> selectMenuBoardList(Map<String, Object> paraMap);
	public Map<String, Object> selectMenuBoardDetail(Map<String, Object> paraMap);
	public Map<String, Object> selectMenuBoardDuple(Map<String, Object> paraMap);
}
