package com.simplesoft.member.service;

import java.util.List;
import java.util.Map;

public interface MemberService {
	
	public int insertMember(MemberVO memberVO);
	public int loginSucess(MemberVO memberVO);
	public List<Map<String, Object>> selectMemberList(Map<String, Object> paraMap);
	public MemberVO selectMemberDetail(MemberVO vo);
	public Map<?,?> selectIdCheck(MemberVO vo);
	public String selectFindIdCheck(MemberVO vo);
	public Map<?,?> memberSave(MemberVO vo);
}
