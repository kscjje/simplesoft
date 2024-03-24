package com.simplesoft.order.service;

import java.util.Date;
import java.util.List;

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
public class OrderVO {
	private String orderNo;				//주문번호
	private int userNo;					//회원번호
	private String orderStatus;			//주문상태
	private String userSession;			//비회원세션정보
	private String orderPayType;		//주문결제방법
	private int totalProductAmt;		//총상품금액
	private int totalDisAmt;			//총상품할인금액
	private int totalDelyAmt;			//총배송비
	private String marketingAgreeFg;	//마케팅동의여부
	private String regUser;				//등록자
	private Date regDt;					//등록일시
	
	private List<OrderProductVO> productList;	//주문상품목록
}
