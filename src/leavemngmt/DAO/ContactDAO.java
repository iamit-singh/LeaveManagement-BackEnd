package leavemngmt.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;

import leavemngmt.beans.Contact;

public class ContactDAO extends BaseDAO{
	
	public static void insert(Contact ct){
		Connection con = null;
		
		try{
			
			con = getCon();
			String sql = "INSERT INTO contact(name, email, subject, msg) VALUES (?,?,?,?)";
			PreparedStatement stmt = con.prepareStatement(sql);
			int i=1;
			stmt.setString(i++, ct.getName());
			stmt.setString(i++, ct.getEmail());
			stmt.setString(i++, ct.getSubject());
			stmt.setString(i++, ct.getMsg());
			int n = stmt.executeUpdate();
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeCon(con);
		}
		
	}
	
}
