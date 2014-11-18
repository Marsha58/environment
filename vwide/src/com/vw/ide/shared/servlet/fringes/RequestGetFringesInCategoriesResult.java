package com.vw.ide.shared.servlet.fringes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vw.ide.shared.servlet.fringes.model.Category;
import com.vw.ide.shared.servlet.fringes.model.Fringe;
import com.vw.ide.shared.servlet.remotebrowser.RequestResult;

@SuppressWarnings("serial")

public class RequestGetFringesInCategoriesResult  extends RequestResult{

	    private List<Category> categoriesList = new ArrayList<>();
	    private List<Fringe> fringesList = new ArrayList<>();

	    public List<Category> getCategoriesList() {
			return categoriesList;
		}

		public void setCategoriesList(List<Category> categoriesList) {
			this.categoriesList = categoriesList;
		}

		public List<Fringe> getFringesList() {
			return fringesList;
		}

		public void setFringesList(List<Fringe> fringesList) {
			this.fringesList = fringesList;
		}

		

		@Override
		public String toString() {
			return "RequestGetFringesInCategoriesResult [fringesList=" + fringesList + ", categoriesList="  + categoriesList + ", result="  + getResult() + 
					", retCode="  + getRetCode() + ", operationType=" + getOperationType()  + "]";
		}
	}


