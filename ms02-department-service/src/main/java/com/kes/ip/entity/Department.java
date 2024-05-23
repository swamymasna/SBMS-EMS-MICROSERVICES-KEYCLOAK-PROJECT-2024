package com.kes.ip.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "DEPARTMENT_TBL")
public class Department {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer departmentId;

	@Column(name = "DEPT_NAME")
	private String departmentName;

	@Column(name = "DEPT_CODE")
	private String departmentCode;

	@Column(name = "DEPT_DESC")
	private String departmentDescription;
}
