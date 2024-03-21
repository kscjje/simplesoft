package com.simplesoft.order.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderVO {
	private String orderNo;				//주문번호
	private int userNo;					//회원번호
	private String orderStatus;			//주문상태
	private String userSession;			//비회원세션정보
	private String orderPayType;		//주문결제방법
	private int totalProductAmt;		//총상품금액
	private int totalDisAmt;			//총상품할인금액
	private String marketingAgreeFg;	//마케팅동의여부
}
