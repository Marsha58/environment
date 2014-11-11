package com.vw.ide.shared.servlet.fringes.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Category implements Serializable{
	
	private Integer id;
	private String name;
	private String icon;
	private String description;

	
	public Category() {
	}	
	
	public Category(Integer id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public Category(Integer id, String name, String icon, String description) {
		this.id = id;
		this.name = name;
		this.icon = icon;
		this.description = description;
	}	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public String toString() {
		return "Category [id=" + id  + ", name="  + name + 
				", icon="  + icon + ", description=" + description  + "]";
	}	

}
