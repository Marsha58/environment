package com.vw.ide.client.fringemanagment.service.fringes.callbacks;

import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.vw.ide.client.fringemanagment.FringeManagerPresenter;
import com.vw.ide.client.service.fringes.FringeServiceBroker;
import com.vw.ide.shared.servlet.fringes.RequestUpdateCategoryResult;

public class UpdateCategoryResultCallback extends FringeServiceBroker.ResultCallback<RequestUpdateCategoryResult>{

	private FringeManagerPresenter owner = null;
	
	public UpdateCategoryResultCallback(FringeManagerPresenter owner) {
		this.owner = owner;
	}
	
	@Override
	public void handle(RequestUpdateCategoryResult result) {
		String messageAlert;
		if (result.getRetCode().intValue() != 0) {
			messageAlert = "Update category (id='"+result.getCategory().getId()+"') operation '" 
					+ "' failed.\r\nResult'" + result.getResult() + "'";
			AlertMessageBox alertMessageBox = new AlertMessageBox(
					"Warning", messageAlert);
			alertMessageBox.show();
		} 
	}


}