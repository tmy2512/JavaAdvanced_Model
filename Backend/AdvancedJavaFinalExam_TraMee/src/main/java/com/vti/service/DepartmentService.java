package com.vti.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.vti.entity.Account;
import com.vti.entity.Department;
import com.vti.form.department.CreatingDepartmentForm;
import com.vti.form.department.DepartmentFilterForm;
import com.vti.form.department.UpdatingDepartmentForm;
import com.vti.repository.IAccountRepository;
import com.vti.repository.IDepartmentRepository;
import com.vti.specification.department.DepartmentSpecification;

@Service
public class DepartmentService implements IDepartmentService {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private IDepartmentRepository repository;
	
	@Autowired
	private IAccountRepository accountRepository;
	
	public Page<Department> getAllDepartments(Pageable pageable, String search, DepartmentFilterForm filterForm) {

		Specification<Department> where = DepartmentSpecification.buildWhere(search, filterForm);
		return repository.findAll(where, pageable);
	}	

	public List<Department> getListDepartments() {
		return repository.getListDepartment();
	}

	public Department getDepartmentByID(int id) {
		return repository.findById(id).get();
	}

	@Transactional
	public void createDepartment(CreatingDepartmentForm form) {
		
		// convert form to entity
		Department departmentEntity = modelMapper.map(form, Department.class);
		
		// get total Member
		int totalMember = 0;
		if(departmentEntity.getAccounts() != null) {
			totalMember = departmentEntity.getAccounts().size();
		}
		departmentEntity.setTotalMember(totalMember);

		// create department
		Department department = repository.save(departmentEntity);
		
		
		// create accounts
		List<Account> accountEntities = new ArrayList<Account>();
		List<CreatingDepartmentForm.Account> accounts = form.getAccounts();
		for (CreatingDepartmentForm.Account account : accounts) {
			int id = account.getId();
			Account acc = accountRepository.findById(id).get();
			acc.setDepartment(department);
			accountEntities.add(acc);
		}
		accountRepository.saveAll(accountEntities);

	}

	public void updateDepartment(UpdatingDepartmentForm form) {

		// convert form to entity
		Department department = modelMapper.map(form, Department.class);

		Department dep = repository.findById(form.getId()).get();
		department.setTotalMember(dep.getTotalMember());
		repository.save(department);
		
	}
	
	public void deleteDepartments(Set<Integer> idList) {
		
		repository.deleteAllById(idList);
	}

	public boolean isDepartmentExistsByName(String name) {
		return repository.existsByName(name);
	}

	public boolean isDepartmentExistsByID(Integer id) {
		return repository.existsById(id);
	}

	public int getCountIdForDelete(Set<Integer> ids) {
		return repository.getCountIdForDelete(ids);
	}
	
	public void deleteDepartment(Integer id) {
		
		repository.deleteById(id);
	}

}
