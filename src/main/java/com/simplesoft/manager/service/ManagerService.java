package com.simplesoft.manager.service;

import java.util.List;
import java.util.Map;

public interface ManagerService {
	public ManagerVO selectManagerDetail(ManagerVO vo);
	public List<Map<String, Object>> selectManagerList(ManagerVO vo);
}
