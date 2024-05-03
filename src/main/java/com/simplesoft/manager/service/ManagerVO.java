package com.simplesoft.manager.service;

import java.util.Date;

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
public class ManagerVO {
	private int managerNo;
	private String managerId;
	private String managerPw;
	private String managerStatus;
	private Date managerStatusDt;
	private int loginFailCnt;
	private Date loginLastDt;
	private Date pwChangeDt;
	private Date pwTempDt;
	private String regUser;
	private Date regDt;
}
