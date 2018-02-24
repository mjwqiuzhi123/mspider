/*  
 * File: MccLogger.java
 * 
 * Version 0.5
 *
 * 2007-12-10
 * 
 * Copyright EverSino TD-AIMS Software (2007).
 * history:
 Date      Author      Changes
 2007-12-10 Yuhongtao   Created   
 */
package com.zouhx.crawler.log;

import java.util.logging.Filter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class MccLogger extends Logger {

	private static final int offValue = Level.OFF.intValue();

	private volatile int levelValue;

	private Filter filter;

	protected MccLogger(String name) {
		super(name, null);
	}

	public static Logger getLogger(String name) {
		LogManager m = LogManager.getLogManager();
		Object l = m.getLogger(name);
		if (l == null)
			m.addLogger(new MccLogger(name));
		l = m.getLogger(name);
		return (MccLogger) l;
	}

	public void oFinest(String msg, int nLogType) {
		if (Level.FINEST.intValue() < levelValue) {
			return;
		}
		LogRecord lr = new LogRecord(Level.FINEST, msg);
		log(lr, nLogType);
	}

	public void oFiner(String msg, int nLogType) {
		if (Level.FINER.intValue() < levelValue) {
			return;
		}
		LogRecord lr = new LogRecord(Level.FINER, msg);
		log(lr, nLogType);
	}

	public void oFine(String msg, int nLogType) {
		if (Level.FINE.intValue() < levelValue) {
			return;
		}
		LogRecord lr = new LogRecord(Level.FINE, msg);
		log(lr, nLogType);
	}

	public void oInfo(String msg, int nLogType) {
		if (Level.INFO.intValue() < levelValue) {
			return;
		}
		LogRecord lr = new LogRecord(Level.INFO, msg);
		log(lr, nLogType);
		
	}

	public void oWarning(String msg, int nLogType) {
		if (Level.WARNING.intValue() < levelValue) {
			return;
		}
		LogRecord lr = new LogRecord(Level.WARNING, msg);
		log(lr, nLogType);
	}

	public void oSevere(String msg, int nLogType) {
		if (Level.SEVERE.intValue() < levelValue) {
			return;
		}
		LogRecord lr = new LogRecord(Level.SEVERE, msg);
		log(lr, nLogType);
	}

	public void log(LogRecord record, int nLogType) {
		try {
			
		    //ֱ�ӱ�ʶ�����������?
		    StackTraceElement stack[] = (new Throwable()).getStackTrace();
			int ii = 0;
			while (ii < stack.length) {
				if (!stack[ii].getClassName().equals("eversino.tdaims.mcc.mcf.log.MccLogger")) {
					record.setLoggerName(stack[ii].getClassName() + "(line:" + stack[ii].getLineNumber() + ")");
					break;
				}
				ii++;
			}			
			if (record.getLevel().intValue() < levelValue || levelValue == offValue) {
				return;
			}
			
			
			//MARKED by zhaoxy@20101119
			/*
			synchronized (this) {
				if (filter != null && !filter.isLoggable(record)) {
					return;
				}
			}
            */			
			
			// Post the LogRecord to all our Handlers, and then to
			// our parents' handlers, all the way up the tree.
			Logger logger = this;
			while (logger != null) {
				Handler targets[] = logger.getHandlers();
				if (targets != null) {
					for (int i = 0; i < targets.length; i++) {
						if ((targets[i].getClass().toString()).indexOf("JDBCLogHandler") > 0) {
							((JDBCLogHandler) (targets[i])).publish(record, nLogType);
						} else
							targets[i].publish(record);
					}
				}
				if (!logger.getUseParentHandlers()) {
					break;
				}
				logger = logger.getParent();
			}
		} catch (Exception e) {

		}
	}

}
