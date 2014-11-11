package com.vw.ide.client.event.uiflow.fringes;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.event.handler.fringes.DeleteCategoryHandler;

/**
 * Fired when user delete Category
 * @author Omelnyk
 *
 */
public class DeleteCategoryEvent  extends GwtEvent<DeleteCategoryHandler> {

	
	private Integer categoryId;

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}


	public static Type<DeleteCategoryHandler> TYPE = new Type<DeleteCategoryHandler>();
	
	public DeleteCategoryEvent() {
		super();
	}

	public DeleteCategoryEvent(Integer categoryId) {
		super();
		this.categoryId = categoryId;
	}
	

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<DeleteCategoryHandler> getAssociatedType() {
		return TYPE;
	}


	@Override
	protected void dispatch(DeleteCategoryHandler handler) {
		handler.onDeleteCategory(this);
	}
}



