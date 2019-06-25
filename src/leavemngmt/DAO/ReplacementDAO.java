package leavemngmt.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import leavemngmt.beans.Application;
import leavemngmt.beans.Faculty;
import leavemngmt.beans.Replacement;

public class ReplacementDAO extends BaseDAO {
	
	public static LinkedList<Replacement> getAllReplacements(String appId){
		LinkedList<Replacement> res = new LinkedList<Replacement>();
		Connection con = null;
		try {
			con = getCon();
			String sql = "SELECT * FROM replacement WHERE application_id = ?";
			PreparedStatement stmt;
			stmt = con.prepareStatement(sql);
			stmt.setString(1,appId);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				Replacement rep = new Replacement();
				rep.setApplication_id(rs.getInt("application_id"));
				rep.setSurrogate_eid(rs.getString("surrogate_eid"));
				rep.setFrom_date(rs.getString("from_date"));
				rep.setTo_date(rs.getString("to_date"));
				rep.setDays(rs.getInt("days"));
				res.add(rep);
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{
			closeCon(con);
		}
		return res;
	}
	
	public static void insert(Replacement rep){
		Connection con = null;
		
		try {
			con = getCon();
			String sql;
			int i,n;
			PreparedStatement stmt;
			sql = "INSERT INTO replacement (application_id,surrogate_eid,from_date,to_date,days) VALUES (?,?,?,?,?)";
			stmt = con.prepareStatement(sql);
			
			i =1;
			stmt.setInt(i++, rep.getApplication_id());
			stmt.setString(i++, rep.getSurrogate_eid());
			stmt.setString(i++, rep.getFrom_date());
			stmt.setString(i++, rep.getTo_date());
			stmt.setInt(i++, rep.getDays());
			n=stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			closeCon(con);
		}
	}	
	
	public static boolean checkAvailability(Replacement rep){
		boolean tof=true;
		Connection con=null;
		
		try{
			con = getCon();
			String sql = "SELECT from_date, to_date FROM replacement WHERE application_id=?";
			PreparedStatement stmt = con.prepareStatement(sql);
			int i=1;
			stmt.setInt(i++,rep.getApplication_id());
			ResultSet rs = stmt.executeQuery();
			
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			
			while(rs.next()){
				Date d1 = df.parse(rs.getString("from_date"));
				Date d2 = df.parse(rs.getString("to_date"));
				
				Date ad1 = df.parse(rep.getFrom_date());
				Date ad2 = df.parse(rep.getTo_date());
				
				if(ad1.compareTo(d1)>=0 && ad1.compareTo(d2)<=0){
					tof = false;
					break;
				}else if(ad2.compareTo(d1)>=0 && ad2.compareTo(d2)<=0){
					tof = false;
					break;
				}
			}
		}catch(SQLException | ParseException e){
			e.printStackTrace();
		}finally{
			closeCon(con);
		}
		return tof;
	}
	
	
}