package com.vti.controller;

import java.security.Principal;
import java.util.Locale;

//import javax.mail.MessagingException;
//import javax.mail.internet.MimeMessage;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vti.dto.LoginInfoDto;
import com.vti.entity.Account;
import com.vti.form.account.CreatingAccountForRegistrationForm;
import com.vti.form.account.UpdatingAccountPasswordForm;
import com.vti.service.IAccountService;

@RestController
@RequestMapping(value = "api/v1/auth")
@CrossOrigin("*")
@Validated
public class AuthController {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private IAccountService service;	

	@Autowired
	private MessageSource messageSource;
	
	@Autowired
    private JavaMailSender mailSender;

	@GetMapping("/login")
	public LoginInfoDto login(Principal principal) {

		String username = principal.getName();
		Account entity = service.getAccountByUsername(username);

		// convert entity --> dto
		LoginInfoDto dto = modelMapper.map(entity, LoginInfoDto.class);

		return dto;
	}

	@PutMapping()
	public void updateAccount(@RequestBody @Valid UpdatingAccountPasswordForm form) {
		service.updatePassword(form);
	}


	@GetMapping(value = "/username/{username}")
	public boolean existsByUsername(@PathVariable(name = "username") String username) {
		return service.isAccountExistsByUsername(username);
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
	
	@GetMapping("/messages/fr")
	public String getMessagesFr(@RequestParam(value = "key") String key){
		return messageSource.getMessage(
				key, 
				null, 
				"Default message", 
				new Locale("fr", "CA"));
	}
	
	@GetMapping("/messages/en")
	public String getMessagesOther(@RequestParam(value = "key") String key){
		return messageSource.getMessage(
				key, 
				null, 
				"Default message", 
				Locale.US);
	}

	@PostMapping()
	public void createAccount(@RequestBody @Valid CreatingAccountForRegistrationForm form) {
		service.registAccount(form);
	}
    
}