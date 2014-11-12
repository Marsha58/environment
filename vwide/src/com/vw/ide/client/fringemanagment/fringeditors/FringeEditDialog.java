package com.vw.ide.client.fringemanagment.fringeditors;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.vw.ide.client.dialog.VwmlDialogExt;
import com.vw.ide.client.fringemanagment.FringeManagerPresenter;
import com.vw.ide.shared.CrudTypes;
import com.vw.ide.shared.servlet.fringes.model.Fringe;

/**
 * Allows to edit single line text
 * @author OMelnyk
 *
 */
public class FringeEditDialog extends VwmlDialogExt {
	
	@UiField TextField fieldId;
	@UiField TextField fieldName;
	@UiField TextField fieldPath;
	@UiField TextField fieldCategoryId;
	@UiField TextArea  fieldDescription;
	
	private static FringeEditDialogUiBinder uiBinder = GWT.create(FringeEditDialogUiBinder.class);
    private FringeManagerPresenter presenter;
    private CrudTypes editingType;
    private Fringe fringe;
    private String selectedHideButton;
	

	public String getSelectedHideButton() {
		return selectedHideButton;
	}


	public void setSelectedHideButton(String selectedHideButton) {
		this.selectedHideButton = selectedHideButton;
	}


	public Fringe getFringe() {
		return fringe;
	}


	public void setFringe(Fringe fringe) {
		this.fringe = fringe;
	}


	public CrudTypes getEditingType() {
		return editingType;
	}


	public void setEditingType(CrudTypes editingType) {
		this.editingType = editingType;
	}
	
	public void setFields(Fringe fringe) {
		fieldId.setValue(fringe.getId().toString());
		fieldName.setValue(fringe.getName());
		fieldPath.setValue(fringe.getPath());
		fieldCategoryId.setValue(fringe.getCategoryId().toString());
		fieldDescription.setValue(fringe.getDescription());
	}

	public Fringe getUpdatedFringe() {
		fringe.setName(fieldName.getValue());
		fringe.setPath(fieldPath.getValue());
		fringe.setCategoryId(Integer.parseInt(fieldCategoryId.getValue()));
		fringe.setDescription(fieldDescription.getValue());
		return fringe;
	}
	

	interface FringeEditDialogUiBinder extends UiBinder<Widget, FringeEditDialog> {
	}

	
    public FringeManagerPresenter getPresenter() {
    	return presenter;
    }
	

	public FringeEditDialog(FringeManagerPresenter presenter, Fringe fringe) {
		setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
		this.presenter = presenter;
		this.fringe = fringe;
		super.setWidget(uiBinder.createAndBindUi(this));
		setFields(fringe);
	}


	
	protected void onButtonPressed(TextButton textButton) {
		getUpdatedFringe();
		selectedHideButton = textButton.getValue();
		super.onButtonPressed(textButton);
  	   	hide();
	}


	
}
