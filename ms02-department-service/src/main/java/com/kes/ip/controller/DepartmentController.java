package com.kes.ip.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kes.ip.dto.DepartmentRequestDto;
import com.kes.ip.dto.DepartmentResponseDto;
import com.kes.ip.service.DepartmentService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/departments")
public class DepartmentController {

	private DepartmentService departmentService;

	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	public DepartmentResponseDto createDepartment(@RequestBody DepartmentRequestDto deptReqDto) {
		return departmentService.saveDepartment(deptReqDto);
	}

	@GetMapping("/{dept-code}")
	@ResponseStatus(value = HttpStatus.OK)
	public DepartmentResponseDto fetchDepartmentByCode(@PathVariable("dept-code") String deptCode) {
		return departmentService.getDepartmentByCode(deptCode);
	}

}
