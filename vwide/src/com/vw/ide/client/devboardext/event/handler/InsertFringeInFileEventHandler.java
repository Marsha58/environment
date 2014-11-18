package com.vw.ide.client.devboardext.event.handler;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.event.handler.fringes.InsertFringeInFileHandler;
import com.vw.ide.client.event.uiflow.fringes.InsertFringeInFileEvent;
import com.vw.ide.client.fringemanagment.parser.FringeMetaParser;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.ui.editorpanel.FileSheet;
import com.vw.ide.shared.servlet.fringes.model.Fringe;

public class InsertFringeInFileEventHandler extends Presenter.PresenterEventHandler implements InsertFringeInFileHandler {
	

	@Override
	public void handler(Presenter presenter, GwtEvent<?> event) {
		process((DevelopmentBoardPresenter)presenter, (InsertFringeInFileEvent)event);
	}

	@Override
	public void onInsertFringeInFile(InsertFringeInFileEvent event) {
		if (getPresenter() != null) {
			getPresenter().delegate(event);
		}
		
	}	
	
	protected void process(DevelopmentBoardPresenter presenter, InsertFringeInFileEvent event) {
		Fringe fringe = event.getFringe();
		FileSheet currentTab = (FileSheet)presenter.getEditorPanel().getTabPanel().getActiveWidget();
		FringeMetaParser fringeMetaParser = new FringeMetaParser(currentTab.getAceEditor().getText());
		fringeMetaParser.insertFringe(fringe); 
		currentTab.getAceEditor().setText(fringeMetaParser.getFullText());
	}



}


