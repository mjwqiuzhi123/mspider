package com.zouhx.crawler.db.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.zouhx.crawler.db.BaseDao;
import com.zouhx.crawler.db.template.DaoTemplate;
import com.zouhx.crawler.main.MccCfg;




public class BaseDaoImpl implements BaseDao{
	protected DaoTemplate template ; 
	protected Connection conn = null;
	protected ResultSet rs = null;
	protected PreparedStatement prepStmt = null;

	public BaseDaoImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object execute(DaoTemplate template,String alias) {
		this.template = template;
		try{
			conn=MccCfg.dbPool.getConnection(alias);
			return this.template.execute(conn,prepStmt,rs);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				closeAll(rs,prepStmt,conn);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public Object executeTransaction(DaoTemplate temolate, String alias) {
		Object obj = null;
		this.template = temolate;
		try{
			conn=MccCfg.dbPool.getConnection(alias);
			conn.setAutoCommit(false);
			obj = this.template.execute(conn,prepStmt,rs);
			conn.commit();
		}catch(Exception e){
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			throw new RuntimeException();
		}finally{
			try {
				conn.setAutoCommit(true);
				this.closeAll(rs, prepStmt, conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return obj;
	}


	@Override
	public void closeAll(ResultSet rs, PreparedStatement prepStmt,
			Connection conn) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			rs = null;
		}
		if (prepStmt != null) {
			try {
				prepStmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			prepStmt = null;
		}
		MccCfg.dbPool.freeConnection(conn);		
		
	}

	@Override
	public Connection getConnection(String alias) {
		// TODO Auto-generated method stub
		return MccCfg.dbPool.getConnection(alias);
	}


	
	
}
