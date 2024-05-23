package com.kes.ip.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.kes.ip.dto.DepartmentRequestDto;
import com.kes.ip.dto.DepartmentResponseDto;
import com.kes.ip.entity.Department;
import com.kes.ip.exception.DepartmentServiceBusinessException;
import com.kes.ip.exception.ResourceNotFoundException;
import com.kes.ip.repository.DepartmentRepository;
import com.kes.ip.service.DepartmentService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

	private DepartmentRepository departmentRepository;

	private ModelMapper modelMapper;

	@Override
	public DepartmentResponseDto saveDepartment(DepartmentRequestDto deptReqDto) {

		Department department = null;

		DepartmentResponseDto departmentResponseDto = null;

		try {

			department = modelMapper.map(deptReqDto, Department.class);

			department = departmentRepository.save(department);

			if (department != null) {
				departmentResponseDto = modelMapper.map(department, DepartmentResponseDto.class);
			} else {
				departmentResponseDto = null;
			}

		} catch (Exception ex) {
			throw new DepartmentServiceBusinessException(
					String.format("Exception Occured While Saving Department Into the Database : %s", ex));
		}

		return departmentResponseDto;
	}

	@Override
	public DepartmentResponseDto getDepartmentByCode(String deptCode) {

		Department department = departmentRepository.findByDepartmentCodeIgnoreCase(deptCode).orElseThrow(
				() -> new ResourceNotFoundException(String.format("Department Not Found With Code : %s", deptCode)));

		return modelMapper.map(department, DepartmentResponseDto.class);
	}

}
