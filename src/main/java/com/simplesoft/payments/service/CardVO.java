package com.simplesoft.payments.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
public class CardVO {
	public String ownerType;
	public int amount;
	public String issuerCode;
	public String acquirerCode;
	public String number;
	public int installmentPlanMonths;
	public String approveNo;
	
	public Boolean useCardPoint;
	public String cardType;
	public String acquireStatus;
	public Boolean isInterestFree;
	public String interestPayer;
}
