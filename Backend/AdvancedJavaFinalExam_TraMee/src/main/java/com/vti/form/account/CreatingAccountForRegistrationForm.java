package com.vti.form.account;

import java.sql.Date;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreatingAccountForRegistrationForm extends CreatingAccountForm {

	@NotBlank(message = "{Account.createAccount.form.password.NotBlank}")
	@Length(min=6, max = 8, message = "{Account.createAccount.form.password.LengthRange}")
	private String password;

	private Date loginDate = new Date(System.currentTimeMillis());

}

