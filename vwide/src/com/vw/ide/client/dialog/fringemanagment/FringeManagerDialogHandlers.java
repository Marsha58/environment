package com.vw.ide.client.dialog.fringemanagment;

import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent.DialogHideHandler;
import com.vw.ide.client.dialog.fringeload.FringeLoadDialog;

public class FringeManagerDialogHandlers {

	public static class LoadFringeDialogHideHandler implements DialogHideHandler {
		private FringeLoadDialog box;
		private FringeManagerPresenter owner;

		public LoadFringeDialogHideHandler(FringeLoadDialog box, FringeManagerPresenter owner) {
			this.owner = owner;
			this.box = box;
		}

		@Override
		public void onDialogHide(DialogHideEvent event) {
//			if (box.getLoadedFiles() > 0) {
//				FringeServiceBroker.requestForFileCreating(FlowController.getLoggedAsUser(), box.getParentPath(), box.getFileName(0),
//						box.getProjectId(), 0L, box.getContent(0), new AnyDirOperationResultCallback(owner));
//			}
		}
	}	
	
}
