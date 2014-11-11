package com.vw.ide.client.event.uiflow.fringes;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.event.handler.fringes.AddCategoryHandler;
import com.vw.ide.shared.servlet.fringes.model.Category;

/**
 * Fired when user add (create) Category of fringes
 * @author Omelnyk
 *
 */
public class AddCategoryEvent extends GwtEvent<AddCategoryHandler> {

	
	private Category category;

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}


	public static Type<AddCategoryHandler> TYPE = new Type<AddCategoryHandler>();
	
	public AddCategoryEvent() {
		super();
	}

	public AddCategoryEvent(Category category) {
		super();
		this.category = category;
	}
	

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<AddCategoryHandler> getAssociatedType() {
		return TYPE;
	}


	@Override
	protected void dispatch(AddCategoryHandler handler) {
		handler.onAddCategory(this);
	}

}


