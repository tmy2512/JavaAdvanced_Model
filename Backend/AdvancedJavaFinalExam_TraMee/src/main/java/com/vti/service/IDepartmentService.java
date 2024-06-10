package com.vti.service;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vti.entity.Department;
import com.vti.form.department.CreatingDepartmentForm;
import com.vti.form.department.DepartmentFilterForm;
import com.vti.form.department.UpdatingDepartmentForm;

public interface IDepartmentService {

	public Page<Department> getAllDepartments(Pageable pageable, String search, DepartmentFilterForm filterForm);
	
	public List<Department> getListDepartments();

	public Department getDepartmentByID(int id);

	public void createDepartment(CreatingDepartmentForm form);

	public void updateDepartment(UpdatingDepartmentForm form);

	public boolean isDepartmentExistsByName(String name);

	public boolean isDepartmentExistsByID(Integer id);
	
	public void deleteDepartments(Set<Integer> idList);

	public int getCountIdForDelete(Set<Integer> ids);
	
	public void deleteDepartment(Integer id);
}
