package com.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.app.beans.User;

public class UserDao {

	public int saveUser(User user) {
		int effectiveRows = 0;
		
		Connection con = DBConnection.getConnection();
		try {
			PreparedStatement stmt = con.prepareStatement("INSERT INTO User values(?,?,?)");
			stmt.setInt(1, user.getId());
			stmt.setString(2, user.getName());
			stmt.setString(3, user.getEmailAddress());
			effectiveRows = stmt.executeUpdate();
			
		} catch (SQLException e) {
			Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, e);
		}
		return effectiveRows;
	}
}
