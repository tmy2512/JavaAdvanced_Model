package com.vti.dto;

import java.util.Date;

import lombok.Data;

@Data
public class LoginInfoDto {

	private int id;

	private String fullName;

	private String role;

	private String departmentName;

	private Date loginDate;
	
	private short ativeFlg;

}
