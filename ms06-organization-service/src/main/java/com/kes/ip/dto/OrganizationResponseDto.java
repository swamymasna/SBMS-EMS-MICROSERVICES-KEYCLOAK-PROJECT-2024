package com.kes.ip.dto;

import lombok.Data;

@Data
public class OrganizationResponseDto {

	private Integer organizationId;

	private String organizationName;

	private String organizationCode;

	private String organizationDescription;
}
