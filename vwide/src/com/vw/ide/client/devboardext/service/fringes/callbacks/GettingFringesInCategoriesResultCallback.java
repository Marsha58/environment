package com.vw.ide.client.devboardext.service.fringes.callbacks;

import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.service.fringes.FringeServiceBroker;
import com.vw.ide.shared.servlet.fringes.RequestGetFringesInCategoriesResult;

public class GettingFringesInCategoriesResultCallback extends FringeServiceBroker.ResultCallback<RequestGetFringesInCategoriesResult>{

	private DevelopmentBoardPresenter owner = null;
	
	public GettingFringesInCategoriesResultCallback(DevelopmentBoardPresenter presenter) {
		this.owner = presenter;
	}
	
	@Override
	public void handle(RequestGetFringesInCategoriesResult result) {
		String messageAlert;
		if (result.getRetCode().intValue() != 0) {
			messageAlert = "Get fringes in categories operation '" 
					+ "' failed.\r\nResult'" + result.getResult() + "'";
			AlertMessageBox alertMessageBox = new AlertMessageBox(
					"Warning", messageAlert);
			alertMessageBox.show();
		} else {
			switch (result.getRetCode()) {
				case 0:
					owner.getView().updateFringesInCategories(result.getCategoriesList(),result.getFringesList());
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
