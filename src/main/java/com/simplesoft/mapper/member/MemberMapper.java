package com.simplesoft.mapper.member;

import java.util.List;
import java.util.Map;

import com.simplesoft.config.MysqlConnMapper;
import com.simplesoft.member.service.MemberVO;

@MysqlConnMapper
public interface MemberMapper {
	public int insertMember(MemberVO vo);
	public int deleteMember(MemberVO vo);
	public int loginSucess(MemberVO vo);
	
	public List<Map<String, Object>> selectMemberList(Map<String, Object> paramMap);
	public MemberVO selectMemberDetail(MemberVO vo);
	public MemberVO selectMemberDetailSns(MemberVO vo);
	public Map<?,?> selectIdCheck(MemberVO vo);
	public Map<?,?> selectFindIdCheck(MemberVO vo);
	public Map<?,?> selectFindPasswordCheck(MemberVO vo);
	public int updateOneTimePassword(MemberVO vo);
	public int insertMemberSnsInfo(MemberVO vo);
	public int deleteMemberSnsInfo(MemberVO vo);
	public int updatePassword(MemberVO vo);
	public List<Map<String, Object>> selectMemberSnsList(MemberVO vo);
}
