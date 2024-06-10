package com.vti.specification.account;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.vti.entity.Account;
import com.vti.form.account.AccountFilterForm;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

public class AccountSpecification {

	@SuppressWarnings("deprecation")
	public static Specification<Account> buildWhere(String search, AccountFilterForm filterForm) {
		
		// Khởi tạo where
		Specification<Account> where = null;

		// where default (1 = 1)
		CustomSpecification init = new CustomSpecification("init", "init");
		where = Specification.where(init);
		
		// Search theo theo username & firstname & lastname
		if (!StringUtils.isEmpty(search)) {
			search = search.trim();
			CustomSpecification username = new CustomSpecification("username", search);
			CustomSpecification firstName = new CustomSpecification("firstName", search);
			CustomSpecification lastName = new CustomSpecification("lastName", search);
			where = where.and((username).or(firstName).or(lastName));
		}
		
		// Filter
		if(filterForm == null) {
			return where;
		}
		// Filter theo role hoặc department
		// role
		if (filterForm.getRole() != null) {
			CustomSpecification role = new CustomSpecification("role", filterForm.getRole());
			where = where.and(role);
		}
		
		// department
		if (filterForm.getDepartmentId() != null) {
			CustomSpecification departmentId = new CustomSpecification("department", filterForm.getDepartmentId());
			where = where.and(departmentId);
		}

		return where;
	}
}

@SuppressWarnings("serial")
@RequiredArgsConstructor
class CustomSpecification implements Specification<Account> {

	@NonNull
	private String field;
	@NonNull
	private Object value;

	@Override
	public Predicate toPredicate(
			Root<Account> root, 
			CriteriaQuery<?> query, 
			CriteriaBuilder criteriaBuilder) {
		
		if (field.equalsIgnoreCase("init")) {
			return criteriaBuilder.equal(criteriaBuilder.literal(1), 1);
		}

		if (field.equalsIgnoreCase("username")) {
			return criteriaBuilder.like(root.get("username"), "%" + value.toString() + "%");
		}

		if (field.equalsIgnoreCase("firstName")) {
			return criteriaBuilder.like(root.get("firstName"), "%" + value.toString() + "%");
		}

		if (field.equalsIgnoreCase("lastName")) {
			return criteriaBuilder.like(root.get("lastName"), "%" + value.toString() + "%");
		}
		
		if (field.equalsIgnoreCase("role")) {
			return criteriaBuilder.equal(root.get("role"), value);
		}
		
		if (field.equalsIgnoreCase("department")) {
			return criteriaBuilder.equal(root.get("department").get("id"), value);
		}

		return null;
	}
}

