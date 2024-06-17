package com.simplesoft.common.dao;

import lombok.AllArgsConstructor;
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
	private String searchOption;	//검색종류
	private String searchText;		//검색내용
	
	public CommonVO(String startDate, String endDate, int startPage, int endPage, String orderStatus,
			String searchOption, String searchText) {
		super();
		this.startDate = startDate;
		this.endDate = endDate;
		this.startPage = startPage;
		this.endPage = endPage;
		this.orderStatus = orderStatus;
		this.searchOption = searchOption;
		this.searchText = searchText;
	}
}
