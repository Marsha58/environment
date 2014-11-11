package com.vw.ide.shared.servlet.fringes;

import com.vw.ide.shared.servlet.fringes.model.Category;
import com.vw.ide.shared.servlet.remotebrowser.RequestResult;

@SuppressWarnings("serial")
public class RequestUpdateCategoryResult extends RequestResult{

	Category category;
	
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}	
	
	@Override
	public String toString() {
		return "RequestUpdateCategoryResult [result="  + getResult() + ", category=" + category + 
				", retCode="  + getRetCode() + ", operation=" + getOperation()  +  "]";
	}		
	
}
