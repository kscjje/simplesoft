package com.simplesoft.order.service;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

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
public class DeliveryVO extends CommonVO{
	
	private int deliverySeq;			//시퀀스
	private int orderProductNo;			//주문상품번호
	private String orderNo;				//주문번호
	private String menuDay;				//식단일자
	private String deliveryStatus;		//배송상태
	
	private String deliveryStatusNm;	//배송상태명
	private String managerNo;			//배송담당자 번호
	private String managerNm;			//배송담당자
	private String deliveryDt;			//배송일시
	private String regUser;				//등록자
	private String regDt;				//등록일시
	private String uptUser;				//수정자
	private Date uptDt;					//수정일시
	private String receiveName;			//받는사람 이름
	private String receiveTel;			//받는사람 전화번호
	private String receivePhone;		//받는사람 핸드폰번호
	private String receivePostNum;		//받는사람 우편번호
	private String receiveAddr;			//받는사람 주소
	private String receiveAddrDetail;	//받는사람 상세주소
	
	private List<DeliveryVO> deliveryList;	//배송목록
	private int deliveryCount;				//배송목록 카운트
	private int orderQty;				//수량
}
