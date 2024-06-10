package com.vti.dto;

import org.springframework.hateoas.RepresentationModel;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountDTO extends RepresentationModel<AccountDTO> {

	private Integer id;

	private String username;

	private String fullName;

	private String firstName;

	private String lastName;

	private String role;

	private Integer departmentId;

	private String departmentName;
}
