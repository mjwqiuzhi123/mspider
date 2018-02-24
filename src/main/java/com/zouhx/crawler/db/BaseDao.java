package com.zouhx.crawler.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.zouhx.crawler.db.template.DaoTemplate;


public interface BaseDao {
	 Object execute(DaoTemplate template,String alias);
	 void closeAll(ResultSet rs,PreparedStatement prepStmt,Connection conn) throws SQLException ;
	 Connection getConnection(String alias);
	 Object executeTransaction(DaoTemplate temolate,String alias);

}
