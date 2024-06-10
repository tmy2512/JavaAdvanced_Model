package com.vti.validation.department;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.vti.service.IDepartmentService;

public class DepartmentIDExistsValidator implements ConstraintValidator<DepartmentIDExists, Integer> {

	@Autowired
	private IDepartmentService service;

	@SuppressWarnings("deprecation")
	@Override
	public boolean isValid(Integer id, ConstraintValidatorContext constraintValidatorContext) {

		if (StringUtils.isEmpty(id)) {
			return true;
		}

		return service.isDepartmentExistsByID(id);
	}
}

