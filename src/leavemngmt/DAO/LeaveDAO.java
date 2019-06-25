package leavemngmt.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

import leavemngmt.beans.Faculty;
import leavemngmt.beans.Leave;

public class LeaveDAO extends BaseDAO{
	public static void initialize(String eid){
		Connection con = null;
		int i,n;
		Date d = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	    String strDate = formatter.format(d); //2017-07-19 06:38:14
		try {
			con = getCon();
			PreparedStatement stmt;
			String sql;
				sql ="INSERT INTO `leave` (`eid`, `type`, `total`, `available`, `cycle`, `lastUpdate`) VALUES (?, 'medical', '10', '10', 'JULY-MAY',?);";
				stmt = con.prepareStatement(sql);
				i =1;
				stmt.setString(i++, eid);
				stmt.setString(i++, strDate);
				n=stmt.executeUpdate();
				
				sql ="INSERT INTO `leave` (`eid`, `type`, `total`, `available`, `cycle`, `lastUpdate`) VALUES (?, 'casual', '8', '8', 'JULY-MAY',?);";
				stmt = con.prepareStatement(sql);
				i =1;
				stmt.setString(i++, eid);
				stmt.setString(i++, strDate);
				n=stmt.executeUpdate();
				
				sql ="INSERT INTO `leave` (`eid`, `type`, `total`, `available`, `cycle`, `lastUpdate`) VALUES (?, 'restricted', '3', '3', 'JULY-MAY',?);";
				stmt = con.prepareStatement(sql);
				i =1;
				stmt.setString(i++, eid);
				stmt.setString(i++, strDate);
				n=stmt.executeUpdate();
				
				sql ="INSERT INTO `leave` (`eid`, `type`, `total`, `available`, `cycle`, `lastUpdate`) VALUES (?, 'study', '15', '15', 'JULY-MAY',?);";
				stmt = con.prepareStatement(sql);
				i =1;
				stmt.setString(i++, eid);
				stmt.setString(i++, strDate);
				n=stmt.executeUpdate();
			
				sql ="INSERT INTO `leave` (`eid`, `type`, `total`, `available`, `cycle`, `lastUpdate`) VALUES (?, 'earned','0','0', 'JANUARY-DECEMBER',?);";
				stmt = con.prepareStatement(sql);
				i =1;
				stmt.setString(i++, eid);
				stmt.setString(i++, strDate);
				n=stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			closeCon(con);
		}
	}
	
	public static void checkLeaveCycle(String eid){
		Connection con = null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	    try{
	    	con = getCon();
	    	String sql ="SELECT lastUpdate FROM leaveManagement_db.leave WHERE eid=? and type=?";
			PreparedStatement stmt = con.prepareStatement(sql);
			int i =1;
			stmt.setString(i++, eid);
			stmt.setString(i++, "medical");
			ResultSet rs=stmt.executeQuery();
			
			if(rs.next()){
				String strDate = rs.getString("lastUpdate");
				Date lu = df.parse(strDate);
				Date d = df.parse(new SimpleDateFormat("yyyy").format(new Date())+"-07-01 00:00:00");
				if(d.after(lu)){
					strDate = df.format(new Date());
					Leave leave=new Leave();
					leave.setLastUpdate(strDate);
					leave.setEid(eid);
					leave.setType("medical");
					leave.setAvailable(10);
					update(leave);
					leave.setType("casual");
					leave.setAvailable(8);
					update(leave);
					leave.setType("restricted");
					leave.setAvailable(3);
					update(leave);
					leave.setType("study");
					leave.setAvailable(15);
					update(leave);
				}
			}
			
			sql ="SELECT lastUpdate, available FROM leaveManagement_db.leave WHERE eid=? and type=?";
			stmt = con.prepareStatement(sql);
			i =1;
			stmt.setString(i++, eid);
			stmt.setString(i++, "earned");
			rs=stmt.executeQuery();
//			if(rs.next()){
//				String strDate = rs.getString("lastUpdate");
//				Date lu = df.parse(strDate);
//				Date d = df.parse(new SimpleDateFormat("yyyy").format(new Date())+"-01-01 00:00:00");
//				if(d.after(lu)){
//					Leave leave=new Leave();
//					leave.setEid(eid);
//					leave.setType("earned");
//					leave.setAvailable(0);
//					update(leave);
//				}
//			}
	    	
			if(rs.next()){
				SimpleDateFormat dformat = new SimpleDateFormat("yyyy-MM-dd");
				String strDate = rs.getString("lastUpdate");
				Date lu = df.parse(strDate);
				Date d = new Date();
				Date d1 = dformat.parse(new SimpleDateFormat("yyyy").format(new Date())+"-01-01");
				Date d2 = dformat.parse(new SimpleDateFormat("yyyy").format(new Date())+"-05-01");
				Date d3 = dformat.parse(new SimpleDateFormat("yyyy").format(new Date())+"-07-01");
				int ava = rs.getInt("available");
				boolean bool = false;
								
				if(lu.before(d1)){
					ava+=5;bool = true;
				}
				if(d2.before(d) && lu.before(d2)){
					ava+=5;bool = true;
				}
				if(d3.before(d) && lu.before(d3)){
					ava+=5;bool = true;
				}
				if(bool){
					Leave leave=new Leave();
					leave.setEid(eid);
					leave.setType("earned");
					leave.setAvailable(ava);
					update(leave);
				}
			}
			
			
	    }catch(Exception e){
	    	e.printStackTrace();
	    }finally{
	    	closeCon(con);
	    }
		
	}
	
	public static LinkedList<Leave> getLeaveDetails(String eid){
		LinkedList<Leave> res = new LinkedList<Leave>();
		Connection con = null;
		
		try {
			con = getCon();
			String sql = "SELECT * FROM leaveManagement_db.leave WHERE eid =?";
			PreparedStatement stmt;
			stmt = con.prepareStatement(sql);
			stmt.setString(1, eid);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				Leave leave = new Leave();
				leave.setEid(rs.getString("eid"));
				leave.setCycle(rs.getString("cycle"));
				leave.setAvailable(rs.getInt("available"));
				leave.setTotal(rs.getInt("total"));
				leave.setType(rs.getString("type"));
				res.add(leave);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			closeCon(con);
		}
		return res;
	} 
	
	public static void update(Leave leave){
		Date d = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	    String strDate = formatter.format(d); //2017-07-19 06:38:14
		Connection con=null;
		try{
			con = getCon();
			PreparedStatement stmt;
			String sql;
			sql ="UPDATE leaveManagement_db.leave SET available = available - ?, lastUpdate=? WHERE eid=? AND type=?";
			stmt = con.prepareStatement(sql);
			int i =1;
			stmt.setInt(i++, leave.getAvailable());
			stmt.setString(i++, strDate);
			stmt.setString(i++, leave.getEid());
			stmt.setString(i++, leave.getType());
			int n=stmt.executeUpdate();
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
			
		}
	}
}
