package com.zouhx.crawler.db.impl;

import java.sql.Timestamp;
import java.util.Date;

import com.zouhx.crawler.bean.ReportModel;
import com.zouhx.crawler.db.ReportDao;
import com.zouhx.crawler.main.MccCfg;

public class ReportDaoImpl extends BaseDaoImpl implements ReportDao {

	public ReportDaoImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void add(ReportModel model) {
		// TODO Auto-generated method stub
		try{
			conn =getConnection("webServer");
			String sql = "";
			StringBuffer bf = new StringBuffer("insert into report (reporttime,nodename,total,valid,taskNum) values(?,?,?,?,?)");
			sql = bf.toString();
			
			if(conn!=null){
				prepStmt = conn.prepareStatement(sql);
				prepStmt.setTimestamp(1, new Timestamp(new Date().getTime()));
				prepStmt.setString(2, model.getNodeName());
				prepStmt.setInt(3, model.getTotal());
				prepStmt.setInt(4, model.getValid());
				prepStmt.setInt(5, model.getTaskNum());
				prepStmt.executeUpdate();
			}
			MccCfg.repetition_num.incrementAndGet();
		}catch(Exception e){

		}finally{
			closeAll(rs, prepStmt, conn);
		}
	}
	public static void main(String[] args) throws Exception {
		MccCfg.InitApp();
		ReportDao dao = new ReportDaoImpl();
		ReportModel model = new ReportModel();
		model.setNodeName("node1");
		model.setTotal(1);
		model.setValid(2);
		model.setTaskNum(3);
		dao.add(model);
	}
}

