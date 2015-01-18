package com.vw.ide.shared.servlet.tracer;

import com.vw.ide.shared.servlet.processor.dto.sandr.SearchAndReplaceResult;

@SuppressWarnings("serial")
public class TracerSearchAndReplaceMessage extends TracerData<SearchAndReplaceResult> {

	public TracerSearchAndReplaceMessage() {
		
	}
	
	public TracerSearchAndReplaceMessage(SearchAndReplaceResult r) {
		super(r);
	}
}
