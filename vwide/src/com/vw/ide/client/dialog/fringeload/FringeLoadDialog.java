package com.vw.ide.client.dialog.fringeload;

import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.IUploader.UploadedInfo;
import gwtupload.client.MultiUploader;
import gwtupload.client.PreloadedImage;
import gwtupload.client.PreloadedImage.OnLoadPreloadedImageHandler;
import gwtupload.client.SingleUploader;

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
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.vw.ide.client.dialog.VwmlDialogExt;
import com.vw.ide.shared.servlet.fringes.model.Fringe;

/**
 * Allows to edit single line text
 * @author OMelnyk
 *
 */
public class FringeLoadDialog extends VwmlDialogExt {
	
	@UiField SimplePanel simplePanel;
	
	
	private static FringeLoadDialogUiBinder uiBinder = GWT.create(FringeLoadDialogUiBinder.class);
	private String parentPath;
    private SingleUploader singleUploader;	
	

	interface FringeLoadDialogUiBinder extends UiBinder<Widget, FringeLoadDialog> {
	}

	
	public String getParentPath() {
		return parentPath;
	}

	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}

	public FringeLoadDialog(Fringe fringe) {
		setPredefinedButtons(PredefinedButton.CLOSE);
		super.setWidget(uiBinder.createAndBindUi(this));
		initLoader(fringe);
	}

	private void initLoader(Fringe fringe) {
		// Create a new uploader panel and attach it to the document
	    singleUploader = new SingleUploader();
	    singleUploader.setServletPath("/vwide/fringeupload" + "?id" + fringe.getId().toString());
// 		You could change the internationalization creating your own Constants file
//	    defaultUploader.setI18Constants(c);
	    singleUploader.setEnabled(true);	    
	    add(singleUploader);
	    singleUploader.addOnFinishUploadHandler(onFinishUploaderHandler);
	}

	
	private IUploader.OnFinishUploaderHandler onFinishUploaderHandler = new IUploader.OnFinishUploaderHandler() {
	    public void onFinish(IUploader uploader) {
	      if (uploader.getStatus() == Status.SUCCESS) {
	        // The server sends useful information to the client by default
	        UploadedInfo info = uploader.getServerInfo();
//	        System.out.println("File name " + info.name); 
//	        System.out.println("File content-type " + info.ctype);
//	        System.out.println("File size " + info.size);
	        System.out.println("Server message " + info.message);
	      }
	    }
	  };

	
	
	protected void onButtonPressed(TextButton textButton) {
		super.onButtonPressed(textButton);
  	   	hide();
	}


	
}
