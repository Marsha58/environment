package com.vw.ide.client.presenters.automata;

import java.util.HashMap;
import java.util.Map;


/**
 * Event handler may have automata in order to process more complex logic than simple response on event
 * @author Oleg
 *
 */
public class EventProcessingAutomata {
	public static class State {
		private int id;

		public State() {
			super();
		}

		public State(int id) {
			super();
			this.id = id;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + id;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			State other = (State) obj;
			if (id != other.id)
				return false;
			return true;
		}
	}
	
	public static abstract class Transition {
		public abstract void handle(EventProcessingAutomata automata);
	}

	public static class Cell {
		private State state;
		private com.google.gwt.event.shared.GwtEvent.Type<?> event;
		
		public Cell(State state, com.google.gwt.event.shared.GwtEvent.Type<?> event) {
			super();
			this.state = state;
			this.event = event;
		}
		
		public State getState() {
			return state;
		}

		public com.google.gwt.event.shared.GwtEvent.Type<?> getEvent() {
			return event;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((event == null) ? 0 : event.hashCode());
			result = prime * result + ((state == null) ? 0 : state.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Cell other = (Cell) obj;
			if (event == null) {
				if (other.event != null)
					return false;
			} else if (!event.equals(other.event))
				return false;
			if (state == null) {
				if (other.state != null)
					return false;
			} else if (!state.equals(other.state))
				return false;
			return true;
		}
	}
	
	private Map<Cell, Transition> automata = new HashMap<Cell, Transition>();
	private State currentState;
	
	public EventProcessingAutomata() {
		
	}
	
	public State getCurrentState() {
		return currentState;
	}

	public void setCurrentState(State currentState) {
		this.currentState = currentState;
	}

	public void add(State state, com.google.gwt.event.shared.GwtEvent.Type<?> event, Transition transition) {
		Cell cell = new Cell(state, event);
		automata.put(cell, transition);
	}
	
	public void handle(com.google.gwt.event.shared.GwtEvent.Type<?> event) throws Exception {
		Transition transition = automata.get(new Cell(currentState, event));
		if (transition == null) {
			throw new Exception("unexpected event '" + event + "' on state '" + currentState + "'");
		}
		transition.handle(this);
	}
}

