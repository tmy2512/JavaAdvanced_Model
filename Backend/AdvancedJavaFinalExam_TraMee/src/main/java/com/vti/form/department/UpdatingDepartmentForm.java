package com.vti.form.department;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.vti.validation.department.DepartmentIDExists;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdatingDepartmentForm {

	@NotBlank(message = "{Department.createDepartment.form.id.NotBlank}")
	@DepartmentIDExists(message = "{Department.createDepartment.form.departmentId.NotExists}")
	private int id;

	@NotBlank(message = "{Department.createDepartment.form.type.NotBlank}")
	@Pattern(regexp = "DEV|TEST|PM|ScrumMaster", message = "{Department.createDepartment.form.type.pattern}")
	private String type;

//	private List<@Valid Account> accounts;
//
//	@Data
//	@NoArgsConstructor
//	public static class Account {
//		
//		@NotNull(message = "{Account.createAccount.form.id.NotBlank}")
//		@AccountIDExists
//		private Integer id;
//		
//	}

}
