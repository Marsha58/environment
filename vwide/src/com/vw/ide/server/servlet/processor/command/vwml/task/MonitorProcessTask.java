package com.vw.ide.server.servlet.processor.command.vwml.task;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import com.vw.ide.server.servlet.processor.servant.Servant.Task;

public class MonitorProcessTask extends Task {
	private InputStream is;
	private InputStream es;
	private String actualUserName;
	private Logger logger = Logger.getLogger(RunVWMLPRojectTask.class);
	
	public MonitorProcessTask(String actualUserName, InputStream is, InputStream es) {
		super();
		this.is = is;
		this.es = es;
		this.actualUserName = actualUserName;
	}

	public InputStream getIs() {
		return is;
	}
	
	public InputStream getEs() {
		return es;
	}
	
	@Override
	public void process() {
		setUserName(actualUserName);
		super.process();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));			
		String line;
		try {
			while ((line = br.readLine()) != null) {
				if (logger.isInfoEnabled()) {
					logger.info(line);
				}
			}
		}
		catch(Exception e) {
			
		}
	}		
}
