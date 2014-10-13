package com.vw.ide.client.ui.windowspanel;

import com.google.gwt.core.client.GWT;
//import com.google.gwt.sample.mail.client.MailItem;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.vw.ide.client.presenters.Presenter;

import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorMode;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorTheme;

public class WindowsPanelView extends Composite {

	private static WindowsPanelViewUiBinder uiBinder = GWT
			.create(WindowsPanelViewUiBinder.class);

	interface WindowsPanelViewUiBinder extends 	UiBinder<Widget, WindowsPanelView>  {
	}

	private AceEditor logAceEditor;
	
	@UiField HTML bodyDebug;
	@UiField HTML bodyProblems;
	@UiField HTML bodyErrors;
	@UiField HTML bodySearch;
	
	@UiField
	SimpleContainer serverLogContainer;

	@UiField(provided = true)
	MarginData centerData = new MarginData();
	@UiField(provided = true)
	BorderLayoutData southData = new BorderLayoutData(80);
	
	
	private Presenter presenter = null;
	
	
	private Widget widget;	
	
	
	
	public WindowsPanelView() {
	    if (widget == null) {
	        widget = constructUi();
	      }		
		initWidget(widget);
	}

	public void associatePresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	protected Presenter getAssociatedPresenter() {
		return this.presenter;
	}

   private Widget constructUi() {
	    widget = uiBinder.createAndBindUi(this);
	    widget.addStyleName("margin-10");
	    constructEditor();
	    return widget;
   }	
	
   private void constructEditor() {
	   logAceEditor = new AceEditor();
	   logAceEditor.setWidth("100%");
	   logAceEditor.setHeight("100%");
	   logAceEditor.startEditor(); // must be called before calling
									// setTheme/setMode/etc.
	   logAceEditor.setTheme(AceEditorTheme.CHROME);
	   logAceEditor.setText("");
	   logAceEditor.setMode(AceEditorMode.JSON);	
	   MarginData layoutData = new MarginData(1,1,1,1);
	   serverLogContainer.add(logAceEditor,layoutData); 
   }
   
   public void appendLog(String logContent) {
	   String sTmp = logAceEditor.getText();
	   sTmp += logContent;
	   logAceEditor.setText(sTmp);
   }   

}
