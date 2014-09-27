package com.vw.ide.client.dialog.newvwmlproj;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vw.ide.client.devboardext.DevelopmentBoard;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.dialog.VwmlDialogExt;
import com.vw.ide.client.dialog.remotebrowser.RemoteDirectoryBrowserDialog;
import com.vw.ide.client.dialog.remotebrowser.RemoteDirectoryBrowserDialogExt;
import com.vw.ide.client.dialog.remotebrowser.RemoteDirectoryBrowserDialogExt.DirContentResult;
import com.vw.ide.client.event.uiflow.GetDirContentEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.service.ProcessedResult;
import com.vw.ide.client.service.remotebrowser.RemoteBrowserService;
import com.vw.ide.client.service.remotebrowser.RemoteBrowserService.ServiceCallbackForCompleteContent;
import com.vw.ide.client.service.remotebrowser.RemoteBrowserService.ServiceCallbackForProjectCreation;
import com.vw.ide.shared.servlet.remotebrowser.RemoteDirectoryBrowserAsync;
import com.vw.ide.shared.servlet.remotebrowser.RequestProjectCreationResult;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.BlurEvent.BlurHandler;
import com.sencha.gxt.widget.core.client.event.FocusEvent.FocusHandler;
import com.sencha.gxt.widget.core.client.event.BlurEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * New VWML project dialog
 * 
 * @author Oleg
 * 
 */
public class NewVwmlProjectDialogExt extends VwmlDialogExt {

	private static String s_remoteDirectoryBrowserCaption = "Select project's folder";
	private static NewVwmlProjectDialogExtUiBinder uiBinder = GWT
			.create(NewVwmlProjectDialogExtUiBinder.class);
	
	private Presenter presenter = null;

	// @UiField FlowPanel dialogMainPanel;
	@UiField
	TextField tfVWMLProjectName;
	@UiField
	TextField tfVWMLProjectPath;
	@UiField
	TextButton browseVwmlProjPath;
	@UiField
	TextField tfVWMLMainModule;
	@UiField
	TextField tfVWMLAuthor;
	@UiField
	TextArea tfVWMLDescr;
	@UiField
	TextField tfJavaPackageName;
	@UiField
	TextField tfJavaSourcePath;

	// @UiField TextButton ok;
	// @UiField TextButton cancel;

	interface NewVwmlProjectDialogExtUiBinder extends
			UiBinder<Widget, NewVwmlProjectDialogExt> {
	}

	public NewVwmlProjectDialogExt() {
		super.setWidget(uiBinder.createAndBindUi(this));
		tfVWMLProjectName.addFocusHandler(new FocusHandler() {

			@Override
			public void onFocus(
					com.sencha.gxt.widget.core.client.event.FocusEvent event) {
				String sName = tfVWMLProjectName.getText();
				tfJavaPackageName.setText(sName);
			}
		});

		tfVWMLProjectName.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				String sName = tfVWMLProjectName.getText();
				tfJavaPackageName.setText(makePackageName(sName));
			}
		});

		tfVWMLMainModule.setText("com.win.strategy.battle.vwml.model");
		tfVWMLAuthor.setText("Win Interactive LLC");
		tfVWMLDescr
				.setText("Here author  should write short description about project");
		tfJavaSourcePath.setText("java_src");
	}

	public class ProjectCreationResult extends
			ProcessedResult<RequestProjectCreationResult> {

		public ProjectCreationResult() {

		}

		@Override
		public void onFailure(Throwable caught) {
			AlertMessageBox alertMessageBox = new AlertMessageBox("Error",
					caught.getMessage());
			alertMessageBox.show();
		}

		@Override
		public void onSuccess(RequestProjectCreationResult result) {
			if (result.getRetCode().intValue() != 0) {
				String messageAlert = "The operation '" + result.getOperation()
						+ "' failed.\r\nResult'" + result.getResult() + "'";
				AlertMessageBox alertMessageBox = new AlertMessageBox(
						"Warning", messageAlert);
				alertMessageBox.show();
			} else {
				((DevelopmentBoardPresenter) presenter).fireEvent(new GetDirContentEvent());
			}
		}

	}

	
	public void associatePresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	protected Presenter getAssociatedPresenter() {
		return this.presenter;
	}		
	
	public NewVwmlProjectDialogExt(String firstName) {
		super.setWidget(uiBinder.createAndBindUi(this));
	}

	public String getText() {
		return null;
	}

	public void setText(String text) {
	}

	public String capitalizeFirstLetter(String input) {
		String sWord = "";
		String firstLetter = input.substring(0, 1).toUpperCase();
		String restLetters = input.substring(1, input.length());
		return sWord = firstLetter + restLetters;
	}

	public String makePackageName(String projectName) {
		StringBuffer sPackageName = new StringBuffer();
		String[] arrProjectName = projectName.split(" ");
		for (String curWord : arrProjectName) {
			sPackageName.append(capitalizeFirstLetter(curWord));
		}

		return sPackageName.toString();
	}

	public static String format(final String format, final String... args) {
		String[] split = format.split("%s");
		final StringBuffer msg = new StringBuffer();
		for (int pos = 0; pos < split.length - 1; pos += 1) {
			msg.append(split[pos]);
			msg.append(args[pos]);
		}
		msg.append(split[split.length - 1]);
		return msg.toString();
	}

	// @UiHandler("ok")
	// void onOkClick(ClickEvent event) {
	//
	// }

	// @UiHandler("cancel")
	// void onCancelClick(ClickEvent event) {
	// this.hide();
	// }

	@UiHandler("browseVwmlProjPath")
	void onBrowseVwmlProjPathClick(SelectEvent event) {
		RemoteDirectoryBrowserDialogExt d = new RemoteDirectoryBrowserDialogExt();
		d.setLoggedAsUser(getLoggedAsUser());
		d.prepare();
		// d.show(s_remoteDirectoryBrowserCaption, null,0,0);
		d.setSize("550", "400");
		d.showCenter(s_remoteDirectoryBrowserCaption, null);
	}

	protected void onButtonPressed(TextButton textButton) {
		super.onButtonPressed(textButton);
		if (textButton == getButton(PredefinedButton.OK)) {

			String params = format("options {\n" + "language=_java_ {\n"
					+ "package = \"%s\"\n" + "path = \"%s\"\n"
					+ "author = \"%s\"\n" + "project_name = \"%s\"\n"
					+ "description = \"%s\"\n" + "beyond {\n" + "}\n" + "}\n"
					+ "conflicting {\n" + "}\n" + "}",
					tfJavaPackageName.getText(), tfJavaSourcePath.getText(),
					tfVWMLAuthor.getText(), tfVWMLProjectName.getText(),
					tfVWMLDescr.getText());

			System.out.println(params);
			requestForProjectCreation(tfVWMLProjectName.getText(), params);
			hide();
		}
	}

	protected void requestForProjectCreation(String projectName, String params) {
		RemoteDirectoryBrowserAsync service = RemoteBrowserService.instance()
				.getServiceImpl();

		if (service != null) {
			ServiceCallbackForProjectCreation cbk = RemoteBrowserService
					.instance().buildCallbackForProjectCreation();
			cbk.setProcessedResult(new ProjectCreationResult());
			service.createProject(this.getLoggedAsUser(), projectName, params,
					cbk);
		}
	}

}
