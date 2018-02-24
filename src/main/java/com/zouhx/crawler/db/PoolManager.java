/*  
 * File: PoolManager.java
 * 
 * Version 0.5
 *
 * 2007-10-11
 * 
 * proxool���ݿ����ӳ�
 * 
 * Copyright EverSino TD-AIMS Software (2007).
 * history:
 Date      Author      Changes
 2007-10-11 Yuhongtao   Created   
 */
package com.zouhx.crawler.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.logicalcobwebs.proxool.ConnectionInfoIF;
import org.logicalcobwebs.proxool.ProxoolException;
import org.logicalcobwebs.proxool.ProxoolFacade;
import org.logicalcobwebs.proxool.admin.SnapshotIF;
import org.logicalcobwebs.proxool.configuration.JAXPConfigurator;

import com.zouhx.crawler.config.ConstMcc;
import com.zouhx.crawler.main.MccCfg;

public class PoolManager {

	private static int activeCount = 0;

	public PoolManager() {
	}

	/**
	 * ��ȡ���� getConnection
	 * 
	 * @param name
	 * @return
	 */
	public Connection getConnection(String alias) {
		try {
			Class.forName("org.logicalcobwebs.proxool.ProxoolDriver");
			Connection conn = DriverManager.getConnection("proxool." + alias);
			//showSnapshotInfo(alias);
			//printStacks("GETCPSLITECONN");
			return conn;
		} catch (Exception ex) {
			MccCfg.log.oWarning(ex.toString(), ConstMcc.nLog_Debug);
		}
		return null;
	}

	/**
	 * �˷������Եõ����ӳص���Ϣ showSnapshotInfo
	 */
	public void showSnapshotInfo(String alias) throws Exception {
		try {
			SnapshotIF snapshot = ProxoolFacade.getSnapshot(alias, true);
			int curActiveCount = snapshot.getActiveConnectionCount();// ��û������?
			int availableCount = snapshot.getAvailableConnectionCount();// ��ÿɵõ���������?
			int maxCount = snapshot.getMaximumConnectionCount();// �����������?

			// if (curActiveCount != activeCount)// ����������仯ʱ��������?
			// {
			String msg = "######?ORACLE CONNECTIONS:(" + alias
					+ ")(act./avai./t.):" + curActiveCount + "/"
					+ availableCount + "/" + maxCount + ".";
			MccCfg.log.oInfo(msg, ConstMcc.nLog_Debug);
			/*
			 * ConnectionInfoIF[] ciif = snapshot.getConnectionInfos(); for(int
			 * i=0; i<ciif.length;i++){ msg =
			 * "##"+ciif[i].getBirthDate().toString()+","+ ciif[i].getAge()+","+
			 * ciif[i].getRequester()+","+ //ciif[i].getSqlCalls()[0];
			 * ciif[i].getId()+","+ ciif[i].getStatus();
			 * 
			 * MccCfg.log.oInfo(msg, ConstMcc.nLog_Debug); }
			 */
			activeCount = curActiveCount;
			// }
		} catch (ProxoolException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * �ͷ����� freeConnection
	 * 
	 * @param conn
	 */
	public void freeConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
				//printStacks("CLOSECPSLITECONN");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * �ͷ����� freeConnection
	 * 
	 * @param name
	 * @param con
	 */
	public void freeConnection(String name, Connection con) {
		freeConnection(con);
	}

	public void init(String fileName) throws Exception {
		try {
			JAXPConfigurator.configure(fileName, false);
		} catch (Exception ex) {
			throw ex;
		}
	}

	public void printStacks(String prompt) {
		Throwable ex = new Throwable();
		StackTraceElement[] stacks = ex.getStackTrace();
		String msg = "";

		if (stacks != null) {
			for (int i = 1; i < stacks.length; i++) {
				msg += "<<" + stacks[i].getClassName() + ":"
						+ stacks[i].getMethodName();
				if (i==3)
					break;
			}
			MccCfg.log.info(prompt + ":  " + msg);
		}
	}
}
