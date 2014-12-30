package com.vw.ide.client.dialog.fileopen;

import java.util.ArrayList;
import java.util.List;

import org.vectomatic.file.File;
import org.vectomatic.file.FileList;
import org.vectomatic.file.FileReader;
import org.vectomatic.file.FileUploadExt;
import org.vectomatic.file.events.ErrorEvent;
import org.vectomatic.file.events.ErrorHandler;
import org.vectomatic.file.events.LoadEndEvent;
import org.vectomatic.file.events.LoadEndHandler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.vw.ide.client.dialog.VwmlDialogExt;

/**
 * Allows to edit single line text
 * @author OMelnyk
 *
 */
public class FileOpenDialog extends VwmlDialogExt {
	
	private static class FileInfo {
		private File f;
		private String content;
		

		public FileInfo(File f) {
			super();
			this.f = f;
		}
		
		public File getFile() {
			return f;
		}
		
		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}
	}
	
	private static class SuccessFulFileLoaded implements LoadEndHandler {

		private FileOpenDialog owner;
		
		public SuccessFulFileLoaded(FileOpenDialog owner) {
			this.owner = owner;
		}
		
		@Override
		public void onLoadEnd(LoadEndEvent event) {
			if (owner != null) {
				owner.processLoadedFile();
				owner.toNextFile();
			}
		}
	}
	
	private static class FailureUponLoad implements ErrorHandler {

		private FileOpenDialog owner;
		
		public FailureUponLoad(FileOpenDialog owner) {
			this.owner = owner;
		}
		
		@Override
		public void onError(ErrorEvent event) {
			owner.toNextFile();
			String messageAlert = event.toDebugString();
			AlertMessageBox alertMessageBox = new AlertMessageBox("File", messageAlert);
			alertMessageBox.show();		    	  
		}
	}
	
	private static FileOpenDialogUiBinder uiBinder = GWT.create(FileOpenDialogUiBinder.class);
	private FileReader reader = new FileReader();
	private List<FileInfo> uploadedFiles = new ArrayList<FileInfo>();
	private int loadCounter = 0;
	private String parentPath;
	private Long projectId;
	
	public static interface ResultCallback {
		public void setResult(String result);
	}
	
	interface FileOpenDialogUiBinder extends UiBinder<Widget, FileOpenDialog> {
	}

	@UiField FieldLabel editLabelField;
	@UiField FileUploadExt fileField;
	
	public FileOpenDialog() {
		setPredefinedButtons(PredefinedButton.OK,PredefinedButton.CANCEL);
		super.setWidget(uiBinder.createAndBindUi(this));
		reader.addErrorHandler(new FailureUponLoad(this));
		reader.addLoadEndHandler(new SuccessFulFileLoaded(this));
	}

	public String getParentPath() {
		return parentPath;
	}

	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	
	public void setEditLabelText(String labelText) {
		editLabelField.setText(labelText);
	}

	public String getValue() {
		return "";
	}	
	
	@UiHandler("fileField")
	public void uploadFile(ChangeEvent event) {
		FileList fl = fileField.getFiles();
		if (fl != null) {
			for(int i = 0; i < fl.getLength(); i++) {
				uploadedFiles.add(new FileInfo(fl.getItem(i)));
			}
		}
	}	
	
	protected void onButtonPressed(TextButton textButton) {
		super.onButtonPressed(textButton);
		   if (textButton == getButton(PredefinedButton.OK)) {
		      if (uploadedFiles.size() == 0) {
		    	  String messageAlert = "At least one file must be selected";
		    	  AlertMessageBox alertMessageBox = new AlertMessageBox("File", messageAlert);
		    	  alertMessageBox.show();		    	  
		      }
		      else {
		    	  try {
	    			  startReadFile();
		    	  }
		    	  catch(Exception e) {
			    	  AlertMessageBox alertMessageBox = new AlertMessageBox("File", e.getLocalizedMessage());
			    	  alertMessageBox.show();		    	  
		    	  }
		      }
		   } 
		   else {
			   hide();
		   }
	}

	public String getFileName(int index) {
		return uploadedFiles.get(index).getFile().getName();
	}	
	
	public String getContent(int index) {
		return uploadedFiles.get(index).getContent();
	}

	public int getLoadedFiles() {
		return uploadedFiles.size();
	}
	
	protected void toNextFile() {
		loadCounter++;
	}
	
	protected void processLoadedFile() {
		String text = reader.getStringResult();
		uploadedFiles.get(loadCounter).setContent(text);
		if (uploadedFiles.size() - 1 == loadCounter) {
			this.hide();
		}
	}
	
	private void startReadFile()  {
		reader.readAsText(uploadedFiles.get(0).getFile());
	}
	
}
