package com.vw.ide.client.devboardext.event.handler;

import com.google.gwt.event.shared.GwtEvent;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.dialog.searchandreplace.SearchAndReplaceDialog;
import com.vw.ide.client.event.handler.SearchTextHandler;
import com.vw.ide.client.event.uiflow.SearchTextEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.service.remote.ResultCallback;
import com.vw.ide.client.service.remote.processor.CommandProcessorServiceBroker;
import com.vw.ide.shared.servlet.processor.CommandProcessorResult;
import com.vw.ide.shared.servlet.processor.dto.sandr.SearchAndReplaceBundle;

public class SearchTextEventHandler extends Presenter.PresenterEventHandler implements SearchTextHandler {

	protected static class StartSearch extends SearchAndReplaceDialog.SearchHandler {

		private DevelopmentBoardPresenter presenter;
		
		public StartSearch(DevelopmentBoardPresenter presenter) {
			this.presenter = presenter;
		}
		
		@Override
		public void process(SearchAndReplaceBundle bundle) {
			// perform request
			if (bundle.getSearch() == null || bundle.getSearch().length() == 0) {
				AlertMessageBox b = new AlertMessageBox("Warning", "At least 'Replace' field must be filled");
				b.show();
			}
			else {
				CommandProcessorServiceBroker.performSearchAndReplace(presenter.getLoggedAsUser(),
																	bundle,
																	new SearchAndReplaceCallback());
			}
		}
	}
	
	protected static class SearchAndReplaceCallback extends ResultCallback<CommandProcessorResult> {

		@Override
		public void handle(CommandProcessorResult result) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	@Override
	public void onSearchText(SearchTextEvent event) {
		if (getPresenter() != null) {
			getPresenter().delegate(event);
		}
	}

	@Override
	public void handler(Presenter presenter, GwtEvent<?> event) {
		process((DevelopmentBoardPresenter)presenter, (SearchTextEvent)event);
	}

	private void process(DevelopmentBoardPresenter presenter, SearchTextEvent event) {
		SearchAndReplaceDialog d = new SearchAndReplaceDialog(presenter.getView(), new StartSearch(presenter));
		if (d.init()) {
			d.setModal(true);
			d.show();
		}
		else {
			AlertMessageBox b = new AlertMessageBox("Warning", "User '" + presenter.getLoggedAsUser() + "' doesn't have any active project");
			b.show();
		}
	}	
}
