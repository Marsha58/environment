package com.vw.ide.client.service.fringes;

import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.vw.ide.client.service.ProcessedResult;
import com.vw.ide.client.service.fringes.FringeService.ServiceCallbackForAddCategory;
import com.vw.ide.client.service.fringes.FringeService.ServiceCallbackForAddFringe;
import com.vw.ide.client.service.fringes.FringeService.ServiceCallbackForDeleteCategory;
import com.vw.ide.client.service.fringes.FringeService.ServiceCallbackForDeleteFringe;
import com.vw.ide.client.service.fringes.FringeService.ServiceCallbackForGetCategories;
import com.vw.ide.client.service.fringes.FringeService.ServiceCallbackForGetFringes;
import com.vw.ide.client.service.fringes.FringeService.ServiceCallbackForUpdateCategory;
import com.vw.ide.client.service.fringes.FringeService.ServiceCallbackForUpdateFringe;
import com.vw.ide.shared.servlet.fringes.RemoteFringeServiceAsync;
import com.vw.ide.shared.servlet.fringes.RequestAddCategoryResult;
import com.vw.ide.shared.servlet.fringes.RequestAddFringeResult;
import com.vw.ide.shared.servlet.fringes.RequestDeleteCategoryResult;
import com.vw.ide.shared.servlet.fringes.RequestDeleteFringeResult;
import com.vw.ide.shared.servlet.fringes.RequestGetCategoriesResult;
import com.vw.ide.shared.servlet.fringes.RequestGetFringesResult;
import com.vw.ide.shared.servlet.fringes.RequestUpdateCategoryResult;
import com.vw.ide.shared.servlet.fringes.RequestUpdateFringeResult;
import com.vw.ide.shared.servlet.fringes.model.Category;
import com.vw.ide.shared.servlet.fringes.model.Fringe;

public class FringeServiceBroker {

	public static abstract class ResultCallback<R> {
		public abstract void handle(R result);
	}

	protected static class Result<R> extends ProcessedResult<R> {

		private ResultCallback<R> callback;
		
		public Result() {
		}
		
		public Result(ResultCallback<R> callback) {
			setCallback(callback);
		}

		public ResultCallback<R> getCallback() {
			return callback;
		}

		public void setCallback(ResultCallback<R> callback) {
			this.callback = callback;
		}

		@Override
		public void onFailure(Throwable caught) {
			AlertMessageBox alertMessageBox = new AlertMessageBox("Error", caught.getMessage());
			alertMessageBox.show();
		}

		@Override
		public void onSuccess(R result) {
			if (callback != null) {
				callback.handle(result);
			}
		}
	}
	
	/**
	 * Requests for the list of fringe categories
	 * @param resultCallback
	 */
	public static void requestForCategories( ResultCallback<RequestGetCategoriesResult> resultCallback) {
		RemoteFringeServiceAsync service = FringeService.instance().getServiceImpl();
		if (service != null) {
			ServiceCallbackForGetCategories cbk = FringeService.instance().buildCallbackForGetCategories();
			cbk.setProcessedResult(new Result<RequestGetCategoriesResult>(resultCallback));
			service.getCategories(cbk);
		}			
		
	}

	/**
	 * Requests for creating category
	 * @param category
	 * @param resultCallback
	 */
	public static void requestAddCategory(Category category, ResultCallback<RequestAddCategoryResult> resultCallback) {
		RemoteFringeServiceAsync service = FringeService.instance().getServiceImpl();
		if (service != null) {
			ServiceCallbackForAddCategory cbk = FringeService.instance().buildCallbackForAddCategory();
			cbk.setProcessedResult(new Result<RequestAddCategoryResult>(resultCallback));
			service.addCategory(category,cbk);
		}
	}	

	/**
	 * Requests for updating category
	 * @param fringe
	 * @param resultCallback
	 */
	public static void requestUpdateCategory(Category category, ResultCallback<RequestUpdateCategoryResult> resultCallback) {
		RemoteFringeServiceAsync service = FringeService.instance().getServiceImpl();
		if (service != null) {
			ServiceCallbackForUpdateCategory cbk = FringeService.instance().buildCallbackForUpdateCategory();
			cbk.setProcessedResult(new Result<RequestUpdateCategoryResult>(resultCallback));
			service.updateCategory(category,cbk);
		}
	}		

	/**
	 * Requests for deleting category
	 * @param fringeId
	 * @param resultCallback
	 */
	public static void requestDeleteCategory(Integer categoryId, ResultCallback<RequestDeleteCategoryResult> resultCallback) {
		RemoteFringeServiceAsync service = FringeService.instance().getServiceImpl();
		if (service != null) {
			ServiceCallbackForDeleteCategory cbk = FringeService.instance().buildCallbackForDeleteCategory();
			cbk.setProcessedResult(new Result<RequestDeleteCategoryResult>(resultCallback));
			service.deleteCategory(categoryId,cbk);
		}
	}		
	
	
	
	/**
	 * Requests for the list of fringes
	 * @param resultCallback
	 */
	public static void requestForFringes( ResultCallback<RequestGetFringesResult> resultCallback) {
		RemoteFringeServiceAsync service = FringeService.instance().getServiceImpl();
		if (service != null) {
			ServiceCallbackForGetFringes cbk = FringeService.instance().buildCallbackForGetFringes();
			cbk.setProcessedResult(new Result<RequestGetFringesResult>(resultCallback));
			service.getFringes(cbk);
		}			
		
	}
	
	/**
	 * Requests for creating fringe
	 * @param fringe
	 * @param resultCallback
	 */
	public static void requestAddFringe(Fringe fringe, ResultCallback<RequestAddFringeResult> resultCallback) {
		RemoteFringeServiceAsync service = FringeService.instance().getServiceImpl();
		if (service != null) {
			ServiceCallbackForAddFringe cbk = FringeService.instance().buildCallbackForAddFringe();
			cbk.setProcessedResult(new Result<RequestAddFringeResult>(resultCallback));
			service.addFringe(fringe,cbk);
		}
	}	

	/**
	 * Requests for updating fringe
	 * @param fringe
	 * @param resultCallback
	 */
	public static void requestUpdateFringe(Fringe fringe, ResultCallback<RequestUpdateFringeResult> resultCallback) {
		RemoteFringeServiceAsync service = FringeService.instance().getServiceImpl();
		if (service != null) {
			ServiceCallbackForUpdateFringe cbk = FringeService.instance().buildCallbackForUpdateFringe();
			cbk.setProcessedResult(new Result<RequestUpdateFringeResult>(resultCallback));
			service.updateFringe(fringe,cbk);
		}
	}		

	/**
	 * Requests for deleting fringe
	 * @param fringeId
	 * @param resultCallback
	 */
	public static void requestDeleteFringe(Integer fringeId, ResultCallback<RequestDeleteFringeResult> resultCallback) {
		RemoteFringeServiceAsync service = FringeService.instance().getServiceImpl();
		if (service != null) {
			ServiceCallbackForDeleteFringe cbk = FringeService.instance().buildCallbackForDeleteFringe();
			cbk.setProcessedResult(new Result<RequestDeleteFringeResult>(resultCallback));
			service.deleteFringe(fringeId,cbk);
		}
	}		
	
}
