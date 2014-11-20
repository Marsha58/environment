package com.vw.ide.shared.servlet.fringes;

import java.util.concurrent.ConcurrentMap;

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

	public void setCategories(ConcurrentMap<Integer, Category> concurrentMap) {
		categories = new Category[concurrentMap.size()];
		Integer maxId = -1;
		for (int i=0; i<concurrentMap.size();i++) {
			categories[i] = concurrentMap.get(i);
			if (categories[i] != null) {
				if (categories[i].getId() > maxId) {
					maxId = categories[i].getId();
				}
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
