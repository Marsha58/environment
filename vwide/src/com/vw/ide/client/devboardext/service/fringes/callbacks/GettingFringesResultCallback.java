package com.vw.ide.client.devboardext.service.fringes.callbacks;

import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.vw.ide.client.dialog.fringemanagment.FringeManagerPresenter;
import com.vw.ide.client.service.fringes.FringeServiceBroker;
import com.vw.ide.shared.servlet.fringes.RequestGetFringesResult;

public class GettingFringesResultCallback extends FringeServiceBroker.ResultCallback<RequestGetFringesResult>{

	private FringeManagerPresenter owner = null;
	
	public GettingFringesResultCallback(FringeManagerPresenter owner) {
		this.owner = owner;
	}

	@Override
	public void handle(RequestGetFringesResult result) {
		String messageAlert;
		if (result.getRetCode().intValue() != 0) {
			messageAlert = "Get Fringes operation '" 
					+ "' failed.\r\nResult'" + result.getResult() + "'";
			AlertMessageBox alertMessageBox = new AlertMessageBox(
					"Warning", messageAlert);
			alertMessageBox.show();
		} else {
			switch (result.getRetCode()) {
				case 0:
					owner.updateFringeList(result.getFringes());
					owner.updateFringeLastUsedId(result.getLastUsedId());
					break;
				case -1:
					messageAlert = "something wrong";
					AlertMessageBox alertMessageBox = new AlertMessageBox(
							"Warning", messageAlert);
					alertMessageBox.show();	
					break;
				default:
					break;
			}
		}
	}	
	
}
