package com.vw.ide.shared.servlet.projectmanager.specific;

import java.io.Serializable;

/**
 * Defines set of properties of VWML compiler
 * @author Oleg
 *
 */
@SuppressWarnings("serial")
public class CompilerSwitchesDescription implements Serializable {
	public static enum Mode {
		SOURCE("source:Generate Java source code from VWML code only"),
		PROJECT("project:Java project, compilation isn't run "),
		COMPILE("compile:Java project, compilation only"),
		TEST("test:Java project (test), compilation and tests"),
		MAIN("main:Java project (ready), compilation and static test");
		
		private final String value;
		private static final int NAME = 0x00;
		private static final int DESCRIPTION = 0x01;
		
		Mode(String mode) {
			value = mode;
		}
		
		public static Mode fromValue(String value) {
			if (value != null) {
				for(Mode mode : values()) {
					if (mode.value.equals(value)) {
						return mode;
					}
				}
			}
			return defMode();
		}
		
		public static Mode defMode() {
			return TEST;
		}
		
		public String toValue() {
			return value;
		}
		
		public Mode key() {
			return fromValue(value);
		}
		
		public Mode mode() {
			return fromValue(value);
		}
		
		public String toString() {
			return (toValue().split(":"))[Mode.DESCRIPTION];
		}
	}
	
	public static enum TestSwitches {
		All("All"),
		Static("Static");
		
		String value;
		
		TestSwitches(String value) {
			this.value = value;
		}
		
		public static TestSwitches fromValue(String value) {
			if (value != null) {
				for(TestSwitches s : values()) {
					if (s.value.equals(value)) {
						return s;
					}
				}
			}
			return defSwitch();
		}
		
		public static TestSwitches defSwitch() {
			return Static;
		}
		
		public String toValue() {
			return value;
		}
		
	}
	
	// separated by ',' and has format <directive> = <value> (example debug = true)
	private String preprocessorDirectives;
	//  compiler's switches => (source | project | compile | test | main)
	private Mode compilationMode = Mode.SOURCE;
	// static, all
	// static - post compilation late binding checking (checks contexts and links on consistency)
	// all - runs static test and model (the model is run twice for checking ability to finish its work in right way)
	private String testSwitch;
	// true of false - in case 'true' debug info is included (VWML -> Java) association is built
	private Boolean includeDebugInfo = Boolean.FALSE;
	@SuppressWarnings("unused")
	private String key = null;
	
	public String getPreprocessorDirectives() {
		return preprocessorDirectives;
	}
	
	public void setPreprocessorDirectives(String preprocessorDirectives) {
		this.preprocessorDirectives = preprocessorDirectives;
	}
	
	public Mode getCompilationMode() {
		return compilationMode;
	}
	
	public void setCompilationMode(Mode compilationMode) {
		this.compilationMode = compilationMode;
	}
	
	public String getTestSwitch() {
		return testSwitch;
	}
	
	public void setTestSwitch(String testSwitch) {
		this.testSwitch = testSwitch;
	}
	
	public Boolean getIncludeDebugInfo() {
		return includeDebugInfo;
	}

	public void setIncludeDebugInfo(Boolean includeDebugInfo) {
		this.includeDebugInfo = includeDebugInfo;
	}

	public String getKey() {
		return getCompilationMode().toValue();
	}

	public void setKey(String key) {
		
	}
	
	public String modeName() {
		return (getCompilationMode().toValue().split(":"))[Mode.NAME];
	}
	
	public String toString() {
		return (getCompilationMode().toValue().split(":"))[Mode.DESCRIPTION];
	}

	public String readable() {
		return "CompilerSwitchesDescription [preprocessorDirectives="
				+ preprocessorDirectives + ", compilationMode="
				+ compilationMode + ", testSwitch=" + testSwitch
				+ ", includeDebugInfo=" + includeDebugInfo + "]";
	}
}
