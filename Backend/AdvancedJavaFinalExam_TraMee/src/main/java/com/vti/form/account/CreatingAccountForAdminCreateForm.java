package com.vti.form.account;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.vti.validation.department.DepartmentIDExists;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreatingAccountForAdminCreateForm extends CreatingAccountForm {
	
	private String password = "123456"; // Set default password

	@NotBlank(message = "{Account.createAccount.form.role.NotBlank}")
	@Pattern(regexp = "ADMIN|EMPLOYEE|MANAGER", message = "{Account.createAccount.form.role.pattern}")
	private String role;

	@NotNull(message = "{Account.createAccount.form.departmentId.NotBlank}")
	@DepartmentIDExists(message = "{Account.createAccount.form.departmentId.NotExists}")
	private Integer departmentId;

}

