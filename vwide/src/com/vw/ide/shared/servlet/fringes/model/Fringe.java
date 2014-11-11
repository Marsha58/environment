package com.vw.ide.shared.servlet.fringes.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Fringe implements Serializable{

	private Integer id;
	private String name;
	private String path;
	private String filename;
	private Boolean loaded;
	private String description;
	private Category category;
	private Integer categoryId;
	
	public Fringe() {
	}
	
	public Fringe(Integer id, String name, String path) {
		this.id = id;
		this.name = name;
		this.path = path;
	}
	
	public Fringe(Integer id, String name, String path, Integer categoryId, String description) {
		this.id = id;
		this.name = name;
		this.path = path;
		this.categoryId = categoryId;
		this.description = description;
	}

	public Fringe(Integer id, String name, String path, String filename, Boolean loaded, Integer categoryId, String description) {
		this.id = id;
		this.name = name;
		this.path = path;
		this.filename = filename;
		this.loaded = loaded;
		this.categoryId = categoryId;
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
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getFilename() {
		return filename;
	}	
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public Boolean getLoaded() {
		return loaded;
	}	
	public void setLoaded(Boolean loaded) {
		this.loaded = loaded;
	}	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	
	
	public String toString() {
		return "Fringe [id=" + id  + ", name="  + name + ", filename="  + filename + 
				", path="  + path + ", loaded="  + loaded + ", description=" + description + 
				", category=" + category  + ", categoryId=" + categoryId  + "]";
	}		
	
}
