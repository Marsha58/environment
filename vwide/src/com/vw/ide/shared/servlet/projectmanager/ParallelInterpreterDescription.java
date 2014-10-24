package com.vw.ide.shared.servlet.projectmanager;

/**
 * Parallel interpreter configurable properties
 * @author Oleg
 *
 */
@SuppressWarnings("serial")
public class ParallelInterpreterDescription extends InterpreterDescription {

	private int nodesPerRing = DEF_NODES_PER_RING;
	
	private static final int DEF_NODES_PER_RING = 50;
	
	public ParallelInterpreterDescription(String name) {
		super(name);
	}

	public ParallelInterpreterDescription(String name, int nodesPerRing) {
		super(name);
		setNodesPerRing(nodesPerRing);
	}
	
	public int getNodesPerRing() {
		return nodesPerRing;
	}

	public void setNodesPerRing(int nodesPerRing) {
		this.nodesPerRing = nodesPerRing;
	}
}
