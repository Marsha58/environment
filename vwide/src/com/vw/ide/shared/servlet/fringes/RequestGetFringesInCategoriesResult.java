package com.vw.ide.shared.servlet.fringes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

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

		public void setCategoriesList(ConcurrentMap<Integer, Category> concurrentMap) {
			for (int i=0; i<concurrentMap.size();i++) {
				categoriesList.add(concurrentMap.get(i));
			}			
		}

		public List<Fringe> getFringesList() {
			return fringesList;
		}

		public void setFringesList(ConcurrentMap<Integer, Fringe> concurrentMap) {
			for (int i=0; i<concurrentMap.size();i++) {
				fringesList.add(concurrentMap.get(i));
			}			
		}

		

		@Override
		public String toString() {
			return "RequestGetFringesInCategoriesResult [fringesList=" + fringesList + ", categoriesList="  + categoriesList + ", result="  + getResult() + 
					", retCode="  + getRetCode() + ", operationType=" + getOperationType()  + "]";
		}
	}


