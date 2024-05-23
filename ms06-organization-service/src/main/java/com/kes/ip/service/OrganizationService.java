package com.kes.ip.service;

import com.kes.ip.dto.OrganizationRequestDto;
import com.kes.ip.dto.OrganizationResponseDto;

public interface OrganizationService {

	OrganizationResponseDto saveOrganization(OrganizationRequestDto orgReqDto);

	OrganizationResponseDto getOrganizationByCode(String orgCode);
}
