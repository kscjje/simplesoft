package com.simplesoft.order.service;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.simplesoft.common.dao.CommonVO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mi:ss")
	private String orderPayDt;			//주문일시
	private String bigo;				//전하실말씀
	private String regUser;				//등록자
	private Date regDt;					//등록일시
	
	private List<OrderProductVO> productList;	//주문상품목록
	
	//Payments 테이블
	private String payMethod;			//결제수단
	private String productName;			//상품명
	private int totalAmount;			//상품금액
	

	@Override
	public String toString() {
		return "OrderVO [orderNo=" + orderNo + ", userNo=" + userNo + ", orderStatus=" + orderStatus + ", userSession="
				+ userSession + ", orderPayType=" + orderPayType + ", totalProductAmt=" + totalProductAmt
				+ ", totalDisAmt=" + totalDisAmt + ", totalDelyAmt=" + totalDelyAmt + ", marketingAgreeFg="
				+ marketingAgreeFg + ", orderName=" + orderName + ", orderPwd=" + orderPwd + ", orderTel=" + orderTel
				+ ", orderPhone=" + orderPhone + ", orderPostNum=" + orderPostNum + ", orderAddr=" + orderAddr
				+ ", orderAddrDetail=" + orderAddrDetail + ", orderEmail=" + orderEmail + ", receiveName=" + receiveName
				+ ", receiveTel=" + receiveTel + ", receivePhone=" + receivePhone + ", receivePostNum=" + receivePostNum
				+ ", receiveAddr=" + receiveAddr + ", receiveAddrDetail=" + receiveAddrDetail + ", commonPwd="
				+ commonPwd + ", orderPayDt=" + orderPayDt + ", bigo=" + bigo + ", regUser=" + regUser + ", regDt="
				+ regDt + ", productList=" + productList + "], " +  super.toString();
	}

}
