package com.shopme.admin.setting.state;

import com.shopme.common.entity.State;

public class StateDTO {
	private String name;
	
	private Integer id;
	
	public StateDTO(State state) {
		this.name =state.getName();
		this.id = state.getId();
	}

	public StateDTO() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String toString() {
		return this.name;
	}
}
