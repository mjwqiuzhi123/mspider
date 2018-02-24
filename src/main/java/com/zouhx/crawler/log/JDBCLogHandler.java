/*
 * $Id: JDBCLogHandler.java,v 1.4 2010/11/19 16:38:14 zhaoxiyin Exp $
 * 
 * Project: AIMS MCC Trial
 * Author : modified by GPP AG
 * 
 * From http://www.developer.com/java/article.php/10922_1468351_2
 */

package com.zouhx.crawler.log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import com.zouhx.crawler.main.MccCfg;

/**
 * Log handler, writes to DB and console
 * 
 * @author Jeff Heaton (http://www.jeffheaton.com)
 * @version $Id: JDBCLogHandler.java,v 1.4 2010/11/19 16:38:14 zhaoxiyin Exp $
 * 
 */
public class JDBCLogHandler extends Handler {

	/**
	 * Used to hold the connection to the JDBC data source.
	 * 
	 * @uml.property name="connection"
	 */
	Connection connection;

	/**
	 * A SQL statement used to insert into the log table.
	 */
	protected final static String insertSQL = "insert into MCCLog (Severity,Location,Message,Sequence,"
			+ "SourceClass,SourceMethod,ThreadID,Time,IMEI,LOGTYPE)"
			+ "values(?,?,?,?,?,?,?,?,?,?)";

	/**
	 * A SQL statement used to clear the log table.
	 */
	protected final static String clearSQL = "delete from MCCLog;";

	/**
	 * A PreparedStatement object used to hold the main insert statement.
	 * 
	 * @uml.property name="prepInsert"
	 */
	protected PreparedStatement prepInsert;

	/**
	 * A PreparedStatement object used to hold the clear statement.
	 * 
	 * @uml.property name="prepClear"
	 */
	protected PreparedStatement prepClear;

	/**
	 */
	public JDBCLogHandler() {
		try {
			connection = MccCfg.dbPool.getConnection(MccCfg.webdbAlias);			
			prepInsert = connection.prepareStatement(insertSQL);
			prepClear = connection.prepareStatement(clearSQL);
		} catch (SQLException e) {
			System.err.println("Exception on new JDBCLogHandler()." + e);
		}
	}
	
	//���ݿ�������ڳ�ʱ�Ժ���Զ��Ͽ�����д��־ǰ��Ҫ���»ָ����ӡ�
    public boolean activeConnection() {
        try{
            if (connection==null || connection.isClosed()){
                connection = MccCfg.dbPool.getConnection(MccCfg.webdbAlias);         
                prepInsert = connection.prepareStatement(insertSQL);
                prepClear = connection.prepareStatement(clearSQL);                
            }   
        } catch (SQLException e) {
            System.err.println("Exception on JDBCLogHandler.activeConnection()." + e);
            return false;
        }   
        return true;
    }
    
	/**
	 * Internal method used to truncate a string to a specified width. Used to
	 * ensure that SQL table widths are not exceeded.
	 * 
	 * @param str
	 *            The string to be truncated.
	 * @param length
	 *            The maximum length of the string.
	 * @return The string truncated.
	 */
	static public String truncate(String str, int length) {
		if (str == null || str.length() < length)
			return str;
		return (str.substring(0, length));
	}

	/**
	 * Overridden method used to capture log entries and put them into a JDBC
	 * database.
	 * 
	 * @param record
	 *            The log record to be stored.
	 */
	public void publish(LogRecord record) {
	    publish(record,0);
	}
	
	public void publish(LogRecord record,int nLogType) {
		// first see if this entry should be filtered out
		try {
			if (record != null) {
			    
			    //MARKED by zhaoxy@20101119
			    /*
			    if (getFilter() != null) {
					if (!getFilter().isLoggable(record))
						return;
				}
				*/
			    
				if (isLoggable(record)) {							
					String HmuImei = null;					
					String Message = record.getMessage();
					try {
						int HmuImeiPos = Message.indexOf("HMU:");
						if (HmuImeiPos >= 0) {
							HmuImei = Message.substring(HmuImeiPos + 4);
							Message = Message.substring(0, HmuImeiPos);
						}
					} catch (Exception e) {
					}

					// now store the log entry into the table
					if (activeConnection()){
    					try {
    						prepInsert.setInt(1, record.getLevel().intValue());
    						prepInsert.setString(2, truncate(
    								record.getLoggerName(), 63));
    						prepInsert.setString(3, truncate(Message, 255));
    						prepInsert.setLong(4, record.getSequenceNumber());
    						prepInsert.setString(5, truncate(record
    								.getSourceClassName(), 63));
    						prepInsert.setString(6, truncate(record
    								.getSourceMethodName(), 31));
    						prepInsert.setInt(7, record.getThreadID());
    						prepInsert.setTimestamp(8, new Timestamp(System
    								.currentTimeMillis()));
    						prepInsert.setString(9, truncate(HmuImei, 15));
    						prepInsert.setInt(10, nLogType);
    						prepInsert.executeUpdate();
    					} catch (SQLException e) {
    					    System.err.println("Exception on publish(record,nLogType)." + e);
    					}
					}
				}
			}
		} catch (Exception e) {
		}
	}

	/**
	 * Called to close this log handler.
	 */
	public void close() {
		try {
			if (connection != null)
				connection.close();
		} catch (SQLException e) {
			System.err.println("Error on close: " + e);
		}
	}

	/**
	 * Called to clear all log entries from the database.
	 */
	public void clear() {
		/*
		 * try { prepClear.executeUpdate(); } catch ( SQLException e ) {
		 * System.err.println("Error on clear: " + e); }
		 */
	}

	/**
	 * Not really used, but required to implement a handler. Since all data is
	 * immediately sent to the database, there is no reason to flush.
	 */
	public void flush() {
	}
}