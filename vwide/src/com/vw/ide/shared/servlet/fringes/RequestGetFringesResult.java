package com.vw.ide.shared.servlet.fringes;

import java.util.List;

import com.vw.ide.shared.servlet.fringes.model.Fringe;
import com.vw.ide.shared.servlet.remotebrowser.RequestResult;

@SuppressWarnings("serial")
public class RequestGetFringesResult extends RequestResult{
	private Fringe[] fringes;
	private Integer lastUsedId;
	
	
	public Fringe[] getFringes() {
		return fringes;
	}

	public Integer getLastUsedId() {
		return lastUsedId;
	}
	
	public void setFringes(List<Fringe> fringesList) {
		fringes = new Fringe[fringesList.size()];
		Integer maxId = -1;
		for (int i=0; i<fringesList.size();i++) {
			fringes[i] = fringesList.get(i);
			if (fringesList.get(i).getId() > maxId) {
				maxId = fringesList.get(i).getId();
			}
		}
		lastUsedId = maxId;
	}

	@Override
	public String toString() {
		return "RequestGetFringesResult [fringes=" + fringes + ", lastUsedId=" + lastUsedId + ", result="  + getResult() + 
				", retCode="  + getRetCode() + ", operationType=" + getOperationType()  + "]";
	}
}
