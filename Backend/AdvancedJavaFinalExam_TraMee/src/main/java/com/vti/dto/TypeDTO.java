package com.vti.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class TypeDTO {

	private String typeValue;
	
	private String typeName;

	public TypeDTO(String typeValue, String typeName) {
		super();
		this.typeValue = typeValue;
		this.typeName = typeName;
	}
}
