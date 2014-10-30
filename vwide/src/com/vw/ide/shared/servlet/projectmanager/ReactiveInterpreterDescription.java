package com.vw.ide.shared.servlet.projectmanager;

import com.vw.ide.shared.servlet.projectmanager.specific.InterpreterDescription;

/**
 * Reactive interpreter configurable properties
 * @author Oleg
 *
 */
@SuppressWarnings("serial")
public class ReactiveInterpreterDescription extends InterpreterDescription {

	public ReactiveInterpreterDescription() {
		super(InterpreterDescription.REACTIVE);
	}
	
	public ReactiveInterpreterDescription(String name) {
		super(name);
	}
}
