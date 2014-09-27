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
import com.sencha.gxt.widget.core.client.event.BeforeCloseEvent;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.event.uiflow.EditorTabClosedEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.ui.toppanel.FileSheet;

public class EditorPanel extends Composite {

	private static EditorPanelUiBinder uiBinder = GWT
			.create(EditorPanelUiBinder.class);

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
				((DevelopmentBoardPresenter)presenter).getEditorContentPanel().setHeadingText(curFileSheet.getFileName());
			}
		});
		
		tabPanel.addBeforeCloseHandler(new BeforeCloseEvent.BeforeCloseHandler<Widget> () {

			@Override
			public void onBeforeClose(BeforeCloseEvent<Widget> event) {
				System.out.println("BeforeCloseEvent: " + event.toString());
				presenter.fireEvent(new EditorTabClosedEvent(event));
			}
			
		});
		
		tabPanel.addResizeHandler(new ResizeHandler() {
			@Override
			public void onResize(ResizeEvent event) {
				 int iPanelHeight = event.getHeight();
				 for(int i=0; i< tabPanel.getWidgetCount(); i ++) {
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

	protected Presenter getAssociatedPresenter() {
		return this.presenter;
	}

	public TabPanel getTabPanel() {
	   return tabPanel;
	}	
	
	private Widget constructUi() {
		widget = uiBinder.createAndBindUi(this);
		widget.addStyleName("margin-10");
		return widget;
	}
	

}
