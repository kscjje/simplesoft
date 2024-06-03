package com.simplesoft.order.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

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
public class OrderProductVO {
	
	private int orderProductNo;		//주문상품번호
	private String orderNo;			//주문번호
	private int orderQty;			//주문수량
	private int menuBoardSeq;		//상품번호
	private String menuDay;			//식단일자
	private String menuMsg;			//상품명
	private int payAmt;				//결제금액
	private String deliveryType;	//배송방법
	private int usedPoint;			//사용_적립금
	private Date confirmDt;			//구매확정일시
	private String regUser;			//등록자
	private Date regDt;				//등록일시
}
