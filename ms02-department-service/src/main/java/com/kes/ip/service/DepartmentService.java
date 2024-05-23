package com.kes.ip.service;

import com.kes.ip.dto.DepartmentRequestDto;
import com.kes.ip.dto.DepartmentResponseDto;

public interface DepartmentService {

	DepartmentResponseDto saveDepartment(DepartmentRequestDto deptReqDto);

	DepartmentResponseDto getDepartmentByCode(String deptCode);
}
