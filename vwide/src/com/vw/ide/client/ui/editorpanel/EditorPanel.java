package com.vw.ide.client.ui.editorpanel;

import org.gwtbootstrap3.client.ui.constants.TabPosition;
import org.springframework.jdbc.datasource.SimpleConnectionHandle;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.BeforeCloseEvent;

import edu.ycp.cs.dh.acegwt.client.ace.AceAnnotationType;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorMode;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorTheme;

import com.sencha.gxt.widget.core.client.Status;
import com.sencha.gxt.widget.core.client.Status.BoxStatusAppearance;
import com.sencha.gxt.widget.core.client.Status.StatusAppearance;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.vw.ide.client.event.handler.SelectFileHandler;
import com.vw.ide.client.event.uiflow.EditorTabClosedEvent;
import com.vw.ide.client.event.uiflow.SelectFileEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.ui.toppanel.FileSheet;

public class EditorPanel extends Composite {

	private static EditorPanelUiBinder uiBinder = GWT
			.create(EditorPanelUiBinder.class);

	interface EditorPanelUiBinder extends UiBinder<Widget, EditorPanel> {
	}
	
	private Presenter presenter = null;

	private Widget widget;
	private AceEditor editor1;

//	@UiField
//	SimpleContainer file1;
//	@UiField
//	Status status;
	@UiField
	TabPanel tabPanel;
//	@UiField
//	DockLayoutPanel dockLayoutPanel1;
//	@UiField(provided = true)
//	Status charCount = new Status(
//			GWT.<StatusAppearance> create(BoxStatusAppearance.class));
//	@UiField(provided = true)
//	Status wordCount = new Status(
//			GWT.<StatusAppearance> create(BoxStatusAppearance.class));

	private static final String JAVA_TEXT = "public class Hello {\n"
			+ "\tpublic static void main(String[] args) {\n"
			+ "\t\tSystem.out.println(\"Hello, world!\");\n" + "\t}\n" + "}\n";

	public EditorPanel() {
		if (widget == null) {
			widget = constructUi();
		}
		initWidget(widget);

		
		tabPanel.setTabScroll(true);
		tabPanel.setAnimScroll(true);
		tabPanel.setCloseContextMenu(true);
		tabPanel.addBeforeCloseHandler(new BeforeCloseEvent.BeforeCloseHandler<Widget> () {

			@Override
			public void onBeforeClose(BeforeCloseEvent<Widget> event) {
				System.out.println("BeforeCloseEvent: " + event.toString());
				presenter.fireEvent(new EditorTabClosedEvent(event));
			}
			
		});
		
		if (editor1 == null) {
			constructEditor();
		}
		


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
	

	private void constructEditor() {
		editor1 = new AceEditor();
		editor1.setWidth("100%");
		editor1.setHeight("600px");

		// Try out custom code completer
		// AceEditor.addCompletionProvider(new MyCompletionProvider());



		// start the first editor and set its theme and mode
		editor1.startEditor(); // must be called before calling
								// setTheme/setMode/etc.
		editor1.setTheme(AceEditorTheme.ECLIPSE);
		editor1.setMode(AceEditorMode.JAVA);

		editor1.setText(JAVA_TEXT);

		// add some annotations
		editor1.addAnnotation(0, 1, "What's up?", AceAnnotationType.WARNING);
		editor1.addAnnotation(2, 1, "This code is lame",
				AceAnnotationType.ERROR);
		editor1.setAnnotations();

//		file1.add(editor1);
	}

	private Widget constructUi() {
		widget = uiBinder.createAndBindUi(this);
		widget.addStyleName("margin-10");
		return widget;
	}
	

}
