package com.kes.ip.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeApiResponseDto {

	private List<EmployeeResponseDto> empsList;
	private Integer pageNo;
	private Integer pageSize;
	private String sortBy;
	private Integer totalPages;
	private Long totalElements;
	private Boolean isFirst;
	private Boolean isLast;
}
