package com.vw.ide.shared.servlet.fringes;

import com.vw.ide.shared.servlet.fringes.model.Fringe;
import com.vw.ide.shared.servlet.remotebrowser.RequestResult;

@SuppressWarnings("serial")
public class RequestAddFringeResult extends RequestResult{

	private Fringe fringe;
	
	public Fringe getFringe() {
		return fringe;
	}

	public void setFringe(Fringe fringe) {
		this.fringe = fringe;
	}

	@Override
	public String toString() {
		return "RequestAddFringeResult [result="  + getResult() + ", fringe="  + fringe +
				", retCode="  + getRetCode() + ", operation=" + getOperation()  +  "]";
	}		
	
}

