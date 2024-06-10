package com.vti.validation.account;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.vti.service.IAccountService;

public class AccountEmailNotExistsValidator implements ConstraintValidator<AccountEmailNotExists, String> {

	@Autowired
	private IAccountService service;

	@SuppressWarnings("deprecation")
	@Override
	public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {

		if (StringUtils.isEmpty(email)) {
			return true;
		}
		return true;

		//return !service.isAccountExistsByEmail(email);
	}
}