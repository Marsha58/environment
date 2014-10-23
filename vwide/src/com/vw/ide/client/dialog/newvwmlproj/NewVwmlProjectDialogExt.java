package com.vw.ide.client.dialog.newvwmlproj;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.util.ToggleGroup;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.BlurEvent;
import com.sencha.gxt.widget.core.client.event.BlurEvent.BlurHandler;
import com.sencha.gxt.widget.core.client.event.FocusEvent.FocusHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FieldSet;
import com.sencha.gxt.widget.core.client.form.Radio;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.vw.ide.client.FlowController;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.dialog.VwmlDialogExt;
import com.vw.ide.client.dialog.remotebrowser.RemoteDirectoryBrowserDialogExt;
import com.vw.ide.client.event.uiflow.GetDirContentEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.service.remote.ResultCallback;
import com.vw.ide.client.service.remote.projectmanager.ProjectManagerServiceBroker;
import com.vw.ide.shared.servlet.projectmanager.CompilerSwitchesDescription;
import com.vw.ide.shared.servlet.projectmanager.InterpreterDescription;
import com.vw.ide.shared.servlet.projectmanager.ParallelInterpreterDescription;
import com.vw.ide.shared.servlet.projectmanager.ProjectDescription;
import com.vw.ide.shared.servlet.projectmanager.ReactiveInterpreterDescription;
import com.vw.ide.shared.servlet.projectmanager.RequestProjectCreationResult;
import com.vw.ide.shared.servlet.projectmanager.SequentialInterpreterDescription;

/**
 * New VWML project dialog
 * 
 * @author Oleg
 * 
 */
public class NewVwmlProjectDialogExt extends VwmlDialogExt {

	private static String s_remoteDirectoryBrowserCaption = "Select project's folder";
	private static NewVwmlProjectDialogExtUiBinder uiBinder = GWT.create(NewVwmlProjectDialogExtUiBinder.class);
	
	private Presenter presenter = null;
	private String path4project = "";
	private ProjectDescription projectDescription = null;
	
	// @UiField FlowPanel dialogMainPanel;
	@UiField
	TextField tfVWMLProjectName;
	@UiField
	TextField tfVWMLProjectPath;
	@UiField
	TextButton browseVwmlProjPath;
	@UiField
	TextButton browseJavaProjPath;
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
	@UiField(provided = true)
    LabelProvider<InterpreterDescription> interpreterLabelProvider = interpreterProps.name();	
	@UiField(provided = true)
    ListStore<InterpreterDescription> interpretersAsListStore = new ListStore<InterpreterDescription>(interpreterProps.key());	
	@UiField
	ComboBox<InterpreterDescription> cbVWMLAvailableInterpreters;
	@UiField
	FieldSet fieldSetInterpreterSettings;
	@UiField 
	VerticalLayoutContainer fieldInterpreterSettingsArea;
	@UiField(provided = true)
    LabelProvider<CompilerSwitchesDescription.Mode> compilationModeLabelProvider = compilerModeProps.mode();
	@UiField(provided = true)
    ListStore<CompilerSwitchesDescription.Mode> compilationModesAsListStore = new ListStore<CompilerSwitchesDescription.Mode>(compilerModeProps.key());
	@UiField
	ComboBox<CompilerSwitchesDescription.Mode> cbVWMLAvailableModes;
	@UiField
	CheckBox bnDebugInfoTrigger;
	@UiField
	FieldSet fieldSetCompileModeSettings;
	@UiField 
	VerticalLayoutContainer fieldSetCompileModeSettingsArea;
	
	private static final int SEQUENTIAL_INTERPRETER_ID = 0x0;
	private static final int REACTIVE_INTERPRETER_ID   = 0x1;
	private static final int PARALLEL_INTERPRETER_ID   = 0x2;

	private static InterpeterProperties interpreterProps = GWT.create(InterpeterProperties.class);
	private static CompilerModeProperties compilerModeProps = GWT.create(CompilerModeProperties.class);
	
	interface NewVwmlProjectDialogExtUiBinder extends UiBinder<Widget, NewVwmlProjectDialogExt> {
	}

	interface InterpeterProperties extends PropertyAccess<InterpreterDescription> {
	    ModelKeyProvider<InterpreterDescription> key();
	    LabelProvider<InterpreterDescription> name();
	}

	interface CompilerModeProperties extends PropertyAccess<CompilerSwitchesDescription.Mode> {
	    ModelKeyProvider<CompilerSwitchesDescription.Mode> key();
	    LabelProvider<CompilerSwitchesDescription.Mode> mode();
	}
	
	public NewVwmlProjectDialogExt(ProjectDescription projectDescription, Integer hashId) {
		this.projectDescription = (projectDescription == null) ? new ProjectDescription() : projectDescription;
		super.setWidget(uiBinder.createAndBindUi(this));
		initializationWidgets();
		setupProjectName();
		setupMainModule();
		setupAuthor();
		setupDescription();
		setupTargetLangSourcePath();
		fieldSetInterpreterSettings.collapse();
		cbVWMLAvailableInterpreters.setEditable(false);
		cbVWMLAvailableInterpreters.setTriggerAction(TriggerAction.ALL);
		cbVWMLAvailableInterpreters.setValue(interpretersAsListStore.get(REACTIVE_INTERPRETER_ID));
		cbVWMLAvailableInterpreters.addSelectionHandler(new SelectionHandler<InterpreterDescription>() {
			@Override
			public void onSelection(SelectionEvent<InterpreterDescription> event) {
				if (event.getSelectedItem().equals(interpretersAsListStore.get(PARALLEL_INTERPRETER_ID))) {
					fieldSetInterpreterSettings.enable();
					fieldInterpreterSettingsArea.enable();
					fieldSetInterpreterSettings.expand();
					activateParallelInterpreterEditableProperties(fieldInterpreterSettingsArea, (ParallelInterpreterDescription)interpretersAsListStore.get(PARALLEL_INTERPRETER_ID));
				}
				else {
					deactivateParallelInterpreterEditableProperties(fieldInterpreterSettingsArea);
					fieldSetInterpreterSettings.collapse();
					fieldSetInterpreterSettings.disable();
					fieldInterpreterSettingsArea.disable();	
				}
			}
		});
		bnDebugInfoTrigger.setToolTip("Debug information increases size of generated code");
		cbVWMLAvailableModes.setEditable(false);
		cbVWMLAvailableModes.setTriggerAction(TriggerAction.ALL);
		cbVWMLAvailableModes.setValue(CompilerSwitchesDescription.Mode.SOURCE);
		cbVWMLAvailableModes.addSelectionHandler(new SelectionHandler<CompilerSwitchesDescription.Mode>() {
			@Override
			public void onSelection(SelectionEvent<CompilerSwitchesDescription.Mode> event) {
				if (event.getSelectedItem() == CompilerSwitchesDescription.Mode.TEST) {
					fieldSetCompileModeSettings.enable();
					fieldSetCompileModeSettingsArea.enable();
					fieldSetCompileModeSettings.expand();
					activateTestModeEditableProperties(fieldSetCompileModeSettingsArea);
				}
				else {
					deactivateTestModeEditableProperties(fieldSetCompileModeSettingsArea);
					fieldSetCompileModeSettings.collapse();
					fieldSetCompileModeSettings.disable();
					fieldSetCompileModeSettingsArea.disable();
				}
			}
		}
		);
		fieldSetCompileModeSettings.collapse();
		fieldSetCompileModeSettings.disable();
	}

	public class ProjectCreationResult extends ResultCallback<RequestProjectCreationResult> {

		public ProjectCreationResult() {
		}

		@Override
		public void handle(RequestProjectCreationResult result) {
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
		String firstLetter = input.substring(0, 1).toUpperCase();
		String restLetters = input.substring(1, input.length());
		return firstLetter + restLetters;
	}

	public String makePackageName(String projectName) {
		StringBuffer sPackageName = new StringBuffer();
		String[] arrProjectName = projectName.split(" ");
		for (String curWord : arrProjectName) {
			sPackageName.append(capitalizeFirstLetter(curWord));
		}

		return sPackageName.toString();
	}

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
			ProjectDescription description = new ProjectDescription(
													FlowController.getLoggedAsUser(),
													tfVWMLProjectName.getText(),
													tfVWMLProjectPath.getText(),
													tfJavaPackageName.getText(),
													tfJavaSourcePath.getText(),
													tfVWMLAuthor.getText(),
													tfVWMLDescr.getText());
			ProjectManagerServiceBroker.requestForProjectCreation(description, new ProjectCreationResult());
			hide();
		}
	}
	
	public void setPath4project(String path4project) {
		this.path4project = path4project;
	}
	
	private void activateParallelInterpreterEditableProperties(VerticalLayoutContainer fieldInterpreterSettingsArea, ParallelInterpreterDescription description) {
		TextField tf = new TextField();
		tf.setAllowBlank(false);
		tf.setText(String.valueOf(description.getNodesPerRing()));
		FieldLabel fl = new FieldLabel(tf, "Lifeterms per ring");
		fieldInterpreterSettingsArea.add(fl);
	}

	private void deactivateParallelInterpreterEditableProperties(VerticalLayoutContainer fieldInterpreterSettingsArea) {
		fieldInterpreterSettingsArea.clear();
	}
	
	private void activateTestModeEditableProperties(VerticalLayoutContainer fieldSetCompileModeSettingsArea) {
		HorizontalPanel hp = new HorizontalPanel();
		Radio testStatic = new Radio();
		Radio testAll = new Radio();
		testStatic.setBoxLabel("Static");
		testAll.setBoxLabel("All");
		testStatic.setValue(true);
		hp.add(testStatic);
		hp.add(testAll);
	    ToggleGroup toggle = new ToggleGroup();
	    toggle.add(testStatic);
	    toggle.add(testAll);		
		FieldLabel fl = new FieldLabel(hp, "Select type of tests");
		fl.setToolTip("'Static' test checks consistency of created model by checking relations among contexts. 'All' test adds ability to run model and debug it");
		fieldSetCompileModeSettingsArea.add(fl);
	}

	private void deactivateTestModeEditableProperties(VerticalLayoutContainer fieldSetCompileModeSettingsArea) {
		fieldSetCompileModeSettingsArea.clear();
	}

	private void initializationWidgets() {
		interpretersAsListStore.add(new SequentialInterpreterDescription("sequential"));
		interpretersAsListStore.add(new ReactiveInterpreterDescription("reactive"));
		interpretersAsListStore.add(new ParallelInterpreterDescription("parallel"));
		compilationModesAsListStore.add(CompilerSwitchesDescription.Mode.SOURCE);
		compilationModesAsListStore.add(CompilerSwitchesDescription.Mode.PROJECT);
		compilationModesAsListStore.add(CompilerSwitchesDescription.Mode.COMPILE);
		compilationModesAsListStore.add(CompilerSwitchesDescription.Mode.TEST);
		compilationModesAsListStore.add(CompilerSwitchesDescription.Mode.MAIN);
	}
	
	private void setupProjectName() {
		tfVWMLProjectName.setText(projectDescription.getProjectName());
		tfVWMLProjectName.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				projectDescription.setProjectName(tfVWMLProjectName.getText());
				projectDescription.setMainModuleName(projectDescription.getProjectName());
				tfVWMLMainModule.setText(projectDescription.getProjectName());
			}
		});	
	}

	private void setupMainModule() {
		tfVWMLMainModule.setText(projectDescription.getProjectName());
	}

	private void setupAuthor() {
		tfVWMLMainModule.setText(projectDescription.getAuthor());
	}
	
	private void setupDescription() {
		tfVWMLMainModule.setText(projectDescription.getDescr());
	}
	
	private void setupTargetLangSourcePath() {
		tfJavaSourcePath.setText("java_src");
	}
}
