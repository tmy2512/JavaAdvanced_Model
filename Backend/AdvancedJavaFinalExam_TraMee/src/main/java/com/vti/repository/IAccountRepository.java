package com.vti.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vti.entity.Account;

public interface IAccountRepository extends JpaRepository<Account, Integer>, JpaSpecificationExecutor<Account> {

	boolean existsByUsername(String username);

	Account findByUsername(String username);
	@Query("FROM Account WHERE id IN(:ids)")
	List<Account> getListAccountByListId(@Param ("ids") Set<Integer> ids);
	
	@Query("SELECT COUNT(*) FROM Account WHERE id IN(:ids)")
	int getCountIdForDelete(@Param ("ids") Set<Integer> ids);
	
	@Query("FROM Account a WHERE department.id <> :id")
	Page<Account> getListAccount(Pageable pageable, @Param ("id") Integer id);
}
