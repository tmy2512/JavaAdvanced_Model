package com.vti.service;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.vti.entity.Account;
import com.vti.form.account.AccountFilterForm;
import com.vti.form.account.CreatingAccountForAdminCreateForm;
import com.vti.form.account.CreatingAccountForRegistrationForm;
import com.vti.form.account.UpdatingAccountForm;
import com.vti.form.account.UpdatingAccountPasswordForm;

public interface IAccountService extends UserDetailsService{

	public Page<Account> getAllAccounts(Pageable pageable, String search, AccountFilterForm filterForm);

	public Account getAccountByID(int id);
	
	public void createAccount(CreatingAccountForAdminCreateForm form);
	
	public void registAccount(CreatingAccountForRegistrationForm form);

	public void updateAccount(UpdatingAccountForm form);
	
	public void updatePassword(UpdatingAccountPasswordForm form);

	public void deleteAccounts(Set<Integer> idList);
	
	public void deleteAccount(Integer id);
	
	public boolean isAccountExistsByUsername(String username);
	
	public Account getAccountByUsername(String username);

	public boolean isAccountExistsByID(Integer id);

	public int getCountIdForDelete(Set<Integer> ids);

	public Page<Account> getListAccount(Pageable pageable, Integer id);
}
