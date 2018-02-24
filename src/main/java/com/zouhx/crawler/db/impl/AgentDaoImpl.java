package com.zouhx.crawler.db.impl;

import java.sql.Timestamp;
import java.util.Date;

import com.zouhx.crawler.db.AgentDao;
import com.zouhx.crawler.main.MccCfg;

public class AgentDaoImpl extends BaseDaoImpl implements AgentDao{

	public AgentDaoImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void add(int number) {
		// TODO Auto-generated method stub
		try{
			conn =getConnection("webServer");
			String sql = "";
			StringBuffer bf = new StringBuffer("insert into agent_report (reporttime,num) values(?,?)");
			sql = bf.toString();
			
			if(conn!=null){
				prepStmt = conn.prepareStatement(sql);
				prepStmt.setTimestamp(1, new Timestamp(new Date().getTime()));
				prepStmt.setInt(2, number);
				prepStmt.executeUpdate();
			}
			MccCfg.repetition_num.incrementAndGet();
		}catch(Exception e){

		}finally{
			closeAll(rs, prepStmt, conn);
		}
	}

}

