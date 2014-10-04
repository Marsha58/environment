package com.vw.ide.client.ui.toppanel;

import java.util.Arrays;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.layout.client.Layout;
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
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.vw.ide.client.devboardext.DevelopmentBoard;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.projects.FilesTypesEnum;
import com.vw.ide.client.ui.toppanel.TopPanel.ActiveTheme;
import com.vw.ide.client.ui.toppanel.TopPanel.Theme;

import edu.ycp.cs.dh.acegwt.client.ace.AceAnnotationType;
import edu.ycp.cs.dh.acegwt.client.ace.AceCompletion;
import edu.ycp.cs.dh.acegwt.client.ace.AceCompletionCallback;
import edu.ycp.cs.dh.acegwt.client.ace.AceCompletionProvider;
import edu.ycp.cs.dh.acegwt.client.ace.AceCompletionSnippet;
import edu.ycp.cs.dh.acegwt.client.ace.AceCompletionSnippetSegment;
import edu.ycp.cs.dh.acegwt.client.ace.AceCompletionSnippetSegmentLiteral;
import edu.ycp.cs.dh.acegwt.client.ace.AceCompletionSnippetSegmentTabstopItem;
import edu.ycp.cs.dh.acegwt.client.ace.AceCompletionValue;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorCallback;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorCursorPosition;
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
	
	@UiField(provided = true)
	Status rowCol = new Status(GWT.<StatusAppearance> create(BoxStatusAppearance.class));
	@UiField(provided = true)
	Status absPos = new Status(GWT.<StatusAppearance> create(BoxStatusAppearance.class));	

	private static class MyCompletionProvider implements AceCompletionProvider {
		@Override		
		public void getProposals(AceEditor editor, AceEditorCursorPosition pos, String prefix, AceCompletionCallback callback) {
			GWT.log("sending completion proposals");
			callback.invokeWithCompletions(new AceCompletion[]{
					new AceCompletionValue("first", "firstcompletion", "custom", 10),
					new AceCompletionValue("second", "secondcompletion", "custom", 11),
					new AceCompletionValue("third", "thirdcompletion", "custom", 12),
					new AceCompletionSnippet("fourth (snippets)",
							new AceCompletionSnippetSegment[]{
							new AceCompletionSnippetSegmentLiteral("filler_"),
							new AceCompletionSnippetSegmentTabstopItem("tabstop1"),
							new AceCompletionSnippetSegmentLiteral("_\\filler_"), // putting backslash in here to prove escaping is working
							new AceCompletionSnippetSegmentTabstopItem("tabstop2"),
							new AceCompletionSnippetSegmentLiteral("_$filler_"), // putting dollar in here to prove escaping is working
							new AceCompletionSnippetSegmentTabstopItem("tabstop3"),
							new AceCompletionSnippetSegmentLiteral("\nnextlinefiller_"),
							new AceCompletionSnippetSegmentTabstopItem("tabstop}4"),
							new AceCompletionSnippetSegmentLiteral("_filler_"),
							new AceCompletionSnippetSegmentTabstopItem("") /* Empty tabstop -- tab to end of replacement text */
					},"csnip", 14)
			});
		}

	} 

	public FileSheet() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public FileSheet(Presenter presenter, Long fileId, String fileName) {
		initWidget(uiBinder.createAndBindUi(this));
		this.presenter = presenter;
		this.fileId = fileId;
		this.fileName = fileName;
	}	


	private void updateEditor1CursorPosition() {
		AceEditorCursorPosition cursorPosition = aceEditor.getCursorPosition();
		rowCol.setText(cursorPosition.toString());
		
		
		int iAbsPos = aceEditor.getIndexFromPosition(cursorPosition);
		absPos.setText(String.valueOf(iAbsPos));
	}	
	
	public void setDockLayoutPanel(String sHeight) {
		dockLayoutPanel.setHeight(sHeight);
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
	
	public void constructEditor(String textFile, FilesTypesEnum fileType) {
		aceEditor = new AceEditor();
		aceEditor.setWidth("100%");
		aceEditor.setHeight("100%");
		
		try {
			AceEditor.addCompletionProvider(new MyCompletionProvider()); 
		} catch (Exception e) {
			// TODO: handle exception
		}

		// Try out custom code completer
		// AceEditor.addCompletionProvider(new MyCompletionProvider());

		// start the first editor and set its theme and mode
		aceEditor.startEditor(); // must be called before calling
								// setTheme/setMode/etc.
		aceEditor.setTheme(((DevelopmentBoardPresenter) presenter).getTopPanel().comboATh.getCurrentValue());

		
		aceEditor.setText(textFile);
		
		
		// use cursor position change events to keep a label updated
		// with the current row/col
		aceEditor.addOnCursorPositionChangeHandler(new AceEditorCallback() {
			@Override
			public void invokeAceCallback(JavaScriptObject obj) {
				updateEditor1CursorPosition();
			}
		});
		
		switch (fileType) {
		case VWML:
			aceEditor.setMode(AceEditorMode.VWML);	
			break;
		case JAVA:
			aceEditor.setMode(AceEditorMode.JAVA);	
			break;
		case C:
			aceEditor.setMode(AceEditorMode.C_CPP);	
			break;
		case CPP:
			aceEditor.setMode(AceEditorMode.C_CPP);	
			break;
		case CSS:
			aceEditor.setMode(AceEditorMode.CSS);	
			break;
		case HTML:
			aceEditor.setMode(AceEditorMode.HTML);	
			break;
		case JSON:
			aceEditor.setMode(AceEditorMode.JSON);	
			break;
		case XML:
			aceEditor.setMode(AceEditorMode.XML);	
			break;
		case JS:
			aceEditor.setMode(AceEditorMode.JAVASCRIPT);	
			break;
		default:
			aceEditor.setMode(AceEditorMode.TEXT);	
			break;
		}
		


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

		MarginData layoutData = new MarginData(1,1,1,1);
		fileContainer.add(aceEditor,layoutData);
	}

}
