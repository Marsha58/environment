package com.vw.ide.client.fringemanagment;

import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent.DialogHideHandler;
import com.sencha.gxt.widget.core.client.grid.Grid.GridCell;
import com.vw.ide.client.fringemanagment.fringeditors.FringeEditDialog;
import com.vw.ide.client.fringemanagment.fringeload.FringeLoadDialog;
import com.vw.ide.client.event.uiflow.fringes.AddFringeEvent;
import com.vw.ide.client.event.uiflow.fringes.UpdateFringeEvent;
import com.vw.ide.shared.CrudTypes;
import com.vw.ide.shared.servlet.fringes.model.Fringe;

public class FringeManagerDialogHandlers {

	public static class LoadFringeDialogHideHandler implements DialogHideHandler {
		private FringeLoadDialog box;
		private FringeManagerPresenter presenter;

		public LoadFringeDialogHideHandler(FringeLoadDialog box, FringeManagerPresenter presenter) {
			this.presenter = presenter;
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

	
	public static class FringeEditDialogHideHandler implements DialogHideHandler {
		private FringeEditDialog box;
		private FringeManagerPresenter presenter;

		public FringeEditDialogHideHandler(FringeEditDialog box, FringeManagerPresenter presenter) {
			this.presenter = presenter;
			this.box = box;
		}

		@Override
		public void onDialogHide(DialogHideEvent event) {
			String btn = box.getSelectedHideButton();
			if (btn.equalsIgnoreCase("OK")) {
				if (box.getEditingType() == CrudTypes.ADD) {
					presenter.getView().updateListStoreFringes(box.getFringe(),box.getEditingType() );
					presenter.fireEvent(new AddFringeEvent(box.getFringe()));
				} else {
					presenter.fireEvent(new UpdateFringeEvent(box.getFringe()));
				}  
			}
			

			
			
		}
	}	
	
	

	
}
