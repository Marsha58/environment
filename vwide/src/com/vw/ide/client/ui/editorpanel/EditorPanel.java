package com.vw.ide.client.ui.editorpanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.event.BeforeCloseEvent;
import com.vw.ide.client.devboardext.DevelopmentBoardDialogHandlers.CloseUnsavedFileDialogHideHandler;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.event.uiflow.EditorTabClosedEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.ui.toppanel.FileSheet;

public class EditorPanel extends Composite {

	private static EditorPanelUiBinder uiBinder = GWT.create(EditorPanelUiBinder.class);

	interface EditorPanelUiBinder extends UiBinder<Widget, EditorPanel> {
	}
	
	private Presenter presenter = null;

	private Widget widget;

	@UiField
	TabPanel tabPanel;

	public EditorPanel() {
		if (widget == null) {
			widget = constructUi();
		}
		initWidget(widget);
		tabPanel.setTabScroll(true);
		tabPanel.setAnimScroll(true);
		tabPanel.setCloseContextMenu(true);
		tabPanel.addBeforeSelectionHandler(new BeforeSelectionHandler<Widget>() {
			@Override
			public void onBeforeSelection(BeforeSelectionEvent<Widget> event) {
				FileSheet curFileSheet = (FileSheet) event.getItem();
				((DevelopmentBoardPresenter)presenter).getView().setTextForEditorContentPanel(curFileSheet.getItemInfo().getAssociatedData().getAbsolutePath() + "/" + curFileSheet.getItemInfo().getAssociatedData().getName());
				((DevelopmentBoardPresenter)presenter).getView().getProjectPanel().select(curFileSheet.getItemInfo());
			}
		});
		
		tabPanel.addBeforeCloseHandler(new BeforeCloseEvent.BeforeCloseHandler<Widget> () {
			@Override
			public void onBeforeClose(BeforeCloseEvent<Widget> event) {
				FileSheet curFileSheet = (FileSheet)event.getItem();
				if (curFileSheet.getItemInfo().isEdited()) {
					event.setCancelled(true);
					ConfirmMessageBox box = new ConfirmMessageBox("Save", "Would you like to save '" + curFileSheet.getItemInfo().getAssociatedData().getName() + "'");
					box.addDialogHideHandler(new CloseUnsavedFileDialogHideHandler((DevelopmentBoardPresenter)presenter, event));
					box.show();
				}
				else {
					System.out.println("BeforeCloseEvent: " + event.toString());
					presenter.fireEvent(new EditorTabClosedEvent(event));
				}
			}
		});
		
		tabPanel.addResizeHandler(new ResizeHandler() {
			@Override
			public void onResize(ResizeEvent event) {
				 int iPanelHeight = event.getHeight();
				 for(int i = 0; i < tabPanel.getWidgetCount(); i++) {
					 Widget widget = tabPanel.getWidget(i);
					 if (widget instanceof FileSheet) {
						 widget.setHeight(String.valueOf(iPanelHeight) + "px");
					 }
				 }
			}
		});		
	}
	
	public void associatePresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	public void setFileEditedState(Widget associatedTabWidget, Boolean isEdited) {
		String sTitle = tabPanel.getConfig(associatedTabWidget).getText();
		if (sTitle.length() > 1) {
			if((sTitle.charAt(0) != '*') && isEdited) {
				sTitle = "*" + sTitle;
			}
			else
			if ((sTitle.charAt(0) == '*') && (!isEdited)) {
				sTitle = sTitle.substring(1);
			}
			tabPanel.getConfig(associatedTabWidget).setText(sTitle);
			tabPanel.update(associatedTabWidget, tabPanel.getConfig(associatedTabWidget));
		}
	}
	
	public TabPanel getTabPanel() {
	   return tabPanel;
	}	

	protected Presenter getAssociatedPresenter() {
		return this.presenter;
	}
	
	private Widget constructUi() {
		widget = uiBinder.createAndBindUi(this);
		widget.addStyleName("margin-10");
		return widget;
	}
}
