package com.simplesoft.member.service.impl;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simplesoft.mapper.member.MemberMapper;
import com.simplesoft.member.service.MemberService;
import com.simplesoft.member.service.MemberVO;
import com.simplesoft.util.EncryptUtils;

@Service
public class MemberServiceImpl implements MemberService{
	
	@Autowired
	MemberMapper memberMapper;
	
	@Override
	public int insertMember(MemberVO memberVO) {
		return memberMapper.insertMember(memberVO);
	}
	@Override
	public int loginSucess(MemberVO memberVO) {
		return memberMapper.loginSucess(memberVO);
	}
	@Override
	public MemberVO selectMemberDetail(MemberVO vo){
		return memberMapper.selectMemberDetail(vo);
	}
	@Override
	public Map<?,?> selectIdCheck(MemberVO vo){
		return memberMapper.selectIdCheck(vo);
	}
	@Override
	public List<Map<String, Object>> selectMemberList(Map<String, Object> paraMap){
		return memberMapper.selectMemberList(paraMap);
	}
	
	@Override
	public Map<?,?> memberSave(MemberVO vo){
		Map<String, Object> returnData = new HashMap<String, Object>();
		int totCnt = ((Number) selectIdCheck(vo).get("totCnt")).intValue();
		if(totCnt > 0) {
			returnData.put("RESULT", "9990");
			returnData.put("message", "이미 회원가입된 아이디입니다.");
			return returnData;
		}
		try {
			vo.setUserPw(EncryptUtils.SHA512_Encrypt(vo.getUserPw()));
		} catch (NoSuchAlgorithmException e) {
			returnData.put("RESULT", "9992");
			returnData.put("message", "회원가입에 실패하였습니다. \n고객센터에 문의해 주세요.");
			e.printStackTrace();
		}
		if(insertMember(vo) > 0) {
			returnData.put("RESULT", "0000");
			returnData.put("message", "회원가입이 완료되었습니다. \n로그인 페이지로 이동합니다.");
		} else {
			returnData.put("RESULT", "9991");
			returnData.put("message", "회원가입에 실패하였습니다. \n고객센터에 문의해 주세요.");
		}
		return returnData;
	}
	
	@Override
	public String selectFindIdCheck(MemberVO vo){
		return memberMapper.selectFindIdCheck(vo);
	}
}
