package com.study.security_joohong.web.controller.api;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.study.security_joohong.handler.aop.annotation.Log;
import com.study.security_joohong.handler.aop.annotation.Timer;
import com.study.security_joohong.handler.aop.annotation.ValidCheck;
import com.study.security_joohong.handler.aop.annotation.ValidCheck2;
import com.study.security_joohong.handler.exception.CustomValidationApiException;
import com.study.security_joohong.service.auth.AuthService;
import com.study.security_joohong.service.auth.PrincipalDetails;
import com.study.security_joohong.service.auth.PrincipalDetailsService;
import com.study.security_joohong.web.dto.CMRespDto;
import com.study.security_joohong.web.dto.auth.SignupReqDto;
import com.study.security_joohong.web.dto.auth.UsernameCheckReqDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
	
	private final PrincipalDetailsService principalDetailsService;
	private final AuthService authService;
	
	@Log
	@Timer
	@ValidCheck
	@GetMapping("/signup/validation/username")
	public ResponseEntity<?> checkUsername(@Valid UsernameCheckReqDto usernameCheckReqDto, BindingResult bindingResult) {
		
		boolean status = false;
		
		try {
			status = authService.checkUsername(usernameCheckReqDto);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(new CMRespDto<>(-1, "?????? ??????", status));
		}
		
		return ResponseEntity.ok(new CMRespDto<>(1, "???????????? ????????????", status));
	}
	
	@ValidCheck
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody @Valid SignupReqDto signupReqDto, BindingResult bindingResult) {
		boolean status = false;
		
		
		try {
			status = principalDetailsService.addUser(signupReqDto);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(new CMRespDto<>(-1, "???????????? ??????", status));
		}
		
		return ResponseEntity.ok(new CMRespDto<>(1, "???????????? ??????", status));
	}
	
	@GetMapping("/principal")
	public ResponseEntity<?> getPrincipal(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		if(principalDetails == null) {
			return ResponseEntity.badRequest().body(new CMRespDto<>(-1, "principal is null", null));
		}
		return ResponseEntity.ok(new CMRespDto<>(1, "success", principalDetails.getUser()));
	}
}
