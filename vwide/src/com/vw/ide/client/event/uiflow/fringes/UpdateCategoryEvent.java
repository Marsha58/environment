package com.vw.ide.client.event.uiflow.fringes;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.event.handler.fringes.UpdateCategoryHandler;
import com.vw.ide.shared.servlet.fringes.model.Category;

/**
 * Fired when user update category item
 * @author Omelnyk
 *
 */
public class UpdateCategoryEvent extends GwtEvent<UpdateCategoryHandler> {

	
	private Category category;

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}


	public static Type<UpdateCategoryHandler> TYPE = new Type<UpdateCategoryHandler>();
	
	public UpdateCategoryEvent() {
		super();
	}

	public UpdateCategoryEvent(Category category) {
		super();
		this.category = category;
	}
	
	


	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<UpdateCategoryHandler> getAssociatedType() {
		return TYPE;
	}


	@Override
	protected void dispatch(UpdateCategoryHandler handler) {
		handler.onUpdateCategory(this);
	}
}

