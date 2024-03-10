package com.simplesoft.member.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simplesoft.mapper.member.MemberMapper;
import com.simplesoft.member.service.MemberService;
import com.simplesoft.member.service.MemberVO;

@Service
public class MemberServiceImpl implements MemberService{
	
	@Autowired
	MemberMapper memberMapper;
	
	@Override
	public int insertMember(MemberVO memberVO) {
		
		return memberMapper.insertMember(memberVO);
	}
	@Override
	public Map<String, Object> selectMemberDetail(Map<String, Object> paraMap){
		return memberMapper.selectMemberDetail(paraMap);
	}
	@Override
	public List<Map<String, Object>> selectMemberList(Map<String, Object> paraMap){
		return memberMapper.selectMemberList(paraMap);
	}
}
