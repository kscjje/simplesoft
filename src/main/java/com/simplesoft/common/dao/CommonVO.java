package com.simplesoft.common.dao;

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
public class CommonVO {
	private String startDate;		//시작날짜
	private String endDate;			//종료날짜
	private int startPage;			//시작페이지
	private int endPage;			//종료페이지
	private String orderStatus;		//주문상태
	private String searchOption;	//검색종류
	private String searchText;		//검색내용
}
