package com.vti.specification.department;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.vti.entity.Department;
import com.vti.form.department.DepartmentFilterForm;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

public class DepartmentSpecification {

	@SuppressWarnings("deprecation")
	public static Specification<Department> buildWhere(String search, DepartmentFilterForm filterForm) {
		
		// Khởi tạo where
		Specification<Department> where = null;

		// where default (1 = 1)
		CustomSpecification init = new CustomSpecification("init", "init");
		where = Specification.where(init);
		
		// Search: sẽ search theo name
		if (!StringUtils.isEmpty(search)) {
			search = search.trim();
			CustomSpecification departmentName = new CustomSpecification("departmentName", search);
			where = where.and(departmentName);
		}	
				
		// Filter
		if(filterForm == null) {
			return where;
		}
				
		// Filter: filter theo min created date, max created date, hoặc type
		// min created date
		if (filterForm.getMinCreatedDate() != null) {
			CustomSpecification minCreatedDate = new CustomSpecification("minCreatedDate", filterForm.getMinCreatedDate());
			where = where.and(minCreatedDate);
		}		
				
		// max created date
		if (filterForm.getMaxCreatedDate() != null) {
			CustomSpecification maxCreatedDate = new CustomSpecification("maxCreatedDate", filterForm.getMaxCreatedDate());
			where = where.and(maxCreatedDate);
		}
		
		// type
		if (filterForm != null && filterForm.getType() != null) {
			CustomSpecification type = new CustomSpecification("type", filterForm.getType());
			where = where.and(type);
		}
		
		return where;
	}
}

@SuppressWarnings("serial")
@RequiredArgsConstructor
class CustomSpecification implements Specification<Department> {

	@NonNull
	private String field;
	@NonNull
	private Object value;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Predicate toPredicate(
			Root<Department> root, 
			CriteriaQuery<?> query, 
			CriteriaBuilder criteriaBuilder) {
		
		if (field.equalsIgnoreCase("init")) {
			return criteriaBuilder.equal(criteriaBuilder.literal(1), 1);
		}
		
		if (field.equalsIgnoreCase("departmentName")) {
			return criteriaBuilder.like(root.get("name"), "%" + value.toString() + "%");
		}
		
		if (field.equalsIgnoreCase("minCreatedDate")) {
			return criteriaBuilder.greaterThanOrEqualTo(
					root.get("createdDate").as(java.sql.Date.class),
					(Date) value);
		}
	
		if (field.equalsIgnoreCase("maxCreatedDate")) {
			return criteriaBuilder.lessThanOrEqualTo(
					root.get("createdDate").as(java.sql.Date.class),
					(Date) value);
		}
		
		if (field.equalsIgnoreCase("type")) {
			return criteriaBuilder.equal(root.get("type"), value);
		}
		
		return null;
	}
}

