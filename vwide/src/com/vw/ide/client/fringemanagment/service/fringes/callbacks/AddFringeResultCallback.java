package com.vw.ide.client.fringemanagment.service.fringes.callbacks;

import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.vw.ide.client.fringemanagment.FringeManagerPresenter;
import com.vw.ide.client.service.fringes.FringeServiceBroker;
import com.vw.ide.shared.servlet.fringes.RequestAddFringeResult;

public class AddFringeResultCallback extends FringeServiceBroker.ResultCallback<RequestAddFringeResult>{

	private FringeManagerPresenter owner = null;
	
	public AddFringeResultCallback(FringeManagerPresenter owner) {
		this.owner = owner;
	}
	
	@Override
	public void handle(RequestAddFringeResult result) {
		String messageAlert;
		if (result.getRetCode().intValue() != 0) {
			messageAlert = "Get categories operation '" 
					+ "' failed.\r\nResult'" + result.getResult() + "'";
			AlertMessageBox alertMessageBox = new AlertMessageBox(
					"Warning", messageAlert);
			alertMessageBox.show();
		} 
	}


}

