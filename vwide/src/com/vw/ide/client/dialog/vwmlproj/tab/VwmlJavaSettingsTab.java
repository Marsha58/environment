package com.vw.ide.client.dialog.vwmlproj.tab;

import com.sencha.gxt.widget.core.client.event.BlurEvent;
import com.sencha.gxt.widget.core.client.event.BlurEvent.BlurHandler;
import com.vw.ide.client.dialog.remotebrowser.RemoteDirectoryBrowserDialogExt;
import com.vw.ide.client.dialog.vwmlproj.VwmlProjectDialog;

public class VwmlJavaSettingsTab extends VwmlProjTab {

	public static class SelectJavaPathHandler implements RemoteDirectoryBrowserDialogExt.SelectPathHandler {

		private VwmlJavaSettingsTab owner;
		
		public SelectJavaPathHandler(VwmlJavaSettingsTab owner) {
			this.owner = owner;
		}
		
		@Override
		public void onSelection(String selectedPath) {
			if (selectedPath != null) {
				owner.getOwner().getProjectDescription().setJavaSrcPath(selectedPath);
				owner.setupTargetLangSourcePath();
			}
		}
	}
	
	private VwmlProjectDialog owner;
	private static String s_remoteDirectoryBrowserCaption = "Select project's folder";

	public VwmlJavaSettingsTab(VwmlProjectDialog owner) {
		this.owner = owner;
	}

	public void initWidgets() {
		owner.getTfJavaSourcePath().addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				owner.getProjectDescription().setJavaSrcPath(owner.getTfJavaSourcePath().getText());
			}
		});
		owner.getTfJavaPackageName().addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				owner.getProjectDescription().setPackageName(owner.getTfJavaPackageName().getText());
			}
		});
	}
	
	@Override
	public void setup() {
		if (getEditMode() == VwmlProjectDialog.EditMode.EDIT_VWML_SETTINGS) {
			owner.getTfJavaPackageName().disable();
			owner.getTfJavaSourcePath().disable();
		}
		setupTargetLangSourcePath();
		setupJavaPackage();
	}

	public boolean validate() {
		if (owner.getProjectDescription().getJavaSrcPath() == null || owner.getProjectDescription().getJavaSrcPath().length() == 0) {
			owner.scrollTo(getWidget());
			owner.getTfJavaSourcePath().focus();
			return false;
		}
		if (owner.getProjectDescription().getPackageName() == null || owner.getProjectDescription().getPackageName().length() == 0) {
			owner.scrollTo(getWidget());
			owner.getTfJavaPackageName().focus();
			return false;
		}
		return true;
	}
	
	public void browseJavaSourceGeneratedPath() {
		RemoteDirectoryBrowserDialogExt d = new RemoteDirectoryBrowserDialogExt();
		d.setSelectPathHandler(new SelectJavaPathHandler(this));
		d.setModal(true);
		d.setLoggedAsUser(owner.getLoggedAsUser());
		d.prepare();
		d.setSize("550", "400");
		d.showCenter(s_remoteDirectoryBrowserCaption, null);
	}
	
	protected VwmlProjectDialog getOwner() {
		return owner;
	}

	public void setupTargetLangSourcePath() {
		owner.getTfJavaSourcePath().setText(owner.getProjectDescription().getJavaSrcPath());
	}
	
	public void setupJavaPackage() {
		owner.getTfJavaPackageName().setText(owner.getProjectDescription().getPackageName());
	}
}
