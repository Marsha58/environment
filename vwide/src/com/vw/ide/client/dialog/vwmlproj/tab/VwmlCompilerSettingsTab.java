package com.vw.ide.client.dialog.vwmlproj.tab;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.util.ToggleGroup;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.BlurEvent;
import com.sencha.gxt.widget.core.client.event.BlurEvent.BlurHandler;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.Radio;
import com.vw.ide.client.dialog.vwmlproj.VwmlProjectDialog;
import com.vw.ide.shared.servlet.projectmanager.CompilerSwitchesDescription;

public class VwmlCompilerSettingsTab extends VwmlProjTab {

	public static class CompileModeSelectionHandler implements SelectionHandler<CompilerSwitchesDescription.Mode> {
		
		private VwmlCompilerSettingsTab owner;
		
		public CompileModeSelectionHandler(VwmlCompilerSettingsTab owner) {
			this.owner = owner;
		}
		
		@Override
		public void onSelection(SelectionEvent<CompilerSwitchesDescription.Mode> event) {
			owner.getOwner().getProjectDescription().getCompilerSwitches().setCompilationMode(event.getSelectedItem());
			if (event.getSelectedItem() == CompilerSwitchesDescription.Mode.TEST) {
				owner.getOwner().getFieldSetCompileModeSettings().enable();
				owner.getOwner().getFieldSetCompileModeSettingsArea().enable();
				owner.getOwner().getFieldSetCompileModeSettings().expand();
				owner.activateTestModeEditableProperties(owner.getOwner().getFieldSetCompileModeSettingsArea());
			}
			else {
				owner.deactivateTestModeEditableProperties(owner.getOwner().getFieldSetCompileModeSettingsArea());
				owner.getOwner().getFieldSetCompileModeSettings().collapse();
				owner.getOwner().getFieldSetCompileModeSettings().disable();
				owner.getOwner().getFieldSetCompileModeSettingsArea().disable();
			}
		}
	}
	
	private VwmlProjectDialog owner;

	public VwmlCompilerSettingsTab(VwmlProjectDialog owner) {
		this.owner = owner;
	}
	
	protected VwmlProjectDialog getOwner() {
		return owner;
	}
	
	public void initWidgets() {
		owner.getCompilationModesAsListStore().add(CompilerSwitchesDescription.Mode.SOURCE);
		owner.getCompilationModesAsListStore().add(CompilerSwitchesDescription.Mode.PROJECT);
		owner.getCompilationModesAsListStore().add(CompilerSwitchesDescription.Mode.COMPILE);
		owner.getCompilationModesAsListStore().add(CompilerSwitchesDescription.Mode.TEST);
		owner.getCompilationModesAsListStore().add(CompilerSwitchesDescription.Mode.MAIN);
		owner.getBnDebugInfoTrigger().setToolTip("Debug information increases size of generated code");
		owner.getBnDebugInfoTrigger().addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				owner.getProjectDescription().getCompilerSwitches().setIncludeDebugInfo(owner.getBnDebugInfoTrigger().getValue());
			}
		});
		owner.getCbVWMLAvailableModes().setEditable(false);
		owner.getCbVWMLAvailableModes().setTriggerAction(TriggerAction.ALL);
		owner.getCbVWMLAvailableModes().setValue(CompilerSwitchesDescription.Mode.SOURCE);
		owner.getCbVWMLAvailableModes().addSelectionHandler(new CompileModeSelectionHandler(this));
		owner.getFieldSetCompileModeSettings().collapse();
		owner.getFieldSetCompileModeSettings().disable();
		owner.getTfVWMLPreprocessor().addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				owner.getProjectDescription().getCompilerSwitches().setPreprocessorDirectives(owner.getTfVWMLPreprocessor().getText());
			}
		});
	}
	
	@Override
	public void setup() {
		setupPreprocessorDirectives();
		setupDebugInfoTrigger();
		setupCompilationMode();
	}
	
	public boolean validate() {
		return true;
	}
	
	private void setupPreprocessorDirectives() {
		String directives = owner.getProjectDescription().getCompilerSwitches().getPreprocessorDirectives();
		owner.getTfVWMLPreprocessor().setText(directives);
	}
	
	private void setupDebugInfoTrigger() {
		owner.getBnDebugInfoTrigger().setValidateOnBlur(owner.getProjectDescription().getCompilerSwitches().getIncludeDebugInfo());		
	}
	
	private void setupCompilationMode() {
		owner.getCbVWMLAvailableModes().setValue(owner.getProjectDescription().getCompilerSwitches().getCompilationMode());
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
		toggle.addValueChangeHandler(new ValueChangeHandler<HasValue<Boolean>>() {
			@Override
			public void onValueChange(ValueChangeEvent<HasValue<Boolean>> event) {
				Radio btn = (Radio)((ToggleGroup)(event.getSource())).getValue();
				if (btn.getValue().booleanValue()) {
					owner.getProjectDescription().getCompilerSwitches().setTestSwitch(btn.getBoxLabel());
				}
			}
		});
	}

	private void deactivateTestModeEditableProperties(VerticalLayoutContainer fieldSetCompileModeSettingsArea) {
		fieldSetCompileModeSettingsArea.clear();
	}
}
