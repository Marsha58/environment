package com.vw.ide.client.fringemanagment.service.fringes.callbacks;

import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.vw.ide.client.fringemanagment.FringeManagerPresenter;
import com.vw.ide.client.service.fringes.FringeServiceBroker;
import com.vw.ide.shared.servlet.fringes.RequestGetCategoriesResult;

public class GettingFringeCategoriesResultCallback extends FringeServiceBroker.ResultCallback<RequestGetCategoriesResult>{

	private FringeManagerPresenter owner = null;
	
	public GettingFringeCategoriesResultCallback(FringeManagerPresenter owner) {
		this.owner = owner;
	}
	
	@Override
	public void handle(RequestGetCategoriesResult result) {
		String messageAlert;
		if (result.getRetCode().intValue() != 0) {
			messageAlert = "Get categories operation '" 
					+ "' failed.\r\nResult'" + result.getResult() + "'";
			AlertMessageBox alertMessageBox = new AlertMessageBox(
					"Warning", messageAlert);
			alertMessageBox.show();
		} else {
			switch (result.getRetCode()) {
				case 0:
					owner.updateCategoryList(result.getCategories());
					owner.updateCategoryLastUsedId(result.getLastUsedId());
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
