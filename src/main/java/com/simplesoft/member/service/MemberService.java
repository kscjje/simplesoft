package com.simplesoft.member.service;

import java.util.List;
import java.util.Map;

public interface MemberService {
	
	public int insertMember(MemberVO memberVO);
	public List<Map<String, Object>> selectMemberList(Map<String, Object> paraMap);
	public Map<String, Object> selectMemberDetail(Map<String, Object> paraMap);
}
