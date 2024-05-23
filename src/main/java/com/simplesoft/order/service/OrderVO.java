package com.simplesoft.order.service;

import java.util.Date;
import java.util.List;

import com.simplesoft.common.dao.CommonVO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderVO extends CommonVO{
	private String orderNo;				//주문번호
	private int userNo;					//회원번호
	private String orderStatus;			//주문상태
	private String userSession;			//비회원세션정보
	private String orderPayType;		//주문결제방법
	private int totalProductAmt;		//총상품금액
	private int totalDisAmt;			//총상품할인금액
	private int totalDelyAmt;			//총배송비
	private String marketingAgreeFg;	//마케팅동의여부
	
	private String orderName;			//주문자 이름
	private String orderPwd;			//주문서 조회 시 필요한 비회원 비밀번호
	private String orderTel;			//주문자 전화번호
	private String orderPhone;			//주문자 핸드폰
	private String orderPostNum;		//주문자 우편번호
	private String orderAddr;			//주문자 기본주소
	private String orderAddrDetail;		//주문자 상세주소
	private String orderEmail;			//주문자 이메일
	private String receiveName;			//받는사람 이름
	private String receiveTel;			//받는사람 전화번호
	private String receivePhone;		//받는사람 핸드폰
	private String receivePostNum;		//받는사람 우편번호
	private String receiveAddr;			//받는사람 기본주소
	private String receiveAddrDetail;	//받는사람 상세주소
	private String commonPwd;			//공동현관 비밀번호
	private String bigo;				//전하실말씀
	private String regUser;				//등록자
	private Date regDt;					//등록일시
	
	private List<OrderProductVO> productList;	//주문상품목록
}
