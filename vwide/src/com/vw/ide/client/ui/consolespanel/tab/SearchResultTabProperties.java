package com.vw.ide.client.ui.consolespanel.tab;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.vw.ide.shared.servlet.processor.dto.sandr.SearchAndReplaceResult;

/**
 * See http://www.sencha.com/examples/#ExamplePlace:basicsimplegrid(uibinder)
 * @author Oleg
 *
 */
public interface SearchResultTabProperties extends PropertyAccess<SearchAndReplaceResult> {
	@Path("key")
	ModelKeyProvider<SearchAndReplaceResult> key();
	
	@Path("userName")
	LabelProvider<SearchAndReplaceResult> nameLabel();
	
	ValueProvider<SearchAndReplaceResult, String> projectName();
	
	ValueProvider<SearchAndReplaceResult, String> file();
	
	ValueProvider<SearchAndReplaceResult, String> search();
	
	ValueProvider<SearchAndReplaceResult, String> replace();
	
	ValueProvider<SearchAndReplaceResult, String> place();

}
