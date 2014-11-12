package com.vw.ide.client.fringemanagment.service.fringes.callbacks;

import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.vw.ide.client.fringemanagment.FringeManagerPresenter;
import com.vw.ide.client.service.fringes.FringeServiceBroker;
import com.vw.ide.shared.servlet.fringes.RequestUpdateFringeResult;

public class UpdateFringeResultCallback  extends FringeServiceBroker.ResultCallback<RequestUpdateFringeResult>{

	private FringeManagerPresenter owner = null;
	
	public UpdateFringeResultCallback(FringeManagerPresenter owner) {
		this.owner = owner;
	}
	
	@Override
	public void handle(RequestUpdateFringeResult result) {
		String messageAlert;
		if (result.getRetCode().intValue() != 0) {
			messageAlert = "Update fringe (id='"+result.getFringe().getId()+"') operation '" 
					+ "' failed.\r\nResult'" + result.getResult() + "'";
			AlertMessageBox alertMessageBox = new AlertMessageBox(
					"Warning", messageAlert);
			alertMessageBox.show();
		} 
	}


}

