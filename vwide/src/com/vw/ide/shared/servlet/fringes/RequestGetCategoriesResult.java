package com.vw.ide.shared.servlet.fringes;

import java.util.List;

import com.vw.ide.shared.servlet.fringes.model.Category;
import com.vw.ide.shared.servlet.remotebrowser.RequestResult;

@SuppressWarnings("serial")
public class RequestGetCategoriesResult extends RequestResult {
	private Category[] categories;
	private Integer lastUsedId;	
	
	public Category[] getCategories() {
		return categories;
	}
	
	public Integer getLastUsedId() {
		return lastUsedId;
	}	

	public void setCategories(List<Category> categoriesList) {
		categories = new Category[categoriesList.size()];
		Integer maxId = -1;
		for (int i=0; i<categoriesList.size();i++) {
			categories[i] = categoriesList.get(i);
			if (categoriesList.get(i).getId() > maxId) {
				maxId = categoriesList.get(i).getId();
			}
		}
		lastUsedId = maxId;
	}

	@Override
	public String toString() {
		return "RequestGetCategoriesResult [categories=" + categories  + ", lastUsedId=" + lastUsedId + ", result="  + getResult() + 
				", retCode="  + getRetCode() + ", operationType=" + getOperationType()  + "]";
	}
}
