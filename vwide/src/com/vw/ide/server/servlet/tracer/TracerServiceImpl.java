package com.vw.ide.server.servlet.tracer;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import net.zschech.gwt.comet.server.CometServlet;
import net.zschech.gwt.comet.server.CometSession;

import org.apache.log4j.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.vw.ide.server.servlet.IService;
import com.vw.ide.server.servlet.locator.ServiceLocator;
import com.vw.ide.shared.servlet.tracer.RemoteTracer;
import com.vw.ide.shared.servlet.tracer.TracerData;
import com.vw.ide.shared.servlet.tracer.TracerRegisterResult;
import com.vw.ide.shared.servlet.tracer.TracerUnregisterResult;

/**
 * Implementation of tracer's service 
 * @author Oleg
 *
 */
@SuppressWarnings("serial")
public class TracerServiceImpl extends RemoteServiceServlet implements RemoteTracer, IService {

	protected static class Servant implements Runnable {

		private TracerServiceImpl owner = null;
		private Thread activity = null;
		private String userName = null;
		private ConcurrentLinkedQueue<TracerData<?>> cq = new ConcurrentLinkedQueue<TracerData<?>>();
		private AtomicBoolean stop = new AtomicBoolean();
		private Logger logger = Logger.getLogger(Servant.class);
		
		public Servant(String userName, TracerServiceImpl owner) {
			this.userName = userName;
			this.owner = owner;
		}

		public void start() {
			if (activity == null) {
				activity = new Thread(this);
				activity.start();
			}
		}
		
		public void stop() {
			if (activity != null) {
				stop.set(true);
				wakeUp();
				activity = null;
			}
		}
		
		public void push(TracerData<?> data) {
			cq.offer(data);
			wakeUp();
		}
		
		public TracerData<?> pop() {
			return cq.peek();
		}
		
		@Override
		public void run() {
			if (logger.isInfoEnabled()) {
				logger.info("Servant for '" + userName + "' started");
			}
			while(!stop.get()) {
				TracerData<?> d = pop();
				if (d == null) {
					synchronized(this) {
						try {
							wait();
						} catch (InterruptedException e) {
						}
					}
				}
				process(d);
			}
			cq.clear();
			if (logger.isInfoEnabled()) {
				logger.info("Servant for '" + userName + "' stopped");
			}
		}

		protected void process(TracerData<?> data) {
			if (data != null && owner != null && userName != null) {
				owner.propagate(userName, data);
			}
		}
		
		private void wakeUp() {
			synchronized(this) {
				this.notify();
			}
		}
	}
	
	private Map<String, Servant> servants = new ConcurrentHashMap<String, Servant>();
	private ConcurrentMap<String, CometSession> traceClients = new ConcurrentHashMap<String, CometSession>();
	private Logger logger = Logger.getLogger(TracerServiceImpl.class);
	
	public static final int ID = 1051;

	public TracerServiceImpl() {
		super();
		if (logger.isInfoEnabled()) {
			logger.info("TracerServiceImpl constructed");
		}
	}
	
	@Override
	public int getId() {
		return ID;
	}

	@Override
	public String getName() {
		return getClass().getName();
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ServiceLocator.instance().register(this);
		if (logger.isInfoEnabled()) {
			logger.info("TracerServiceImpl started and initialized");
		}
	}
	
	@Override
	public TracerRegisterResult register(String userName) {
		TracerRegisterResult r = null;
		if (logger.isInfoEnabled()) {
			logger.info("Registering user '" + userName + "'");
		}
		if (getThreadLocalRequest() != null) {
			HttpSession httpSession = getThreadLocalRequest().getSession();
	        // Get or create the HTTP session for the browser
	        // Get or create the Comet session for the browser
	        if (httpSession != null) {
	        	r = registerInternal(httpSession, userName);
	        }
		}
		else {
			r = new TracerRegisterResult();
			r.setRetCode(TracerRegisterResult.GENERAL_FAIL);
		}
		return r;
	}

	@Override
	public TracerUnregisterResult unregister(String userName) {
		TracerUnregisterResult r = null;
		if (logger.isInfoEnabled()) {
			logger.info("Unregistering user '" + userName + "'");
		}
		if (getThreadLocalRequest() != null) {
			HttpSession httpSession = getThreadLocalRequest().getSession(false);
			if (httpSession != null) {
				r = unregisterInternal(httpSession, userName);
			}
		}
		else {
			r = new TracerUnregisterResult();
			r.setRetCode(TracerUnregisterResult.GENERAL_FAIL);
			
		}
		return r;
	}
	
	public TracerRegisterResult registerInternal(HttpSession ownerSession, String userName) {
		TracerRegisterResult r = new TracerRegisterResult();
		r.setRetCode(TracerRegisterResult.GENERAL_OK);
		if (logger.isInfoEnabled()) {
			logger.info("Registering user '" + userName + "'");
		}
        // Get or create the Comet session for the browser
        if (ownerSession != null) {
	        CometSession cometSession = CometServlet.getCometSession(ownerSession);
	        // Remember the user name for the
	        ownerSession.setAttribute("username", userName);
	        if (traceClients.putIfAbsent(userName, cometSession) != null) {
	        	logger.error("User '" + userName + "' has already been registered on trace service");
	        }
	        Servant s = new Servant(userName, this);
	        servants.put(userName, s);
	        s.start();
        }
        else {
        	r.setRetCode(TracerRegisterResult.GENERAL_FAIL);
        }
		return r;
	}

	public TracerUnregisterResult unregisterInternal(HttpSession ownerSession, String userName) {
		TracerUnregisterResult r = new TracerUnregisterResult();
		if (logger.isInfoEnabled()) {
			logger.info("Unregistering user '" + userName + "'");
		}
		CometSession cometSession = CometServlet.getCometSession(ownerSession, false);
        if (cometSession != null && userName.equals(ownerSession.getAttribute("username"))) {
            // remove the mapping of user name to CometSession
            traceClients.remove(userName, cometSession);
            ownerSession.invalidate();
            Servant s = servants.get(userName);
            if (s != null) {
            	s.stop();
            	servants.remove(userName);
            }
        }
        else {
        	r.setRetCode(TracerUnregisterResult.GENERAL_FAIL);
        }
		return r;
	}
	
	public void pushData(String userName, TracerData<? extends Serializable> data) {
		Servant s = servants.get(userName);
		if (s != null) {
			s.push(data);
		}
	}
	
	protected void propagate(String userName, TracerData<? extends Serializable> data) {
		CometSession cs = traceClients.get(userName);
		if (cs != null) {
			cs.enqueue(data);
		}
	}
}
