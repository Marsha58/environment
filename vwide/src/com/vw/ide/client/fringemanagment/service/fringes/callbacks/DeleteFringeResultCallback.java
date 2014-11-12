package com.vw.ide.client.fringemanagment.service.fringes.callbacks;

import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.vw.ide.client.fringemanagment.FringeManagerPresenter;
import com.vw.ide.client.service.fringes.FringeServiceBroker;
import com.vw.ide.shared.servlet.fringes.RequestDeleteFringeResult;

public class DeleteFringeResultCallback extends FringeServiceBroker.ResultCallback<RequestDeleteFringeResult>{

	private FringeManagerPresenter owner = null;
	
	public DeleteFringeResultCallback(FringeManagerPresenter owner) {
		this.owner = owner;
	}
	
	@Override
	public void handle(RequestDeleteFringeResult result) {
		String messageAlert;
		if (result.getRetCode().intValue() != 0) {
			messageAlert = "Delete finge (id='"+result.getId()+"') operation '" 
					+ "' failed.\r\nResult'" + result.getResult() + "'";
			AlertMessageBox alertMessageBox = new AlertMessageBox(
					"Warning", messageAlert);
			alertMessageBox.show();
		} 
	}

}
