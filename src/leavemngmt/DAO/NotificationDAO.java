package leavemngmt.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import leavemngmt.beans.Application;
import leavemngmt.beans.Faculty;
import leavemngmt.beans.MergedNotification;
import leavemngmt.beans.Notification;
import lmUtils.LoginUtil;

public class NotificationDAO extends BaseDAO{

	public static void insert(Notification ntf) {
		Connection con = null;
		
		try {
			con = getCon();
			String sql = "INSERT INTO notification (application_id, sender_eid, recipient_eid, recipient_designation, is_new_for_sender) VALUES (?,?,?,?,?)";
			PreparedStatement stmt = con.prepareStatement(sql);
			int i =1;
			stmt.setInt(i++, ntf.getApplication_id());
			stmt.setString(i++, ntf.getSender_eid());
			stmt.setString(i++, ntf.getRecipient_eid());
			stmt.setString(i++, ntf.getRecipient_designation());
			stmt.setString(i++,  ntf.getIs_new_for_sender());
			int n=stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			closeCon(con);
		}
	}
	
	public static LinkedList<MergedNotification> getNotification(String sor, String eid){
//		LinkedList<Notification> res = new LinkedList<Notification>(); 
//		Connection con = null;
//		
//		try {
//			con = getCon();
//			String sql = "SELECT * FROM notification WHERE "+sor+"=?"; // TO order by newest sql = sql + " order by is_new_for_sender ";
//			PreparedStatement stmt;
//			stmt = con.prepareStatement(sql);
//			stmt.setString(1, eid);
//			ResultSet rs = stmt.executeQuery();
//			while(rs.next()){
//				Notification ntf = new Notification();
//				ntf.setNotification_id(rs.getInt("notification_id"));
//				ntf.setApplication_id(rs.getInt("application_id"));
//				ntf.setLastUpdate(rs.getString("lastUpdate"));
//				ntf.setIs_new_for_sender(rs.getString("is_new_for_sender"));
//				ntf.setIs_new_for_recipient(rs.getString("is_new_for_recipient"));
//				ntf.setRecipient_eid(rs.getString("recipient_eid"));
//				ntf.setRecipient_designation(rs.getString("recipient_designation"));
//				ntf.setSender_eid(rs.getString("sender_eid"));
//				res.add(ntf);
//			}
//			
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			closeCon(con);
//		}
//		return res;
		
		LinkedList<MergedNotification> res = new LinkedList<MergedNotification>(); 
		Connection con = null;
		
		try {
			con = getCon();
			String sql = "SELECT * FROM (SELECT ABC.application_id, eid, apply_date, from_date, to_date, days, type_of_leave, reason, status, fname, lname, department, notification_id, sender_eid, recipient_eid, recipient_designation, is_new_for_sender, is_new_for_recipient, lastUpdate FROM (SELECT application_id, application.eid, apply_date, from_date, to_date, days, type_of_leave, reason, status, fname, lname, department FROM application INNER JOIN faculty ON application.eid = faculty.eid) AS ABC INNER JOIN notification ON ABC.application_id = notification.application_id) AS xyz  WHERE xyz."+sor+"=? ORDER BY xyz.apply_date DESC"; // TO order by newest sql = sql + " order by is_new_for_sender ";
			PreparedStatement stmt;
			stmt = con.prepareStatement(sql);
			stmt.setString(1, eid);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				MergedNotification mntf = new MergedNotification();
				
				Notification ntf = new Notification();
				ntf.setNotification_id(rs.getInt("notification_id"));
				ntf.setApplication_id(rs.getInt("application_id"));
				ntf.setLastUpdate(rs.getString("lastUpdate"));
				ntf.setIs_new_for_sender(rs.getString("is_new_for_sender"));
				ntf.setIs_new_for_recipient(rs.getString("is_new_for_recipient"));
				ntf.setRecipient_eid(rs.getString("recipient_eid"));
				ntf.setRecipient_designation(rs.getString("recipient_designation"));
				ntf.setSender_eid(rs.getString("sender_eid"));
				mntf.setNotification(ntf);
				
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
				mntf.setApplication(app);
				
				Faculty f = new Faculty();
				f.setEid(rs.getString("eid"));
				f.setFname(rs.getString("fname"));
				f.setLname(rs.getString("lname"));
				f.setDepartment(rs.getString("department"));
				mntf.setFaculty(f);
				
				res.add(mntf);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			closeCon(con);
		}
		return res;
		
		
	}
	
	public static void update(Notification ntf){
//		Connection con = null;
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
//		try {
//			con = getCon();
//			String sql = "UPDATE notification SET lastUpdate=?, is_new_for_sender=?, is_new_for_recipient=? WHERE notification_id=?";
//			PreparedStatement stmt = con.prepareStatement(sql);
//			int i =1;
//			stmt.setString(i++, df.format(new Date()));
//			stmt.setString(i++, ntf.getIs_new_for_sender());
//			stmt.setString(i++, ntf.getIs_new_for_recipient());
//			stmt.setInt(i++, ntf.getNotification_id());
//			int n=stmt.executeUpdate();
//			
//			if(ntf.getRecipient_designation().equals("HOD") && ApplicationDAO.getStatus(ntf.getApplication_id()).indexOf("Approved")!=-1){
//				Notification ntf1 = new Notification();
//				ntf1.setRecipient_eid(FacultyDAO.getDirector());
//				ntf1.setRecipient_designation("Director");
//				ntf1.setIs_new_for_sender("yes");
//				ntf1.setApplication_id(ntf.getApplication_id());
//				ntf1.setSender_eid(ntf.getSender_eid());
//				NotificationDAO.insert(ntf1);
//			}
//			
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			closeCon(con);
//		}
		
		Connection con = null;
		try {
			
			if(ApplicationDAO.getStatus(ntf.getApplication_id()).indexOf("Approved by HOD")!=-1){
				Notification ntf1 = new Notification();
				ntf1.setRecipient_eid(FacultyDAO.getDirector());
				ntf1.setRecipient_designation("Director");
				ntf1.setIs_new_for_sender("yes");
				ntf1.setApplication_id(ntf.getApplication_id());
				ntf1.setSender_eid(ntf.getSender_eid());
				NotificationDAO.insert(ntf1);
				
				con = getCon();
				String sql = "DELETE FROM notification WHERE notification_id=?";
				PreparedStatement stmt = con.prepareStatement(sql);
				int i =1;
				stmt.setInt(i++, ntf.getNotification_id());
				int n=stmt.executeUpdate();
			}else{
				con = getCon();
				String sql = "UPDATE notification SET recipient_designation=?, recipient_eid=? WHERE notification_id=?";
				PreparedStatement stmt = con.prepareStatement(sql);
				int i =1;
				stmt.setString(i++, ntf.getRecipient_designation());
				stmt.setString(i++, ntf.getRecipient_eid());
				stmt.setInt(i++, ntf.getNotification_id());
				int n=stmt.executeUpdate();
			}
			
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{
			closeCon(con);
		}
	}
}
