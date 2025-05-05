package com.simplesoft.payments.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CancelsVO {
	public String transactionKey;
	public String cancelReason;
	public String canceledAt;
	public int transferDiscountAmount;
	public int easyPayDiscountAmount;
	public int taxExemptionAmount;
	public int refundableAmount;
	public int taxFreeAmount;
	public String receiptKey;
	public String cancelStatus;
	public String cancelRequestId;
	public int cancelAmount;
}
