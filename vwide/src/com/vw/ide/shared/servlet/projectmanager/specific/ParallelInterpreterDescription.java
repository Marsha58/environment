package com.vw.ide.shared.servlet.projectmanager.specific;

/**
 * Parallel interpreter configurable properties
 * @author Oleg
 *
 */
@SuppressWarnings("serial")
public class ParallelInterpreterDescription extends InterpreterDescription {

	public ParallelInterpreterDescription() {
		super(InterpreterDescription.PARALLEL);
		setNodesPerRing(InterpreterDescription.DEF_NODES_PER_RING);
	}
	
	public ParallelInterpreterDescription(String name) {
		super(name);
		setNodesPerRing(InterpreterDescription.DEF_NODES_PER_RING);
	}

	public ParallelInterpreterDescription(String name, int nodesPerRing) {
		super(name);
		setNodesPerRing(nodesPerRing);
	}
}
