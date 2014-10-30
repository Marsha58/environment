package com.vw.ide.client.dialog.vwmlproj.tab;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.BlurEvent;
import com.sencha.gxt.widget.core.client.event.BlurEvent.BlurHandler;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.NumberField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.vw.ide.client.dialog.remotebrowser.RemoteDirectoryBrowserDialogExt;
import com.vw.ide.client.dialog.vwmlproj.VwmlProjectDialog;
import com.vw.ide.client.dialog.vwmlproj.VwmlProjectDialog.EditMode;
import com.vw.ide.shared.servlet.projectmanager.specific.InterpreterDescription;

public class VwmlGeneralSettingsTab extends VwmlProjTab {
	
	public static class SelectVwmlPathHandler implements RemoteDirectoryBrowserDialogExt.SelectPathHandler {

		private VwmlGeneralSettingsTab owner;
		
		public SelectVwmlPathHandler(VwmlGeneralSettingsTab owner) {
			this.owner = owner;
		}
		
		@Override
		public void onSelection(String selectedPath) {
			if (selectedPath != null) {
				owner.getOwner().getProjectDescription().setProjectPath(selectedPath);
				if (owner.getOwner().getProjectDescription().getJavaSrcPath() == null || owner.getOwner().getProjectDescription().getJavaSrcPath().length() == 0) {
					owner.getOwner().getProjectDescription().setJavaSrcPath(selectedPath + "/java_src");
					owner.getOwner().getVwmlJavaSettingsTab().setupTargetLangSourcePath();
				}
				owner.setupProjectPath();
			}
		}
	}
	
	public static class InterpreterSelectionHandler implements SelectionHandler<InterpreterDescription> {
		
		private VwmlGeneralSettingsTab owner;
		
		public InterpreterSelectionHandler(VwmlGeneralSettingsTab owner) {
			this.owner = owner;
		}
		
		@Override
		public void onSelection(SelectionEvent<InterpreterDescription> event) {
			owner.getOwner().getProjectDescription().setInterpreterDescription(event.getSelectedItem());
			owner.forceExpandParallelInterpreterParams(event.getSelectedItem());
		}
	}
	
	private VwmlProjectDialog owner;
	private static String s_remoteDirectoryBrowserCaption = "Select project's folder";

	public VwmlGeneralSettingsTab(VwmlProjectDialog owner) {
		this.owner = owner;
	}

	public void browseVwmlProjectPath() {
		RemoteDirectoryBrowserDialogExt d = new RemoteDirectoryBrowserDialogExt();
		d.setSelectPathHandler(new SelectVwmlPathHandler(this));
		d.setModal(true);
		d.setLoggedAsUser(owner.getLoggedAsUser());
		d.prepare();
		d.setSize("550", "400");
		d.showCenter(s_remoteDirectoryBrowserCaption, null);
	}

	public void initWidgets() {
		owner.getInterpretersAsListStore().add(new InterpreterDescription(InterpreterDescription.SEQUENTIAL));
		owner.getInterpretersAsListStore().add(new InterpreterDescription(InterpreterDescription.REACTIVE));
		owner.getInterpretersAsListStore().add(new InterpreterDescription(InterpreterDescription.PARALLEL));
		if (getEditMode() != EditMode.NEW) {
			InterpreterDescription d = owner.getInterpretersAsListStore().findModelWithKey(owner.getProjectDescription().getInterpreterDescription().getKey());
			if (d != null) {
				owner.getInterpretersAsListStore().remove(d);
				owner.getInterpretersAsListStore().add(owner.getProjectDescription().getInterpreterDescription());
			}
		}
		owner.getFieldSetInterpreterSettings().collapse();
		owner.getCbVWMLAvailableInterpreters().setEditable(false);
		owner.getCbVWMLAvailableInterpreters().setTriggerAction(TriggerAction.ALL);
		owner.getCbVWMLAvailableInterpreters().addSelectionHandler(new InterpreterSelectionHandler(this));
		owner.getTfVWMLProjectPath().addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				owner.getProjectDescription().setProjectPath(owner.getTfVWMLProjectPath().getText());
			}
		});
		owner.getTfVWMLAuthor().addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				owner.getProjectDescription().setAuthor(owner.getTfVWMLAuthor().getText());
			}
		});
		owner.getTfVWMLDescr().addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				owner.getProjectDescription().setDescr(owner.getTfVWMLDescr().getText());
			}
		});
		if (getEditMode() == VwmlProjectDialog.EditMode.EDIT_VWML_SETTINGS) {
			owner.getTfVWMLProjectPath().disable();
			owner.getTfVWMLProjectName().disable();
			owner.getTfVWMLMainModule().disable();
			owner.getTfVWMLDescr().disable();
			owner.getTfVWMLAuthor().disable();
		}
	}
	
	public void setup() {
		setupProjectName();
		setupProjectPath();
		setupMainModule();
		setupAuthor();
		setupDescription();
		setupInterpreter();
	}
	
	public boolean validate() {
		if (owner.getProjectDescription().getProjectName() == null || owner.getProjectDescription().getProjectName().length() == 0) {
			owner.scrollTo(getWidget());
			owner.getTfVWMLProjectName().focus();
			return false;
		}
		if (owner.getProjectDescription().getProjectPath() == null || owner.getProjectDescription().getProjectPath().length() == 0) {
			owner.scrollTo(getWidget());
			owner.getTfVWMLProjectPath().focus();
			return false;
		}
		if (owner.getProjectDescription().getAuthor() == null || owner.getProjectDescription().getAuthor().length() == 0) {
			owner.scrollTo(getWidget());
			owner.getTfVWMLAuthor().focus();
			return false;
		}
		if (owner.getProjectDescription().getDescr() == null || owner.getProjectDescription().getDescr().length() == 0) {
			owner.scrollTo(getWidget());
			owner.getTfVWMLDescr().focus();
			return false;
		}
		return true;
	}

	protected VwmlProjectDialog getOwner() {
		return owner;
	}

	protected void forceExpandParallelInterpreterParams(InterpreterDescription description) {
		if (description.getName().equals(InterpreterDescription.PARALLEL)) {
			owner.getFieldSetInterpreterSettings().enable();
			owner.getFieldInterpreterSettingsArea().enable();
			owner.getFieldSetInterpreterSettings().expand();
			activateParallelInterpreterEditableProperties(owner.getFieldInterpreterSettingsArea(),
					owner.getInterpretersAsListStore().get(InterpreterDescription.PARALLEL_INTERPRETER_ID));
		}
		else {
			deactivateParallelInterpreterEditableProperties(owner.getFieldInterpreterSettingsArea());
			owner.getFieldSetInterpreterSettings().collapse();
			owner.getFieldSetInterpreterSettings().disable();
			owner.getFieldInterpreterSettingsArea().disable();	
		}
	}
	
	private void setupProjectName() {
		owner.getTfVWMLProjectName().setText(owner.getProjectDescription().getProjectName());
		owner.getTfVWMLProjectName().addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				owner.getProjectDescription().setProjectName(owner.getTfVWMLProjectName().getText());
				owner.getProjectDescription().setMainModuleName(owner.getProjectDescription().getProjectName());
				setupMainModule();
			}
		});	
	}

	private void setupProjectPath() {
		owner.getTfVWMLProjectPath().setText(owner.getProjectDescription().getProjectPath());		
	}
	
	private void setupMainModule() {
		owner.getTfVWMLMainModule().setText(owner.getProjectDescription().getProjectName());
	}

	private void setupAuthor() {
		owner.getTfVWMLAuthor().setText(owner.getProjectDescription().getAuthor());
	}
	
	private void setupDescription() {
		owner.getTfVWMLDescr().setText(owner.getProjectDescription().getDescr());
	}
	
	private void setupInterpreter() {
		owner.getCbVWMLAvailableInterpreters().setValue(owner.getProjectDescription().getInterpreterDescription(), true);
		forceExpandParallelInterpreterParams(owner.getProjectDescription().getInterpreterDescription());
	}
	
	private void activateParallelInterpreterEditableProperties(VerticalLayoutContainer fieldInterpreterSettingsArea, InterpreterDescription description) {
		NumberField<Integer> tf = new NumberField<Integer>(new NumberPropertyEditor.IntegerPropertyEditor());
		tf.addBlurHandler(new BlurHandler() {
			@SuppressWarnings("unchecked")
			@Override
			public void onBlur(BlurEvent event) {
				InterpreterDescription pid = owner.getProjectDescription().getInterpreterDescription();
				pid.setNodesPerRing(((NumberField<Integer>)event.getSource()).getValue());
			}
		});
		tf.setAllowBlank(false);
		tf.setText(String.valueOf(description.getNodesPerRing()));
		FieldLabel fl = new FieldLabel(tf, "Lifeterms per ring");
		fieldInterpreterSettingsArea.add(fl);
	}

	private void deactivateParallelInterpreterEditableProperties(VerticalLayoutContainer fieldInterpreterSettingsArea) {
		fieldInterpreterSettingsArea.clear();
	}
}
