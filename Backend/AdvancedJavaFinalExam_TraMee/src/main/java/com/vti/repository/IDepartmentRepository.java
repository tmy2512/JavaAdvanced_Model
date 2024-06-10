package com.vti.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vti.entity.Department;

public interface IDepartmentRepository extends JpaRepository<Department, Integer>, JpaSpecificationExecutor<Department> {

	boolean existsByName(String name);
	
	Department findByName(String name);
	
	@Query("SELECT COUNT(*) FROM Department WHERE id IN(:ids)")
	int getCountIdForDelete(@Param ("ids") Set<Integer> ids);
	
	
	@Query("FROM Department ORDER BY name")
	List<Department> getListDepartment();
	
}
