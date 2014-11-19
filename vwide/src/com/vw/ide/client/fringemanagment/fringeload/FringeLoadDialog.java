package com.vw.ide.client.fringemanagment.fringeload;

import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.SingleUploader;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.nmorel.gwtjackson.client.ObjectMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent.DialogHideHandler;
import com.sencha.gxt.widget.core.client.form.ListField;
import com.vw.ide.client.dialog.VwmlDialogExt;
import com.vw.ide.client.event.uiflow.fringes.UpdateFringeEvent;
import com.vw.ide.client.fringemanagment.FringeManagerPresenter;
import com.vw.ide.client.utils.Utils;
import com.vw.ide.shared.CrudTypes;
import com.vw.ide.shared.servlet.fringes.model.Category;
import com.vw.ide.shared.servlet.fringes.model.Fringe;

/**
 * Allows to edit single line text
 * 
 * @author OMelnyk
 * 
 */
public class FringeLoadDialog extends VwmlDialogExt {

	private static Logger logger = Logger.getLogger("");

	@UiField
	VerticalLayoutContainer place;
	@UiField
	SimpleContainer loaderPlace;
	@UiField
	VerticalLayoutContainer listPlace;

	final private String SERVLET_URL = "vwide/vwide/fringes/fringeupload";
	private static FringeLoadDialogUiBinder uiBinder = GWT.create(FringeLoadDialogUiBinder.class);
	private String parentPath;
	private SingleUploader singleUploader;
	private FringeManagerPresenter presenter;
	private ListStore<Data> listStore;
	private Fringe fringeForUpdate;
	private Boolean isFingeClassSet = false;

	interface FringeLoadDialogUiBinder extends UiBinder<Widget, FringeLoadDialog> {
	}

	public static interface FringeLoadResultMapper extends ObjectMapper<FringeLoadResult> {
	}

	public interface DataProperties extends PropertyAccess<Object> {
		@Path("name")
		ModelKeyProvider<Data> key();

		ValueProvider<Data, String> name();
	}

	public class Data {
		private String name;

		public Data(String name) {
			super();
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	public static class FringeLoadResult {

		private Integer id;
		private String path;
		private String filename;
		private String[] classes;

		@JsonCreator
		public FringeLoadResult(@JsonProperty("id") Integer id, @JsonProperty("path") String path, @JsonProperty("filename") String filename,
				@JsonProperty("classes") String[] classes) {
			this.id = id;
			this.path = path;
			this.filename = filename;
			this.classes = classes;
			

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

		public String[] getClasses() {
			return classes;
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

	public FringeLoadDialog(FringeManagerPresenter presenter, Category category, Fringe fringe) {
		setPredefinedButtons(PredefinedButton.CLOSE);
		this.presenter = presenter;
		super.setWidget(uiBinder.createAndBindUi(this));
		initLoader(category, fringe);
		createListField();
	}

	private void initLoader(Category category, Fringe fringe) {
		// Create a new uploader panel and attach it to the document
		singleUploader = new SingleUploader();

		singleUploader.setServletPath(SERVLET_URL + "?" + makeParams(category, fringe));
		logger.info("singleUploader.getServletPath()" + singleUploader.getServletPath()); 
		// You could change the internationalization creating your own Constants
		// file
		// defaultUploader.setI18Constants(c);
		singleUploader.setEnabled(true);
		loaderPlace.add(singleUploader);
		singleUploader.addOnFinishUploadHandler(onFinishUploaderHandler);
		singleUploader.addOnStatusChangedHandler(onStatusChangedHandler);
	}

	private String makeParams(Category category, Fringe fringe) {
		String params = "id=" + fringe.getId().toString() + "&folder=\"" + category.getName();
		if (fringe.getPath().trim().length() > 0) {
			params += Utils.FILE_SEPARATOR + fringe.getPath();
		}
		params += "\"";
//		logger.info("params : " + params);
		logger.log(Level.INFO,"params : " + params);
		return params;
	}

	private IUploader.OnStatusChangedHandler onStatusChangedHandler = new IUploader.OnStatusChangedHandler() {
		public void onStatusChanged(IUploader uploader) {
			logger.log(Level.INFO,"uploader.getStatus(): " + uploader.getStatus());
		}
	};


	private IUploader.OnFinishUploaderHandler onFinishUploaderHandler = new IUploader.OnFinishUploaderHandler() {

		public void onFinish(IUploader uploader) {

			FringeLoadResult fringeLoadResult = deserializeResult(uploader.getServerInfo().message);
			if (uploader.getStatus() == Status.SUCCESS) {
				try {
					if (fringeLoadResult.getClasses().length == 0) {
						 String messageAlert = "File " + fringeLoadResult.getFileName() + " doesn't contain class which implements fringe interface";
							AlertMessageBox alertMessageBox = new AlertMessageBox("Warning", messageAlert);
							alertMessageBox.show();				 
						
					} else {
						updateFringeProperties(fringeLoadResult);
						String messageAlert = "Fringe " + fringeLoadResult.path + "/" + fringeLoadResult.filename + " is loaded successfuly.";
						MessageBox messageBox = new MessageBox("Info", messageAlert);
						messageBox.show();
					}

				} catch (Exception e) {
					String messageAlert = "Fringe " + fringeLoadResult.path + "/" + fringeLoadResult.filename + " isn't loaded. "
							+ e.getLocalizedMessage();
					AlertMessageBox alertMessageBox = new AlertMessageBox("Warning", messageAlert);
					alertMessageBox.show();
				}
			} else {
				String messageAlert = "Fringe " + fringeLoadResult.path + "/" + fringeLoadResult.filename + " isn't loaded.";
				AlertMessageBox alertMessageBox = new AlertMessageBox("Warning", messageAlert);
				alertMessageBox.show();
			}

		}
	};

	public void updateFringeProperties(FringeLoadResult fringeLoadResult) {
		if (presenter.getView().getListStoreFringes() != null) {
			for (int i = 0; i < presenter.getView().getListStoreFringes().size(); i++) {
				if (presenter.getView().getListStoreFringes().get(i).getId() == fringeLoadResult.getId()) {

					fringeForUpdate = presenter.getView().getListStoreFringes().get(i);
					fringeForUpdate.setId(fringeLoadResult.getId());
					fringeForUpdate.setFilename(fringeLoadResult.getFileName());
					fringeForUpdate.setLoaded(true);
					presenter.getView().getListStoreFringes().get(i).setFilename(fringeLoadResult.getFileName());
					presenter.getView().getListStoreFringes().get(i).setLoaded(true);
					presenter.getView().getFringesListByCategoryId(presenter.getView().getListStoreFringes().get(i).getCategoryId());

					for (int j = 0; j < fringeLoadResult.getClasses().length; j++) {
						listStore.add(new Data(fringeLoadResult.getClasses()[j]));
					}
					break;
				}
			}
			;
		}

		// this.hide();
	}

	private void createListField() {

		DataProperties dp = GWT.create(DataProperties.class);
		listStore = new ListStore<Data>(dp.key());
		ListView<Data, String> listView = new ListView<Data, String>(listStore, dp.name());
		listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		listView.getSelectionModel().setVertical(true);
		listView.getSelectionModel().addSelectionHandler(new SelectionHandler<FringeLoadDialog.Data>() {

			@Override
			public void onSelection(SelectionEvent<Data> event) {
				presenter.getView().setFringeOperationType(CrudTypes.EDIT);
				fringeForUpdate.setClassname(event.getSelectedItem().getName());
				isFingeClassSet = true;
			}
		});

		ListField<Data, String> listField = new ListField<Data, String>(listView);

		listField.setPixelSize(300, 90);
		listPlace.add(new Label("Choose class which will be associated with this fringe"));
		listPlace.add(listField);
	}

	public void updateFringeClassname() {
//		presenter.getView().updateListStoreFringes(fringeForUpdate, CrudTypes.EDIT);
		presenter.getView().getFringesListByCategoryId(fringeForUpdate.getCategoryId());
		presenter.fireEvent(new UpdateFringeEvent(fringeForUpdate));
	}

	public FringeLoadResult deserializeResult(String result) {
		FringeLoadResultMapper mapper = GWT.create(FringeLoadResultMapper.class);
		FringeLoadResult fringeLoadResult = mapper.read(result);
		return fringeLoadResult;
	}

	protected void onButtonPressed(TextButton textButton) {
		// super.onButtonPressed(textButton);
		if (!isFingeClassSet) {
			ConfirmMessageBox box = new ConfirmMessageBox("Confirm", "You didn't choose fringe class. Exit in any case?");
			box.addDialogHideHandler(new DialogHideHandler() {

				@Override
				public void onDialogHide(DialogHideEvent event) {
					if (event.getHideButton().name().equalsIgnoreCase("YES")) {
						hide();
					}
				}
			});

			box.show();
		} else {
			updateFringeClassname();
			hide();
		}

	}

}
