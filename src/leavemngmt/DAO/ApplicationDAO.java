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
import leavemngmt.beans.Leave;
import leavemngmt.beans.Notification;
import lmUtils.LoginUtil;

public class ApplicationDAO extends BaseDAO {
	
	public static int insert(Application app){
		java.sql.Connection con=null;
		int res=0;
		
		try{
			con = getCon();
			String sql = "INSERT INTO application (eid,apply_date,from_date,to_date,days,type_of_leave,reason) VALUES (?,?,?,?,?,?,?)";
			PreparedStatement stmt = con.prepareStatement(sql);
			int i=1;
			stmt.setString(i++,app.getEid());
			stmt.setString(i++,app.getApply_date());
			stmt.setString(i++,app.getFrom_date());//Date Formate yyyy-MM-dd
			stmt.setString(i++,app.getTo_date());
			stmt.setInt(i++,app.getDays());
			stmt.setString(i++,app.getType_of_leave());
			stmt.setString(i++,app.getReason());
			int n = stmt.executeUpdate();
			
			ResultSet rs = stmt.getGeneratedKeys();
			rs.next();
			res = rs.getInt(1);
			
//			sql = "SELECT application_id FROM application WHERE eid=? and from_date=?";
//			stmt = con.prepareStatement(sql);
//			i=1;
//			stmt.setString(i++,app.getEid());
//			stmt.setString(i++,app.getFrom_date());
//			ResultSet rs = stmt.executeQuery();
//			if(rs.next()){
//				res = rs.getInt("application_id");
//			}
			
			Notification ntf = new Notification();
			if(LoginUtil.getDesignation().equals("Faculty")){
				ntf.setRecipient_eid(FacultyDAO.getHOD(app.getEid()));
				ntf.setRecipient_designation("HOD");
			}else{
				ntf.setRecipient_eid(FacultyDAO.getRegistrar());
				ntf.setRecipient_designation("Registrar");
			}
			ntf.setIs_new_for_sender("no");
			ntf.setApplication_id(res);
			ntf.setSender_eid(app.getEid());
			NotificationDAO.insert(ntf);
			
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			closeCon(con);
		}
		return res;
		
	}
	
	public static boolean checkAvailability(Application app){
		boolean tof=true;
		Connection con=null;
		
		try{
			con = getCon();
			String sql = "SELECT from_date, to_date FROM application WHERE eid=?";
			PreparedStatement stmt = con.prepareStatement(sql);
			int i=1;
			stmt.setString(i++,app.getEid());
			ResultSet rs = stmt.executeQuery();
			
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			
			while(rs.next()){
				Date d1 = df.parse(rs.getString("from_date"));
				Date d2 = df.parse(rs.getString("to_date"));
				
				Date ad1 = df.parse(app.getFrom_date());
				Date ad2 = df.parse(app.getTo_date());
				
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

	public static LinkedList<Application> getLeaveApplicationByEid(String eid) {
		
		LinkedList<Application> res = new LinkedList<Application>();
		Connection con = null;
		
		try {
			con = getCon();
			String sql = "SELECT * FROM application WHERE eid = ?";
			PreparedStatement stmt;
			stmt = con.prepareStatement(sql);
			stmt.setString(1, eid);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				Application app = new Application();
				app.setEid(rs.getString("eid"));
				app.setApplication_id(rs.getInt("application_id"));
				app.setApply_date(rs.getString("apply_date"));
				app.setFrom_date(rs.getString("from_date"));
				app.setReason(rs.getString("reason"));
				app.setTo_date(rs.getString("to_date"));
				app.setDays(rs.getInt("days"));
				app.setType_of_leave(rs.getString("type_of_leave"));
				app.setStatus(rs.getString("status"));
				res.add(app);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			closeCon(con);
		}
		return res;
	}

	public static Application getLeaveApplicationByAppId(int appId) {
		Application app=null;
		Connection con = null;
		
		try {
			con = getCon();
			String sql = "SELECT * FROM application where application_id = ?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, appId);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				app = new Application();
				app.setEid(rs.getString("eid"));
				app.setApplication_id(rs.getInt("application_id"));
				app.setApply_date(rs.getString("apply_date"));
				app.setFrom_date(rs.getString("from_date"));
				app.setReason(rs.getString("reason"));
				app.setTo_date(rs.getString("to_date"));
				app.setDays(rs.getInt("days"));
				app.setType_of_leave(rs.getString("type_of_leave"));
				app.setStatus(rs.getString("status"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			closeCon(con);
		}
		return app;
	}
	
	public static void updateStatus(Application app){
		Connection con=null;
		try{
			con = getCon();
			String sql = "UPDATE application SET status=? WHERE application_id=?";
			PreparedStatement stmt = con.prepareStatement(sql);
			int i=1;
			stmt.setString(i++, app.getStatus());
			stmt.setInt(i++, app.getApplication_id());
			int n = stmt.executeUpdate();
			
			if(app.getStatus().indexOf("Approved by Director")!=-1 || app.getStatus().indexOf("Approved by Registrar")!=-1){
				sql = "SELECT * FROM application WHERE application_id = ?";
				stmt = con.prepareStatement(sql);
				i=1;
				stmt.setInt(i++, app.getApplication_id());
				ResultSet rs = stmt.executeQuery();
				
				Leave l=null;
				if (rs.next()){
					l = new Leave();
					
					l.setAvailable(rs.getInt("days"));
					l.setEid(rs.getString("eid"));
					l.setType(rs.getString("type_of_leave"));
					
				}
				
				LeaveDAO.update(l);
				
				sql = "SELECT application_id FROM (SELECT days, application_id FROM (SELECT * FROM application WHERE (status = 'Approved by HOD, Waiting for Director''s approval' OR status = 'No response so far') ) AS xyz WHERE xyz.eid = ? AND xyz.type_of_leave = ?) AS abc, (SELECT available FROM leaveManagement_db.leave WHERE leave.eid = ? AND leave.type = ?) AS pqr WHERE abc.days > pqr.available";
				stmt = con.prepareStatement(sql);
				i=1;
				stmt.setString(i++, app.getEid());
				stmt.setString(i++, app.getType_of_leave());
				stmt.setString(i++, app.getEid());
				stmt.setString(i++, app.getType_of_leave());
				rs = stmt.executeQuery();
				
				while(rs.next()){
					
					sql = "UPDATE application SET status=? WHERE application_id=?";
					stmt = con.prepareStatement(sql);
					i=1;
					stmt.setString(i++, "Required leave is more then available");
					stmt.setInt(i++, rs.getInt("application_id"));
					n = stmt.executeUpdate();
					
					sql = "UPDATE notification SET recipient_designation=?, recipient_eid=? WHERE application_id=?";
					stmt = con.prepareStatement(sql);
					i =1;
					stmt.setString(i++, "");
					stmt.setString(i++, "");
					stmt.setInt(i++, rs.getInt("application_id"));
					n=stmt.executeUpdate();
					
				}
				
				
			}
			
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			closeCon(con);
		}
	}
	
	public static String getStatus(int appId){
		String sts=null;
		Connection con=null;
		try{
			con = getCon();
			String sql = "SELECT status FROM application where application_id = ?";
			PreparedStatement stmt = con.prepareStatement(sql);
			int i=1;
			stmt.setInt(i++, appId);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				sts = rs.getString("status");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			closeCon(con);
		}
		
		return sts;
	}
}
