package com.kes.ip.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseDto {

	private EmployeeResponseDto employeeResponseDto;

	private DepartmentResponseDto departmentResponseDto;

	private OrganizationResponseDto organizationResponseDto;
}
