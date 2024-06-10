package com.vti.validation.account;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.vti.service.IAccountService;

public class AccountUsernameNotExistsValidator implements ConstraintValidator<AccountUsernameNotExists, String> {

	@Autowired
	private IAccountService service;

	@SuppressWarnings("deprecation")
	@Override
	public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {

		if (StringUtils.isEmpty(username)) {
			return true;
		}

		return !service.isAccountExistsByUsername(username);
	}
}