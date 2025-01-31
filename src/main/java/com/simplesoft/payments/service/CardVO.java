package com.simplesoft.payments.service;

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
public class CardVO {
	private String ownerType;
	private int amount;
	private String issuerCode;
	private String acquirerCode;
	private String number;
	private int installmentPlanMonths;
	private String approveNo;
	
	private Boolean useCardPoint;
	private String cardType;
	private String acquireStatus;
	private Boolean isInterestFree;
	private String interestPayer;
	
}
