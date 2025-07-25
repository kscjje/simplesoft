package com.simplesoft.payments.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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
/**
 * 결제 정보 VO
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentsVO {
	private String paymentType;
	private String orderId;
	private String paymentKey;
	private int amount;
	
	private String country;
	private String version;
	private String type;
	private String orderName;
    @JsonProperty("mId")
	private String mId;
	private String currency;
	private String method;
	private int totalAmount;
	private int balanceAmount; 
	private String status;
	private String requestedAt;
	private String approvedAt;
	private Boolean useEscrow; 
	private String lastTransactionKey;
	private int suppliedAmount;
	private int vat; 
	private Boolean cultureExpense; 
	private int taxFreeAmount;
	
	private int taxExemptionAmount;
	private Object cancels;
	private Boolean isPartialCancelable;
	private Object card;
	private Object virtualAccount;
	private String secret;
	private Object mobilePhone;
	private Object giftCertificate;
	private Object transfer;
	private Object receipt;
	private Object checkout;
	private Object easyPay;
	private Object failure;
	private Object cashReceipt;
	private Object cashReceipts;
	private Object discount;
	private String receiptUrl;
	
}
