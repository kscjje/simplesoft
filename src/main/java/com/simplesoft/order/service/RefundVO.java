package com.simplesoft.order.service;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RefundVO {
	private String orderNo;				//주문번호
	private String refundMsg;			//환불내용
	private String status;				//환불상태(0000:신청,1001:반려,2001:환불)
	private String refundAnswer;		//환불반려내용
	private String regUser;
	private String regDt;
	private String uptUser;
	private String uptDt;
}
