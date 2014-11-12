package com.vw.ide.client.fringemanagment.fringeload;


import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.SingleUploader;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.nmorel.gwtjackson.client.ObjectMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.vw.ide.client.dialog.VwmlDialogExt;
import com.vw.ide.client.fringemanagment.FringeManagerPresenter;
import com.vw.ide.client.event.uiflow.fringes.UpdateFringeEvent;
import com.vw.ide.client.utils.Utils;
import com.vw.ide.shared.CrudTypes;
import com.vw.ide.shared.servlet.fringes.model.Category;
import com.vw.ide.shared.servlet.fringes.model.Fringe;

/**
 * Allows to edit single line text
 * @author OMelnyk
 *
 */
public class FringeLoadDialog extends VwmlDialogExt {
	
	private static Logger logger = Logger.getLogger(FringeLoadDialog.class);
	
	final private String SERVLET_URL = "vwide/vwide/fringes/fringeupload";
	private static FringeLoadDialogUiBinder uiBinder = GWT.create(FringeLoadDialogUiBinder.class);
	private String parentPath;
    private SingleUploader singleUploader;	
    private FringeManagerPresenter presenter;
	

	interface FringeLoadDialogUiBinder extends UiBinder<Widget, FringeLoadDialog> {
	}

	public static interface FringeLoadResultMapper extends ObjectMapper<FringeLoadResult> {
	}

	
    public static class FringeLoadResult {

        private final Integer id;
        private final String path;
        private final String filename;


		@JsonCreator
        public FringeLoadResult( @JsonProperty( "id" ) Integer id,
                       @JsonProperty( "path" ) String path,
                       @JsonProperty( "filename" ) String filename) {
            this.id = id;
            this.path = path;
            this.filename = filename;
        }

        public Integer getId() {
            return id;
        }

        public String getPath() {
            return path;
        }
        
        public String getFileName() {
            return filename;
        }
  
    }		
	
    public FringeManagerPresenter getPresenter() {
    	return presenter;
    }
	
	public String getParentPath() {
		return parentPath;
	}

	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}

	public FringeLoadDialog(FringeManagerPresenter presenter, Category category,Fringe fringe) {
		setPredefinedButtons(PredefinedButton.CLOSE);
		this.presenter = presenter;
		super.setWidget(uiBinder.createAndBindUi(this));
		initLoader(category,fringe);
	}

	private void initLoader(Category category, Fringe fringe) {
		// Create a new uploader panel and attach it to the document
	    singleUploader = new SingleUploader();
	   
	    singleUploader.setServletPath(SERVLET_URL + "?" +  makeParams(category, fringe));
		logger.debug(singleUploader.getServletPath());
// 		You could change the internationalization creating your own Constants file
//	    defaultUploader.setI18Constants(c);
	    singleUploader.setEnabled(true);	    
	    add(singleUploader);
	    singleUploader.addOnFinishUploadHandler(onFinishUploaderHandler);
	    singleUploader.addOnStatusChangedHandler(onStatusChangedHandler);
	}

	private String makeParams(Category category, Fringe fringe) {
		String params = "id=" + fringe.getId().toString() +"&folder=\"" + category.getName();
		if (fringe.getPath().trim().length() > 0) {
		   params += Utils.FILE_SEPARATOR + fringe.getPath(); 
		}
		params += "\"";
		return params;   
	}
	
	private IUploader.OnStatusChangedHandler onStatusChangedHandler = new IUploader.OnStatusChangedHandler() {
		public void onStatusChanged(IUploader uploader) {
			logger.debug("uploader.getStatus(): " + uploader.getStatus());
		}
	};
	
	private IUploader.OnFinishUploaderHandler onFinishUploaderHandler = new IUploader.OnFinishUploaderHandler() {
		
	    public void onFinish(IUploader uploader) {
	    	FringeLoadResult fringeLoadResult  = deserializeResult(uploader.getServerInfo().message);
	    	if (uploader.getStatus() == Status.SUCCESS) {
	    		try {
	    			genEvent(fringeLoadResult);	
	    			String messageAlert = "Fringe " + fringeLoadResult.path + "/" + 
	    					fringeLoadResult.filename + " is loaded successfuly.";
	    			MessageBox messageBox = new MessageBox("Info", messageAlert);
	    			messageBox.show();
	    				
	    		} catch (Exception e) {
	    			String messageAlert = "Fringe " + fringeLoadResult.path + "/" + 
	    					fringeLoadResult.filename + " isn't loaded. " +  e.getLocalizedMessage();
	    			AlertMessageBox alertMessageBox = new AlertMessageBox("Warning", messageAlert);
	    			alertMessageBox.show();	    	  
	    		}
	    	} else {
				String messageAlert = "Fringe " + fringeLoadResult.path + "/" + 
    					fringeLoadResult.filename + " isn't loaded.";
				AlertMessageBox alertMessageBox = new AlertMessageBox("Warning", messageAlert);
				alertMessageBox.show();	    	  
	    	}
	      
	    }
	};


	 public void genEvent(FringeLoadResult fringeLoadResult) {
		 System.out.println("genEvent(FringeLoadResult fringeLoadResult): "+ fringeLoadResult.toString());
		 if (presenter.getView().getListStoreFringes() != null) {
			 for(int i = 0; i < presenter.getView().getListStoreFringes().size(); i++) {
				 if (presenter.getView().getListStoreFringes().get(i).getId() == fringeLoadResult.getId()) {
					 
					 Fringe fringe = presenter.getView().getListStoreFringes().get(i);

					 presenter.getView().getListStoreFringes().get(i).setFilename(fringeLoadResult.getFileName());
					 presenter.getView().getListStoreFringes().get(i).setLoaded(true);
					 presenter.getView().getFringesListByCategoryId(presenter.getView().getListStoreFringes().get(i).getCategoryId());
					 
					 fringe.setId(fringeLoadResult.getId());
					 fringe.setFilename(fringeLoadResult.getFileName());
					 fringe.setLoaded(true);
					 
					 presenter.getView().setFringeOperationType(CrudTypes.EDIT);
					 presenter.fireEvent(new UpdateFringeEvent(fringe));
					 break;
				 }
			 }; 
		 }
		 this.hide();
	 }
	
	
	 public FringeLoadResult deserializeResult(String result) {
	        FringeLoadResultMapper mapper = GWT.create(FringeLoadResultMapper.class); 
	        FringeLoadResult fringeLoadResult = mapper.read(result);
	        return fringeLoadResult;
	 }
	
	protected void onButtonPressed(TextButton textButton) {
		super.onButtonPressed(textButton);
  	   	hide();
	}


	
}
