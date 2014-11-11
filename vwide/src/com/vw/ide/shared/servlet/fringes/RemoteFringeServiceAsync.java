package com.vw.ide.shared.servlet.fringes;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vw.ide.shared.servlet.fringes.model.Category;
import com.vw.ide.shared.servlet.fringes.model.Fringe;

public interface RemoteFringeServiceAsync {

	void getCategories(AsyncCallback<RequestGetCategoriesResult> callback);
	void addCategory(Category category,AsyncCallback<RequestAddCategoryResult> callback);
	void updateCategory(Category category,AsyncCallback<RequestUpdateCategoryResult> callback);
	void deleteCategory(Integer fringeId,AsyncCallback<RequestDeleteCategoryResult> callback);

	void getFringes(AsyncCallback<RequestGetFringesResult> callback);
	void addFringe(Fringe fringe,AsyncCallback<RequestAddFringeResult> callback);
	void updateFringe(Fringe fringe,AsyncCallback<RequestUpdateFringeResult> callback);
	void deleteFringe(Integer fringeId,AsyncCallback<RequestDeleteFringeResult> callback);

}
