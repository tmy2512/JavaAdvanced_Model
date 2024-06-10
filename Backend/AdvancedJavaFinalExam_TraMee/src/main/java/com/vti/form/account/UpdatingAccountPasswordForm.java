package com.vti.form.account;

import java.sql.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.vti.validation.account.AccountIDExists;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdatingAccountPasswordForm {

	@NotNull(message = "{Account.createAccount.form.id.NotBlank}")
	@AccountIDExists(message = "{Account.createAccount.form.id.Exists}")
	private Integer id;

	@NotBlank(message = "{Account.createAccount.form.password.NotBlank}")
	@Length(min=6, max = 8, message = "{Account.createAccount.form.password.LengthRange}")
	private String password;
	
	private Date loginDate = new Date(System.currentTimeMillis());
	
}

