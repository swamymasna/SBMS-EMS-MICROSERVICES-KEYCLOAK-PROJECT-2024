package com.kes.ip.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kes.ip.dto.OrganizationRequestDto;
import com.kes.ip.dto.OrganizationResponseDto;
import com.kes.ip.service.OrganizationService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/organizations")
public class OrganizationController {

	private OrganizationService organizationService;

	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	public OrganizationResponseDto createDepartment(@RequestBody OrganizationRequestDto orgReqDto) {
		return organizationService.saveOrganization(orgReqDto);
	}

	@GetMapping("/{org-code}")
	@ResponseStatus(value = HttpStatus.OK)
	public OrganizationResponseDto fetchOrganizationByCode(@PathVariable("org-code") String orgCode) {
		return organizationService.getOrganizationByCode(orgCode);
	}

}
