package com.simplesoft.common.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CommonVO {
	private String startDate;		//시작날짜
	private String endDate;			//종료날짜
	private int startPage;			//시작페이지
	private int endPage;			//종료페이지
	private String orderStatus;		//주문상태
	private String delivStatus;		//배송상태
	private String searchOption;	//검색종류
	private String searchText;		//검색내용
	private String searchOrder;		//정렬조건
	private String searchOrderBy;	//오름내림차순
	
	public CommonVO(String startDate, String endDate, int startPage, int endPage, String orderStatus,
			String delivStatus, String searchOption, String searchText, String searchOrder, String searchOrderBy) {
		super();
		this.startDate = startDate;
		this.endDate = endDate;
		this.startPage = startPage;
		this.endPage = endPage;
		this.orderStatus = orderStatus;
		this.delivStatus = delivStatus;
		this.searchOption = searchOption;
		this.searchText = searchText;
		this.searchOrder = searchOrder;
		this.searchOrderBy = searchOrderBy;
	}
}
