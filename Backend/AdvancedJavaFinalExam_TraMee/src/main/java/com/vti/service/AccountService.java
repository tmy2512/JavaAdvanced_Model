package com.vti.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vti.entity.Account;
import com.vti.entity.Account.Role;
import com.vti.entity.Department;
import com.vti.entity.Department.Type;
import com.vti.form.account.AccountFilterForm;
import com.vti.form.account.CreatingAccountForAdminCreateForm;
import com.vti.form.account.CreatingAccountForRegistrationForm;
import com.vti.form.account.UpdatingAccountForm;
import com.vti.form.account.UpdatingAccountPasswordForm;
import com.vti.repository.IAccountRepository;
import com.vti.repository.IDepartmentRepository;
import com.vti.specification.account.AccountSpecification;

@Service
public class AccountService implements IAccountService {

	@Autowired
	private IAccountRepository repository;

	@Autowired
	private IDepartmentRepository departmentRepository;

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public Page<Account> getAllAccounts(
			Pageable pageable, 
			String search, 
			AccountFilterForm filterForm) {
		
		Specification<Account> where = AccountSpecification.buildWhere(search, filterForm);
		return repository.findAll(where, pageable);
	}
	
	public Account getAccountByID(int id) {
		return repository.findById(id).get();
	}

	public Page<Account> getListAccount(Pageable pageable, Integer id) {
		if(id!=null) {
			return repository.getListAccount(pageable, id);
		}else {
			return repository.findAll(pageable);
		}
	}

	@Transactional
	public void createAccount(CreatingAccountForAdminCreateForm form) {
		
		// omit id field
		TypeMap<CreatingAccountForAdminCreateForm, Account> typeMap = modelMapper.getTypeMap(CreatingAccountForAdminCreateForm.class, Account.class);
		if (typeMap == null) { // if not already added
			// skip field
			modelMapper.addMappings(new PropertyMap<CreatingAccountForAdminCreateForm, Account>() {
				@Override
				protected void configure() {
					skip(destination.getId());
				}
			});
		}

		// convert form to entity
		Account account = modelMapper.map(form, Account.class);
		
		account.setPassword(passwordEncoder.encode(account.getPassword()));
		repository.save(account);
		
		// update totalmember cho department
		Integer departmentId = account.getDepartment().getId();		
		Department department = departmentRepository.findById(departmentId).get();
		department.setTotalMember(department.getTotalMember() + 1);
		departmentRepository.save(department);
	}

	@Transactional
	public void registAccount(CreatingAccountForRegistrationForm form) {
		// convert form to entity
		Account account = modelMapper.map(form, Account.class);
		
		// kiểm tra xem có phòng waitting chưa
		Department department = departmentRepository.findByName("waitting");

		if(department != null) {
			// nếu có rồi thì update totalmember cho department
			department.setTotalMember(department.getTotalMember() + 1);
			departmentRepository.save(department);
			account.setDepartment(department);
		}else {
			// nếu chưa có thì tạo phòng
			Department wattingDepartment = new Department("waitting", 1, Type.DEV);
			departmentRepository.save(wattingDepartment);
			account.setDepartment(wattingDepartment);
		}
		
		account.setPassword(passwordEncoder.encode(account.getPassword()));
		account.setRole(Role.EMPLOYEE);
		repository.save(account);
	}

	@Transactional
	public void updateAccount(UpdatingAccountForm form) {

		// convert form to entity
		Account account = modelMapper.map(form, Account.class);
		
		// get Account by id
		Account acc = repository.findById(account.getId()).get();
		
		account.setPassword(passwordEncoder.encode(acc.getPassword()));

		Integer beforDepartmentId = acc.getDepartment().getId();
		Integer afterDepartmentId = form.getDepartmentId();
		
		repository.save(account);
		
		if(beforDepartmentId != beforDepartmentId) {
			// update totalmember cho befor department	
			Department departmentBefor = departmentRepository.findById(beforDepartmentId).get();
			departmentBefor.setTotalMember(departmentBefor.getTotalMember() - 1);
			departmentRepository.save(departmentBefor);
			
			// update totalmember cho befor department	
			Department departmentAfter = departmentRepository.findById(afterDepartmentId).get();
			departmentAfter.setTotalMember(departmentAfter.getTotalMember() + 1);
			departmentRepository.save(departmentAfter);
		}
		
	}

	@Transactional
	public void updatePassword(UpdatingAccountPasswordForm form) {
		
		// get Account by id
		Account account = repository.findById(form.getId()).get();
		
		account.setPassword(passwordEncoder.encode(form.getPassword()));
		
		//account.setLoginDate(form.getLoginDate());
		
		repository.save(account);		
	}

	@Transactional
	public void deleteAccounts(Set<Integer> idList) {
		
		List<Account> accounts = repository.getListAccountByListId(idList);
		List<Department> departmentEntities = new ArrayList<Department>();
		for (Account account : accounts) {
			Department department = account.getDepartment();
			department.setTotalMember(department.getTotalMember() - 1);
			departmentEntities.add(department);
		}
		repository.deleteAllById(idList);
		
		departmentRepository.saveAll(departmentEntities);
		
	}

	@Transactional
	public void deleteAccount(Integer id) {

		Account account = getAccountByID(id);
		Department department = account.getDepartment();
		if(department != null) {
			department.setTotalMember(department.getTotalMember() - 1);
			departmentRepository.save(department);
		}
		
		repository.deleteById(id);
		
		
	}

	public boolean isAccountExistsByUsername(String username) {
		return repository.existsByUsername(username);
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//dangblack -> 
		//mật khẩu : $10$W2neF9.6Agi6kAKVq8q3fec5dHW8KUA.b0VSIGdIZyUravfLpyIFi
		Account account = repository.findByUsername(username);

		if (account == null) {
			throw new UsernameNotFoundException(username);
		}
		
		return new User(
				account.getUsername(), 
				account.getPassword(), 
				AuthorityUtils.createAuthorityList(account.getRole().toString()));
	}
	
	@Override
	public Account getAccountByUsername(String username) {
		return repository.findByUsername(username);
	}
	
	@Override
	public boolean isAccountExistsByID(Integer id) {
		return repository.existsById(id);
	}

	public int getCountIdForDelete(Set<Integer> ids) {
		return repository.getCountIdForDelete(ids);
	}
}


