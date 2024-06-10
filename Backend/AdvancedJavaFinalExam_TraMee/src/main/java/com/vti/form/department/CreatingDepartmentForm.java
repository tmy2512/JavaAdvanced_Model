package com.vti.form.department;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.vti.validation.account.AccountIDExists;
import com.vti.validation.department.DepartmentNameNotExists;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreatingDepartmentForm {

	@NotBlank(message = "{Department.createDepartment.form.name.NotBlank}")
	@Length(max = 30, message = "{Department.createDepartment.form.name.Length}")
	@DepartmentNameNotExists(message = "{Department.createDepartment.form.name.NotExists}")
	private String name;

	@NotBlank(message = "{Department.createDepartment.form.type.NotBlank}")
	@Pattern(regexp = "DEV|TEST|PM|ScrumMaster", message = "{Department.createDepartment.form.type.pattern}")
	private String type;
	
	private List<@Valid Account> accounts;

	@Data
	@NoArgsConstructor
	public static class Account {
		
		@NotNull(message = "{Account.createAccount.form.id.NotBlank}")
		@AccountIDExists(message = "{Account.createAccount.form.id.Exists}")
		private Integer id;
		
	}
}
