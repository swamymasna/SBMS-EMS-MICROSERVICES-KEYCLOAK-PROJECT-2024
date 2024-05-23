package com.kes.ip.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.kes.ip.dto.OrganizationRequestDto;
import com.kes.ip.dto.OrganizationResponseDto;
import com.kes.ip.entity.Organization;
import com.kes.ip.exception.OrganizationServiceBusinessException;
import com.kes.ip.exception.ResourceNotFoundException;
import com.kes.ip.repository.OrganizationRepository;
import com.kes.ip.service.OrganizationService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

	private OrganizationRepository organizationRepository;

	private ModelMapper modelMapper;

	@Override
	public OrganizationResponseDto saveOrganization(OrganizationRequestDto deptReqDto) {

		Organization organization = null;

		OrganizationResponseDto organizationResponseDto = null;

		try {

			organization = modelMapper.map(deptReqDto, Organization.class);

			organization = organizationRepository.save(organization);

			if (organization != null) {
				organizationResponseDto = modelMapper.map(organization, OrganizationResponseDto.class);
			} else {
				organizationResponseDto = null;
			}

		} catch (Exception ex) {
			throw new OrganizationServiceBusinessException(
					String.format("Exception Occured While Saving Organization Into the Database : %s", ex));
		}

		return organizationResponseDto;
	}

	@Override
	public OrganizationResponseDto getOrganizationByCode(String orgCode) {

		Organization organization = organizationRepository.findByOrganizationCodeIgnoreCase(orgCode).orElseThrow(
				() -> new ResourceNotFoundException(String.format("Organization Not Found With Code : %s", orgCode)));

		return modelMapper.map(organization, OrganizationResponseDto.class);
	}

}
