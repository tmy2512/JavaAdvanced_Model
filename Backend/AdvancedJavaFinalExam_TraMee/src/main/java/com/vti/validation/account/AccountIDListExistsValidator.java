package com.vti.validation.account;

import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.vti.service.IAccountService;

public class AccountIDListExistsValidator implements ConstraintValidator<AccountIDListExists, Set<Integer>> {

	@Autowired
	private IAccountService service;

	@SuppressWarnings("deprecation")
	@Override
	public boolean isValid(Set<Integer> ids, ConstraintValidatorContext constraintValidatorContext) {

		if (StringUtils.isEmpty(ids)) {
			return true;
		}
		
		int countRecort = service.getCountIdForDelete(ids);
		
		if(countRecort != ids.size()) {
			return false;
		} 
		
		return true;
	}
}

