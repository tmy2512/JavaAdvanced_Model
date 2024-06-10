package com.vti.form.department;

import java.util.Set;

import javax.validation.constraints.NotEmpty;

import com.vti.validation.department.DepartmentIDListExists;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeleteDepartmentForm {

	@NotEmpty(message = "{Department.createDepartment.form.id.NotBlank}")
	@DepartmentIDListExists
	private Set<Integer> ids;
	
}

