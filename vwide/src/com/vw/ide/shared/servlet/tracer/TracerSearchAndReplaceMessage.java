package com.vw.ide.shared.servlet.processor.command.sandr;

import com.vw.ide.shared.servlet.tracer.TracerData;

@SuppressWarnings("serial")
public class SearchAndReplaceMessage extends TracerData<SearchAndReplaceResult> {

	public SearchAndReplaceMessage() {
		
	}
	
	public SearchAndReplaceMessage(SearchAndReplaceResult r) {
		super(r);
	}
}
