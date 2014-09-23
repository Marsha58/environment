package com.vw.ide.client.ui.toppanel;

import java.util.Arrays;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.StringLabelProvider;
import com.sencha.gxt.widget.core.client.Status;
import com.sencha.gxt.widget.core.client.Status.BoxStatusAppearance;
import com.sencha.gxt.widget.core.client.Status.StatusAppearance;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.vw.ide.client.devboardext.DevelopmentBoard;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.ui.toppanel.TopPanel.ActiveTheme;
import com.vw.ide.client.ui.toppanel.TopPanel.Theme;

import edu.ycp.cs.dh.acegwt.client.ace.AceAnnotationType;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorCallback;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorMode;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorTheme;

public class FileSheet extends Composite {

	private static FileSheetUiBinder uiBinder = GWT
			.create(FileSheetUiBinder.class);

	interface FileSheetUiBinder extends UiBinder<Widget, FileSheet> {
	}

	private AceEditor aceEditor;
	private Presenter presenter;
	private Long fileId;
	private String fileName;

	@UiField
	SimpleContainer fileContainer;
	@UiField
	DockLayoutPanel dockLayoutPanel;
	@UiField(provided = true)
	Status charCount = new Status(GWT.<StatusAppearance> create(BoxStatusAppearance.class));
	@UiField(provided = true)
	Status wordCount = new Status(GWT.<StatusAppearance> create(BoxStatusAppearance.class));
	

	

	public FileSheet() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public FileSheet(Presenter presenter, Long fileId, String fileName) {
		initWidget(uiBinder.createAndBindUi(this));
		this.presenter = presenter;
		this.fileId = fileId;
		this.fileName = fileName;
	}	
	
	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}
	
	public Long getFileId() {
		return fileId;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	
	public AceEditor getAceEditor() {
		return aceEditor;
	}
	
	public void constructEditor(String textFile) {
		aceEditor = new AceEditor();
		aceEditor.setWidth("100%");
		aceEditor.setHeight("600px");
		
		

		// Try out custom code completer
		// AceEditor.addCompletionProvider(new MyCompletionProvider());

		// start the first editor and set its theme and mode
		aceEditor.startEditor(); // must be called before calling
								// setTheme/setMode/etc.
		aceEditor.setTheme(((DevelopmentBoardPresenter) presenter).getTopPanel().comboATh.getCurrentValue());
		aceEditor.setMode(AceEditorMode.JAVA);

		aceEditor.setText(textFile);

		aceEditor.addOnChangeHandler(new AceEditorCallback() {
			@Override
			public void invokeAceCallback(JavaScriptObject obj) {
				
				System.out.println("invokeAceCallback: ");
			}
			
		});
		// add some annotations
		// editor1.addAnnotation(0, 1, "What's up?", AceAnnotationType.WARNING);
		// editor1.addAnnotation(2, 1, "This code is lame",
		// AceAnnotationType.ERROR);
		// editor1.setAnnotations();

		fileContainer.add(aceEditor);
	}

}
