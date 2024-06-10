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

import com.vti.dto.AccountDTO;
import com.vti.dto.RolesDTO;
import com.vti.entity.Account;
import com.vti.form.account.AccountFilterForm;
import com.vti.form.account.CreatingAccountForAdminCreateForm;
import com.vti.form.account.DeleteAccountForm;
import com.vti.form.account.UpdatingAccountForm;
import com.vti.service.IAccountService;
import com.vti.validation.account.AccountIDExists;

@RestController
@RequestMapping(value = "api/v1/accounts")
@Validated
public class AccountController {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private IAccountService service;

	@Autowired
	private MessageSource messageSource;
	
	@GetMapping
	public Page<AccountDTO> getAllAccounts(Pageable pageable,
			@RequestParam(value = "search", required = false) String search, AccountFilterForm filterForm) {

		Page<Account> entityPages = service.getAllAccounts(pageable, search, filterForm);

		// convert entities --> dtos
		List<AccountDTO> dtos = modelMapper.map(entityPages.getContent(), new TypeToken<List<AccountDTO>>() {
		}.getType());

		Page<AccountDTO> dtoPages = new PageImpl<>(dtos, pageable, entityPages.getTotalElements());

		return dtoPages;
	}

	@GetMapping(value = "/list")
	public Page<AccountDTO> getListAccount(
			@PageableDefault() Pageable pageable,
			Integer departmentId) {

		Page<Account> entityPages = service.getListAccount(pageable, departmentId);

		// convert entities --> dtos
		List<AccountDTO> dtos = modelMapper.map(entityPages.getContent(), new TypeToken<List<AccountDTO>>() {
		}.getType());

		Page<AccountDTO> dtoPages = new PageImpl<>(dtos, pageable, entityPages.getTotalElements());

		return dtoPages;
	}

	@GetMapping(value = "/{id}")
	public AccountDTO getAccountByID(@PathVariable(name = "id") Integer id) {
		Account entity = service.getAccountByID(id);

		// convert entity to dto
		AccountDTO dto = modelMapper.map(entity, AccountDTO.class);

		dto.add(linkTo(methodOn(AccountController.class).getAccountByID(id)).withSelfRel());

		return dto;
	}

	@GetMapping(value = "username/{username}")
	public boolean existsByUsername(@PathVariable(name = "username") String username) {
		return service.isAccountExistsByUsername(username);
	}

	@GetMapping(value = "/roles")
	public List<RolesDTO> getRoles() {

		List<RolesDTO> listRoles = new ArrayList<>();
		// Lấy ra tất cả các phần tử của Enum.
		Account.Role[] allRoles = Account.Role.values();
		for (Account.Role role : allRoles) {
			RolesDTO dto = new RolesDTO(role.toString(), role.toString());
			listRoles.add(dto);
		}
		return listRoles;
	}

	@PostMapping()
	public void createAccount(@RequestBody @Valid CreatingAccountForAdminCreateForm form) {
		service.createAccount(form);
	}

	@PutMapping()
	public void updateAccount(@RequestBody @Valid UpdatingAccountForm form) {
		service.updateAccount(form);
	}

	@DeleteMapping()
	public void deleteAccounts(@RequestBody @Valid DeleteAccountForm form) {
		service.deleteAccounts(form.getIds());
	}

	@DeleteMapping(value = "/{id}")
	public void deleteAccount(@AccountIDExists @PathVariable(name = "id") int id) {
		service.deleteAccount(id);
	}

	@GetMapping("/messages")
	public String getMessages(@RequestParam(value = "key") String key) {
		return messageSource.getMessage(key, null, "Default message", LocaleContextHolder.getLocale());
	}

	@GetMapping("/messages/vi")
	public String getMessagesVi(@RequestParam(value = "key") String key) {
		return messageSource.getMessage(key, null, "Default message", new Locale("vi", "VN"));
	}

	@GetMapping("/messages/en")
	public String getMessagesOther(@RequestParam(value = "key") String key) {
		return messageSource.getMessage(key, null, "Default message", Locale.US);
	}
}
