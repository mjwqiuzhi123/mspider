package com.zouhx.crawler.db.template;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface DaoTemplate {
	Object execute(Connection conn,PreparedStatement prepStmt,ResultSet rs) throws SQLException;
}
