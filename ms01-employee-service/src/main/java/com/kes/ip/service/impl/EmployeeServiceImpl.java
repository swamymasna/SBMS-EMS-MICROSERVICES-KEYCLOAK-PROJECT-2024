package com.kes.ip.service.impl;

import static com.kes.ip.utils.AppConstants.DELETE_EMPLOYEE_EXCEPTION;
import static com.kes.ip.utils.AppConstants.DEPT_CODE;
import static com.kes.ip.utils.AppConstants.EMAIL_SUBJECT;
import static com.kes.ip.utils.AppConstants.EMPLOYEE_DELETION_FAILED;
import static com.kes.ip.utils.AppConstants.EMPLOYEE_DELETION_SUCCEEDED;
import static com.kes.ip.utils.AppConstants.EMPLOYEE_NOT_FOUND_EXCEPTION;
import static com.kes.ip.utils.AppConstants.EMPLOYEE_SERVICE_EMAIL_BODY_TEMPLATE;
import static com.kes.ip.utils.AppConstants.EMP_ADDR;
import static com.kes.ip.utils.AppConstants.EMP_ID;
import static com.kes.ip.utils.AppConstants.EMP_NAME;
import static com.kes.ip.utils.AppConstants.EMP_SAL;
import static com.kes.ip.utils.AppConstants.FALLBACK_GET_EMPLOYEE_BY_ID;
import static com.kes.ip.utils.AppConstants.GET_ALL_EMPLOYEES_EXCEPTION;
import static com.kes.ip.utils.AppConstants.GET_EMPLOYEE_BY_ID_EXCEPTION;
import static com.kes.ip.utils.AppConstants.ORG_CODE;
import static com.kes.ip.utils.AppConstants.SAVE_EMPLOYEE_EXCEPTION;
import static com.kes.ip.utils.AppConstants.TO_ADDR;
import static com.kes.ip.utils.AppConstants.UPDATE_EMPLOYEE_EXCEPTION;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.kes.ip.client.DepartmentClient;
import com.kes.ip.client.OrganizationClient;
import com.kes.ip.dto.ApiResponseDto;
import com.kes.ip.dto.DepartmentResponseDto;
import com.kes.ip.dto.EmployeeApiResponseDto;
import com.kes.ip.dto.EmployeeRequestDto;
import com.kes.ip.dto.EmployeeResponseDto;
import com.kes.ip.dto.OrganizationResponseDto;
import com.kes.ip.entity.Employee;
import com.kes.ip.exception.EmployeeServiceBusinessException;
import com.kes.ip.exception.ResourceNotFoundException;
import com.kes.ip.props.AppProperties;
import com.kes.ip.repository.EmployeeRepository;
import com.kes.ip.service.EmployeeService;
import com.kes.ip.utils.EmailUtils;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

	private EmployeeRepository employeeRepository;

	private ModelMapper modelMapper;

	private EmailUtils emailUtils;

	private AppProperties appProperties;

	private DepartmentClient departmentClient;

	private OrganizationClient organizationClient;

	@Override
	public EmployeeResponseDto saveEmployee(EmployeeRequestDto empReqDto) {

		EmployeeResponseDto employeeResponseDto = null;

		Employee employee = null;

		try {

			employee = modelMapper.map(empReqDto, Employee.class);

			employee = employeeRepository.save(employee);

			if (employee != null) {
				employeeResponseDto = modelMapper.map(employee, EmployeeResponseDto.class);

				String subject = appProperties.getMessages().get(EMAIL_SUBJECT);
				String toAddress = TO_ADDR;
				String body = readMailBody(EMPLOYEE_SERVICE_EMAIL_BODY_TEMPLATE, employeeResponseDto);

				emailUtils.sendEmail(subject, toAddress, body);

			} else {
				employeeResponseDto = null;
			}

		} catch (Exception ex) {
			throw new EmployeeServiceBusinessException(
					String.format(appProperties.getMessages().get(SAVE_EMPLOYEE_EXCEPTION), ex));
		}

		return employeeResponseDto;
	}

	private String readMailBody(String fileName, EmployeeResponseDto employeeResponseDto) {

		String mailBody = null;

		try {

			Path path = Paths.get(fileName);

			mailBody = Files.readString(path);

			mailBody = mailBody.replace(EMP_ID, employeeResponseDto.getEmployeeId().toString());
			mailBody = mailBody.replace(EMP_NAME, employeeResponseDto.getEmployeeName());
			mailBody = mailBody.replace(EMP_SAL, employeeResponseDto.getEmployeeSalary().toString());
			mailBody = mailBody.replace(EMP_ADDR, employeeResponseDto.getEmployeeAddress());
			mailBody = mailBody.replace(DEPT_CODE, employeeResponseDto.getDepartmentCode());
			mailBody = mailBody.replace(ORG_CODE, employeeResponseDto.getOrganizationCode());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return mailBody;
	}

	@Override
	public List<EmployeeResponseDto> getAllEmployees() {

		List<EmployeeResponseDto> employeesList = null;

		List<Employee> employees = null;

		try {

			employees = employeeRepository.findAll();

			if (!employees.isEmpty()) {
				employeesList = employees.stream().map(employee -> modelMapper.map(employee, EmployeeResponseDto.class))
						.collect(Collectors.toList());
			} else {
				employeesList = Collections.emptyList();
			}

		} catch (Exception ex) {
			throw new EmployeeServiceBusinessException(
					String.format(appProperties.getMessages().get(GET_ALL_EMPLOYEES_EXCEPTION), ex));
		}

		return employeesList;
	}

	@CircuitBreaker(name = "${spring.application.name}", fallbackMethod = FALLBACK_GET_EMPLOYEE_BY_ID)
	@Override
	public ApiResponseDto getEmployeeById(Integer employeeId) {

		ApiResponseDto apiResponseDto = null;

		EmployeeResponseDto employeeResponseDto = null;

		Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException(
				String.format(appProperties.getMessages().get(EMPLOYEE_NOT_FOUND_EXCEPTION), employeeId)));

		DepartmentResponseDto departmentResponseDto = departmentClient
				.fetchDepartmentByCode(employee.getDepartmentCode());

		OrganizationResponseDto organizationResponseDto = organizationClient
				.fetchOrganizationByCode(employee.getOrganizationCode());

		try {

			employeeResponseDto = modelMapper.map(employee, EmployeeResponseDto.class);

			apiResponseDto = ApiResponseDto.builder().employeeResponseDto(employeeResponseDto)
					.departmentResponseDto(departmentResponseDto).organizationResponseDto(organizationResponseDto)
					.build();

		} catch (Exception ex) {
			throw new EmployeeServiceBusinessException(
					String.format(appProperties.getMessages().get(GET_EMPLOYEE_BY_ID_EXCEPTION), ex));

		}

		return apiResponseDto;
	}

	public ApiResponseDto fallbackGetEmployeeById(Integer employeeId, Exception e) {

		ApiResponseDto apiResponseDto = null;

		Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException(
				String.format(appProperties.getMessages().get(EMPLOYEE_NOT_FOUND_EXCEPTION), employeeId)));

		EmployeeResponseDto employeeResponseDto = modelMapper.map(employee, EmployeeResponseDto.class);

		DepartmentResponseDto departmentResponseDto = DepartmentResponseDto.builder().departmentId(0)
				.departmentName("DEFAULT-DEPT-NAME").departmentCode("DEFAULT-DEPT-001")
				.departmentDescription("DEFAULT-DEPT-DESCRIPTION").build();

		OrganizationResponseDto organizationResponseDto = OrganizationResponseDto.builder().organizationId(0)
				.organizationName("DEFAULT-ORG-NAME").organizationCode("DEFAULT-ORG-001")
				.organizationDescription("DEFAULT-ORG-DESCRIPTION").build();

		apiResponseDto = ApiResponseDto.builder().employeeResponseDto(employeeResponseDto)
				.departmentResponseDto(departmentResponseDto).organizationResponseDto(organizationResponseDto).build();

		return apiResponseDto;
	}

	@Override
	public EmployeeResponseDto updateEmployee(Integer employeeId, EmployeeRequestDto empReqDto) {

		EmployeeResponseDto employeeResponseDto = null;

		Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException(
				String.format(appProperties.getMessages().get(EMPLOYEE_NOT_FOUND_EXCEPTION), employeeId)));

		try {

			employee.setEmployeeName(empReqDto.getEmployeeName());
			employee.setEmployeeSalary(empReqDto.getEmployeeSalary());
			employee.setEmployeeAddress(empReqDto.getEmployeeAddress());

			employee = employeeRepository.save(employee);

			if (employee != null) {
				employeeResponseDto = modelMapper.map(employee, EmployeeResponseDto.class);
			} else {
				employeeResponseDto = null;
			}

		} catch (Exception ex) {
			throw new EmployeeServiceBusinessException(
					String.format(appProperties.getMessages().get(UPDATE_EMPLOYEE_EXCEPTION), ex));

		}

		return employeeResponseDto;
	}

	@Override
	public String deleteEmployee(Integer employeeId) {

		String message = null;

		Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException(
				String.format(appProperties.getMessages().get(EMPLOYEE_NOT_FOUND_EXCEPTION), employeeId)));

		try {

			if (employee.getEmployeeId().equals(employeeId)) {
				employeeRepository.deleteById(employeeId);
				message = appProperties.getMessages().get(EMPLOYEE_DELETION_SUCCEEDED);
			} else {
				message = appProperties.getMessages().get(EMPLOYEE_DELETION_FAILED);
			}

		} catch (Exception ex) {
			throw new EmployeeServiceBusinessException(
					String.format(appProperties.getMessages().get(DELETE_EMPLOYEE_EXCEPTION), ex));

		}

		return message;
	}

	@Override
	public EmployeeApiResponseDto getAllEmployees(Integer pageNo, Integer pageSize, String sortBy) {

		EmployeeApiResponseDto apiResponseDto = null;
		List<EmployeeResponseDto> empsList = null;
		try {

			Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

			Page<Employee> page = employeeRepository.findAll(pageable);

			List<Employee> employees = page.getContent();

			if (employees.isEmpty()) {
				empsList = Collections.emptyList();
			} else {
				empsList = employees.stream().map(employee -> modelMapper.map(employee, EmployeeResponseDto.class))
						.toList();
			}

			apiResponseDto = EmployeeApiResponseDto.builder().empsList(empsList).pageNo(pageNo).pageSize(pageSize)
					.sortBy(sortBy).totalPages(page.getTotalPages()).totalElements(page.getTotalElements())
					.isFirst(page.isFirst()).isLast(page.isLast()).build();

		} catch (Exception ex) {
			throw new EmployeeServiceBusinessException(
					String.format(appProperties.getMessages().get(GET_ALL_EMPLOYEES_EXCEPTION), ex));

		}

		return apiResponseDto;
	}

}
