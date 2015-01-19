package com.vw.ide.client.ui.toppanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Status;
import com.sencha.gxt.widget.core.client.Status.BoxStatusAppearance;
import com.sencha.gxt.widget.core.client.Status.StatusAppearance;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.event.uiflow.FileEditedEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.projects.FilesTypesEnum;
import com.vw.ide.client.ui.projectpanel.ProjectPanel.ProjectItemInfo;

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

public class FileSheet extends Composite {

	private static FileSheetUiBinder uiBinder = GWT
			.create(FileSheetUiBinder.class);

	interface FileSheetUiBinder extends UiBinder<Widget, FileSheet> {
	}

	private AceEditor aceEditor;
	private Presenter presenter;
	private String fileName;
	private String filePath;
	private ProjectItemInfo itemInfo;
	private FileEditedEvent editedEvent = new FileEditedEvent(null); 

	@UiField
	SimpleContainer fileContainer;
	@UiField
	DockLayoutPanel dockLayoutPanel;
	
	@UiField(provided = true)
	Status status  = new Status(GWT.<StatusAppearance> create(BoxStatusAppearance.class));
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

	public FileSheet(Presenter presenter, ProjectItemInfo itemInfo) {
		initWidget(uiBinder.createAndBindUi(this));
		this.presenter = presenter;
		this.itemInfo = itemInfo;
		this.filePath = itemInfo.getAssociatedData().getAbsolutePath();
		this.fileName = itemInfo.getAssociatedData().getName();
	}	

	private void updateEditor1CursorPosition() {
		AceEditorCursorPosition cursorPosition = aceEditor.getCursorPosition();
		rowCol.setText(cursorPosition.toString());
		int iAbsPos = aceEditor.getIndexFromPosition(cursorPosition);
		absPos.setText(String.valueOf(iAbsPos));
	}	
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	public String getFilePath() {
		return filePath;
	}
	
	public AceEditor getAceEditor() {
		return aceEditor;
	}

	public ProjectItemInfo getItemInfo() {
		return itemInfo;
	}

	public void setItemInfo(ProjectItemInfo itemInfo) {
		this.itemInfo = itemInfo;
	}

	public void setCursorPosition(int line, int pos) {
		aceEditor.gotoPos(line, pos);
	}

	public void markText(String text) {
		aceEditor.markText(aceEditor.createSelectionObject(), text);
	}
	
	public void setText(String text) {
		aceEditor.setText(text);
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
				if (!itemInfo.isEdited()) {
					editedEvent.setItemInfo(itemInfo);
					presenter.fireEvent(editedEvent);
				}
				System.out.println("invokeAceCallback: ");
			}
		});
		
		// add some annotations
		// editor1.addAnnotation(0, 1, "What's up?", AceAnnotationType.WARNING);
		// editor1.addAnnotation(2, 1, "This code is lame",
		// AceAnnotationType.ERROR);
		// editor1.setAnnotations();

		MarginData layoutData = new MarginData(1, 1, 1, 1);
		fileContainer.add(aceEditor,layoutData);
	}
}
