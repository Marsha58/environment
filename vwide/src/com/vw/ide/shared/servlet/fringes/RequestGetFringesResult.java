package com.vw.ide.shared.servlet.fringes;

import java.util.concurrent.ConcurrentMap;

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
	
	public void setFringes(ConcurrentMap<Integer, Fringe> concurrentMap) {
		fringes = new Fringe[concurrentMap.size()];
		Integer maxId = -1;
		for (int i=0; i<concurrentMap.size();i++) {
			fringes[i] = concurrentMap.get(i);
			if (fringes[i] != null) {
				if (fringes[i].getId() > maxId) {
					maxId = fringes[i].getId();
				}
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
