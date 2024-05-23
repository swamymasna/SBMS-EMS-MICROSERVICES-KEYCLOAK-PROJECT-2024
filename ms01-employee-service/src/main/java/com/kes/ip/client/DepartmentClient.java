package com.kes.ip.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.kes.ip.dto.DepartmentResponseDto;

@FeignClient(name = "DEPARTMENT-SERVICE")
public interface DepartmentClient {

	@GetMapping("/api/departments/{dept-code}")
	public DepartmentResponseDto fetchDepartmentByCode(@PathVariable("dept-code") String deptCode);
}
