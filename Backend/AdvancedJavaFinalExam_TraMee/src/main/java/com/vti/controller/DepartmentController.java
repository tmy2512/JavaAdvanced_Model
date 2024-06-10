package com.vti.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vti.dto.DepartmentDTO;
import com.vti.dto.TypeDTO;
import com.vti.entity.Department;
import com.vti.form.department.CreatingDepartmentForm;
import com.vti.form.department.DeleteDepartmentForm;
import com.vti.form.department.DepartmentFilterForm;
import com.vti.form.department.UpdatingDepartmentForm;
import com.vti.service.IDepartmentService;
import com.vti.validation.department.DepartmentIDExists;

@RestController
@RequestMapping(value = "api/v1/departments")
@Validated
public class DepartmentController {

	@Autowired
	private IDepartmentService service;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private MessageSource messageSource;

	@GetMapping()
	public Page<DepartmentDTO> getAllDepartments(
			@PageableDefault(sort = { "totalMember" }, direction = Sort.Direction.DESC) Pageable pageable,
			@RequestParam(name = "search", required = false) String search, DepartmentFilterForm filterForm) {
		Page<Department> entityPages = service.getAllDepartments(pageable, search, filterForm);

		// convert entities --> dtos
		List<DepartmentDTO> dtos = modelMapper.map(entityPages.getContent(), new TypeToken<List<DepartmentDTO>>() {
		}.getType());

		// add HATEOAS
		for (DepartmentDTO dto : dtos) {
			for (DepartmentDTO.AccountDTO accountDTO : dto.getAccounts()) {
				accountDTO.add(
						linkTo(methodOn(AccountController.class).getAccountByID(accountDTO.getId())).withSelfRel());
			}
			dto.add(linkTo(methodOn(DepartmentController.class).getDepartmentByID(dto.getId())).withSelfRel());
		}

		Page<DepartmentDTO> dtoPages = new PageImpl<>(dtos, pageable, entityPages.getTotalElements());

		return dtoPages;
	}
	
	@GetMapping(value = "/lists")
	public List<DepartmentDTO> getListDepartments() {
		List<Department> lst = service.getListDepartments();

		// convert entities --> dtos
		List<DepartmentDTO> dtos = modelMapper.map(lst, new TypeToken<List<DepartmentDTO>>() {
		}.getType());

		return dtos;
	}

	@GetMapping(value = "/{id}")
	public DepartmentDTO getDepartmentByID(@PathVariable(name = "id") @DepartmentIDExists int id) {
		Department entity = service.getDepartmentByID(id);

		// convert entity to dto
		DepartmentDTO dto = modelMapper.map(entity, DepartmentDTO.class);

		dto.add(linkTo(methodOn(DepartmentController.class).getDepartmentByID(id)).withSelfRel());

		return dto;
	}

	@GetMapping(value = "/types")
	public List<TypeDTO> getTypes() {

		List<TypeDTO> listTypes = new ArrayList<>();
		// Lấy ra tất cả các phần tử của Enum.
		Department.Type[] allTypes = Department.Type.values();
		for (Department.Type type : allTypes) {
			TypeDTO dto = new TypeDTO(type.toString(), type.getValue().toString());
			listTypes.add(dto);
		}
		return listTypes;
	}

	@GetMapping(value = "departmentName/{departmentName}")
	public boolean existsByDepartmentName(@PathVariable(name = "departmentName") String departmentName) {
		
		return service.isDepartmentExistsByName(departmentName);
		
	}

	@PostMapping()
	public void createDepartment(@RequestBody @Valid CreatingDepartmentForm form) {
		service.createDepartment(form);
	}

	@PutMapping(value = "/{id}")
	public void updateDepartment(@DepartmentIDExists @PathVariable(name = "id") int id,
			@RequestBody UpdatingDepartmentForm form) {
		form.setId(id);
		service.updateDepartment(form);
	}

	@DeleteMapping()
	public void deleteDepartments(@RequestBody @Valid DeleteDepartmentForm form) {
		service.deleteDepartments(form.getIds());
	}

	@DeleteMapping(value = "/{id}")
	public void deleteDepartment(@DepartmentIDExists @PathVariable(name = "id") int id) {
		service.deleteDepartment(id);
	}

	@GetMapping("/messages")
	public String getMessages(@RequestParam(value = "key") String key){
		return messageSource.getMessage(
				key, 
				null, 
				"Default message", 
				LocaleContextHolder.getLocale());
	}
	
	@GetMapping("/messages/vi")
	public String getMessagesVi(@RequestParam(value = "key") String key){
		return messageSource.getMessage(
				key, 
				null, 
				"Default message", 
				new Locale("vi", "VN"));
	}
	
	@GetMapping("/messages/en")
	public String getMessagesOther(@RequestParam(value = "key") String key){
		return messageSource.getMessage(
				key, 
				null, 
				"Default message", 
				Locale.US);
	}
}
