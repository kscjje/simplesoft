package com.simplesoft.member.service;

import java.util.List;
import java.util.Map;

public interface MemberService {
	
	public int insertMember(MemberVO memberVO);
	public int deleteMember(MemberVO memberVO);
	public int loginSucess(MemberVO memberVO);
	public List<Map<String, Object>> selectMemberList(Map<String, Object> paraMap);
	public MemberVO selectMemberDetail(MemberVO vo);
	public MemberVO selectMemberDetailSns(MemberVO vo);
	public Map<?,?> selectIdCheck(MemberVO vo);
	public Map<?,?> selectFindIdCheck(MemberVO vo);
	public Map<?,?> selectFindPasswordCheck(MemberVO vo);
	public Map<?,?> memberSave(MemberVO vo);
	public int updateOneTimePassword(MemberVO vo);
	public int updatePassword(MemberVO vo);
	public int insertMemberSnsInfo(MemberVO vo);
	public int deleteMemberSnsInfo(MemberVO vo);
	public List<Map<String, Object>> selectMemberSnsList(MemberVO vo);
}
