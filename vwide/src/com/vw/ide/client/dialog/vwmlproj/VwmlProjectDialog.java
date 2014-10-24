package com.vw.ide.client.dialog.vwmlproj;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.FieldSet;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.dialog.VwmlDialogExt;
import com.vw.ide.client.dialog.vwmlproj.tab.VwmlCompilerSettingsTab;
import com.vw.ide.client.dialog.vwmlproj.tab.VwmlGeneralSettingsTab;
import com.vw.ide.client.dialog.vwmlproj.tab.VwmlJavaSettingsTab;
import com.vw.ide.client.dialog.vwmlproj.tab.VwmlProjTab;
import com.vw.ide.client.event.uiflow.GetDirContentEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.service.remote.ResultCallback;
import com.vw.ide.client.service.remote.projectmanager.ProjectManagerServiceBroker;
import com.vw.ide.shared.servlet.projectmanager.CompilerSwitchesDescription;
import com.vw.ide.shared.servlet.projectmanager.InterpreterDescription;
import com.vw.ide.shared.servlet.projectmanager.ProjectDescription;
import com.vw.ide.shared.servlet.projectmanager.RequestProjectCreationResult;

/**
 * New VWML project dialog
 * 
 * @author Oleg
 * 
 */
public class VwmlProjectDialog extends VwmlDialogExt {

	interface NewVwmlProjectDialogExtUiBinder extends UiBinder<Widget, VwmlProjectDialog> {
	}

	interface InterpeterProperties extends PropertyAccess<InterpreterDescription> {
	    ModelKeyProvider<InterpreterDescription> key();
	    LabelProvider<InterpreterDescription> name();
	}

	interface CompilerModeProperties extends PropertyAccess<CompilerSwitchesDescription.Mode> {
	    ModelKeyProvider<CompilerSwitchesDescription.Mode> key();
	    LabelProvider<CompilerSwitchesDescription.Mode> mode();
	}
	
	@UiField
	TabPanel tabContainer;
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
	@UiField
	TextField tfVWMLPreprocessor;
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
	
	private Presenter presenter = null;
	private ProjectDescription projectDescription = null;
	private VwmlGeneralSettingsTab 	vwmlGeneralSettingsTab 	= new VwmlGeneralSettingsTab(this);
	private VwmlCompilerSettingsTab vwmlCompilerSettingsTab = new VwmlCompilerSettingsTab(this);
	private VwmlJavaSettingsTab 	vwmlJavaSettingsTab 	= new VwmlJavaSettingsTab(this);
	private VwmlProjTab[] tabs 								= {	vwmlGeneralSettingsTab,
																vwmlCompilerSettingsTab,
																vwmlJavaSettingsTab
																};
	
	private static NewVwmlProjectDialogExtUiBinder uiBinder = GWT.create(NewVwmlProjectDialogExtUiBinder.class);
	private static InterpeterProperties interpreterProps = GWT.create(InterpeterProperties.class);
	private static CompilerModeProperties compilerModeProps = GWT.create(CompilerModeProperties.class);
	
	public VwmlProjectDialog(ProjectDescription projectDescription, Integer hashId) {
		this.projectDescription = (projectDescription == null) ? new ProjectDescription() : projectDescription;
		super.setWidget(uiBinder.createAndBindUi(this));
		initializationWidgets();
		for(int i = 0; i < tabContainer.getWidgetCount(); i++) {
			tabs[i].setWidget(tabContainer.getWidget(i));
		}
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
	
	public VwmlProjectDialog(String firstName) {
		super.setWidget(uiBinder.createAndBindUi(this));
	}

	@UiHandler("browseVwmlProjPath")
	protected void onBrowseVwmlProjPathClick(SelectEvent event) {
		vwmlGeneralSettingsTab.browseVwmlProjectPath();
	}

	@UiHandler("browseJavaProjPath")
	protected void onBrowseJavaProjPathClick(SelectEvent event) {
		vwmlJavaSettingsTab.browseJavaSourceGeneratedPath();
	}
	
	protected void onButtonPressed(TextButton textButton) {
		if (textButton == getButton(PredefinedButton.OK)) {
			for(VwmlProjTab tab : tabs) {
				if (!tab.validate()) {
					return;
				}
			}
			super.onButtonPressed(textButton);
			projectDescription.setUserName(getLoggedAsUser());
			System.out.println(projectDescription);
			ProjectManagerServiceBroker.requestForProjectCreation(projectDescription, new ProjectCreationResult());
			hide();
		}
		else {
			super.onButtonPressed(textButton);
		}
	}
	
	private void initializationWidgets() {
		for(VwmlProjTab tab : tabs) {
			tab.initWidgets();
		}
		for(VwmlProjTab tab : tabs) {
			tab.setup();
		}
	}

	public ProjectDescription getProjectDescription() {
		return projectDescription;
	}
	
	public static NewVwmlProjectDialogExtUiBinder getUiBinder() {
		return uiBinder;
	}

	public TextField getTfVWMLProjectName() {
		return tfVWMLProjectName;
	}

	public TextField getTfVWMLProjectPath() {
		return tfVWMLProjectPath;
	}

	public TextButton getBrowseVwmlProjPath() {
		return browseVwmlProjPath;
	}

	public TextButton getBrowseJavaProjPath() {
		return browseJavaProjPath;
	}

	public TextField getTfVWMLMainModule() {
		return tfVWMLMainModule;
	}

	public TextField getTfVWMLAuthor() {
		return tfVWMLAuthor;
	}

	public TextArea getTfVWMLDescr() {
		return tfVWMLDescr;
	}

	public TextField getTfJavaPackageName() {
		return tfJavaPackageName;
	}

	public TextField getTfJavaSourcePath() {
		return tfJavaSourcePath;
	}

	public TextField getTfVWMLPreprocessor() {
		return tfVWMLPreprocessor;
	}

	public LabelProvider<InterpreterDescription> getInterpreterLabelProvider() {
		return interpreterLabelProvider;
	}

	public ListStore<InterpreterDescription> getInterpretersAsListStore() {
		return interpretersAsListStore;
	}

	public ComboBox<InterpreterDescription> getCbVWMLAvailableInterpreters() {
		return cbVWMLAvailableInterpreters;
	}

	public FieldSet getFieldSetInterpreterSettings() {
		return fieldSetInterpreterSettings;
	}

	public VerticalLayoutContainer getFieldInterpreterSettingsArea() {
		return fieldInterpreterSettingsArea;
	}

	public LabelProvider<CompilerSwitchesDescription.Mode> getCompilationModeLabelProvider() {
		return compilationModeLabelProvider;
	}

	public ListStore<CompilerSwitchesDescription.Mode> getCompilationModesAsListStore() {
		return compilationModesAsListStore;
	}

	public ComboBox<CompilerSwitchesDescription.Mode> getCbVWMLAvailableModes() {
		return cbVWMLAvailableModes;
	}

	public CheckBox getBnDebugInfoTrigger() {
		return bnDebugInfoTrigger;
	}

	public FieldSet getFieldSetCompileModeSettings() {
		return fieldSetCompileModeSettings;
	}

	public VerticalLayoutContainer getFieldSetCompileModeSettingsArea() {
		return fieldSetCompileModeSettingsArea;
	}
	
	public void scrollTo(Widget widget) {
		tabContainer.setActiveWidget(widget);
	}
}
