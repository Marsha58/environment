package com.vw.ide.client.ui.windowspanel;

import com.google.gwt.core.client.GWT;
//import com.google.gwt.sample.mail.client.MailItem;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.container.HasLayout;
import com.vw.ide.client.presenters.Presenter;

public class WindowsPanelView extends Composite {

	private static WindowsPanelViewUiBinder uiBinder = GWT
			.create(WindowsPanelViewUiBinder.class);

	interface WindowsPanelViewUiBinder extends 	UiBinder<Widget, WindowsPanelView>  {
	}

	@UiField HTML bodyDebug;
	@UiField HTML bodyProblems;
	@UiField HTML bodyErrors;
	@UiField HTML bodySearch;

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
		    return widget;
		  }	
	
/*		    subject.setInnerText(item.subject);
	  public void setItem(MailItem item) {
		    sender.setInnerText(item.sender);
		    recipient.setInnerHTML("foo@example.com");

		    // WARNING: For the purposes of this demo, we're using HTML directly, on
		    // the assumption that the "server" would have appropriately scrubbed the
		    // HTML. Failure to do so would open your application to XSS attacks.
		    body.setHTML(item.body);
		  }
 */		    
	  
	  
	  



}
