package com.vw.ide.client.devboardext.service.fringes.callbacks;

import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.vw.ide.client.dialog.fringemanagment.FringeManagerPresenter;
import com.vw.ide.client.service.fringes.FringeServiceBroker;
import com.vw.ide.shared.servlet.fringes.RequestAddCategoryResult;

public class AddCategoryResultCallback  extends FringeServiceBroker.ResultCallback<RequestAddCategoryResult>{

	private FringeManagerPresenter owner = null;
	
	public AddCategoryResultCallback(FringeManagerPresenter owner) {
		this.owner = owner;
	}
	
	@Override
	public void handle(RequestAddCategoryResult result) {
		String messageAlert;
		if (result.getRetCode().intValue() != 0) {
			messageAlert = "Add  category (id='"+result.getCategory().getId()+"') operation '" 
					+ "' failed.\r\nResult'" + result.getResult() + "'";
			AlertMessageBox alertMessageBox = new AlertMessageBox(
					"Warning", messageAlert);
			alertMessageBox.show();
		} 
	}


}
