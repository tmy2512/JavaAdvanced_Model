package com.vti.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "`Account`")
@Data
@NoArgsConstructor
public class Account implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "username", length = 50, nullable = false, unique = true, updatable = false)
	private String username;

	@Column(name = "first_name", length = 50, nullable = false, updatable = false)
	private String firstName;

	@Column(name = "last_name", length = 50, nullable = false, updatable = false)
	private String lastName;

	@Column(name = "password", length = 12, nullable = false)
	private String password;

	@Formula(" concat(first_name, ' ', last_name) ")
	private String fullName;

	@Column(name = "`role`")
	@Enumerated(EnumType.STRING)
	private Role role;

	@ManyToOne
	@JoinColumn(name = "department_id")
	private Department department;

	public enum Role {
		ADMIN, MANAGER, EMPLOYEE
	}
}
