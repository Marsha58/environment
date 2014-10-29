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
import com.vw.ide.shared.servlet.projectmanager.ReactiveInterpreterDescription;
import com.vw.ide.shared.servlet.projectmanager.SequentialInterpreterDescription;
import com.vw.ide.shared.servlet.projectmanager.specific.InterpreterDescription;
import com.vw.ide.shared.servlet.projectmanager.specific.ParallelInterpreterDescription;

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
			if (event.getSelectedItem().equals(owner.getOwner().getInterpretersAsListStore().get(InterpreterDescription.PARALLEL_INTERPRETER_ID))) {
				owner.getOwner().getFieldSetInterpreterSettings().enable();
				owner.getOwner().getFieldInterpreterSettingsArea().enable();
				owner.getOwner().getFieldSetInterpreterSettings().expand();
				owner.activateParallelInterpreterEditableProperties(owner.getOwner().getFieldInterpreterSettingsArea(),
						(ParallelInterpreterDescription)owner.getOwner().getInterpretersAsListStore().get(InterpreterDescription.PARALLEL_INTERPRETER_ID));
			}
			else {
				owner.deactivateParallelInterpreterEditableProperties(owner.getOwner().getFieldInterpreterSettingsArea());
				owner.getOwner().getFieldSetInterpreterSettings().collapse();
				owner.getOwner().getFieldSetInterpreterSettings().disable();
				owner.getOwner().getFieldInterpreterSettingsArea().disable();	
			}
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
		owner.getInterpretersAsListStore().add(new SequentialInterpreterDescription(InterpreterDescription.SEQUENTIAL));
		owner.getInterpretersAsListStore().add(new ReactiveInterpreterDescription(InterpreterDescription.REACTIVE));
		owner.getInterpretersAsListStore().add(new ParallelInterpreterDescription(InterpreterDescription.PARALLEL));
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
		owner.getCbVWMLAvailableInterpreters().setValue(owner.getProjectDescription().getInterpreterDescription());
	}
	
	private void activateParallelInterpreterEditableProperties(VerticalLayoutContainer fieldInterpreterSettingsArea, ParallelInterpreterDescription description) {
		NumberField<Integer> tf = new NumberField<Integer>(new NumberPropertyEditor.IntegerPropertyEditor());
		tf.addBlurHandler(new BlurHandler() {
			@SuppressWarnings("unchecked")
			@Override
			public void onBlur(BlurEvent event) {
				ParallelInterpreterDescription pid = (ParallelInterpreterDescription)owner.getProjectDescription().getInterpreterDescription();
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
