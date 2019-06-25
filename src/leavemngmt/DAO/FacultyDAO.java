package leavemngmt.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import leavemngmt.beans.Faculty;

public class FacultyDAO extends BaseDAO {
	
	public static LinkedList<Faculty> getAllFaculties(){
		LinkedList<Faculty> res = new LinkedList<Faculty>();
		Connection con = null;
		
		try {
			con = getCon();
			String sql = "SELECT * FROM faculty";
			PreparedStatement stmt;
			stmt = con.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				Faculty f = new Faculty();
				f.setEid(rs.getString("eid"));
				f.setFname(rs.getString("fname"));
				f.setLname(rs.getString("lname"));
				f.setEmail(rs.getString("email"));
				f.setDesignation(rs.getString("designation"));
				f.setDepartment(rs.getString("department"));
				f.setMno(rs.getLong("mno"));
				res.add(f);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			closeCon(con);
		}
		return res;
	} 
	
	public static String getHOD(String eid){
		String str = null;
		Connection con=null;
		try {
			con = getCon();
			String sql = "SELECT eid FROM (SELECT department FROM faculty  WHERE eid=?) AS xyz, faculty WHERE faculty.department = xyz.department AND faculty.designation=?";
			PreparedStatement stmt;
			stmt = con.prepareStatement(sql);
			stmt.setString(1, eid);
			stmt.setString(2, "HOD");
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				str = rs.getString("eid");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			closeCon(con);
		}
		return str;
	}
	
	public static Faculty getFaculty(String eid){
		Faculty f=null;
		Connection con = null;
		
		try {
			con = getCon();
			String sql = "SELECT * FROM faculty where eid = ?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, eid);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				f = new Faculty();
				f.setEid(rs.getString("eid"));
				f.setFname(rs.getString("fname"));
				f.setLname(rs.getString("lname"));
				f.setEmail(rs.getString("email"));
				f.setDesignation(rs.getString("designation"));
				f.setDepartment(rs.getString("department"));
				f.setMno(rs.getLong("mno"));
				f.setPassword(rs.getString("password"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			closeCon(con);
		}
		return f;
	}
	
	public static void insert(Faculty f){
		Connection con = null;
		
		try {
			con = getCon();
			String sql = "INSERT INTO faculty (eid, fname, lname, department, designation, email, mno, password) VALUES (?,?,?,?,?,?,?,?)";
			PreparedStatement stmt = con.prepareStatement(sql);
			int i =1;
			stmt.setString(i++, f.getEid());
			stmt.setString(i++, f.getFname());
			stmt.setString(i++, f.getLname());
			stmt.setString(i++, f.getDepartment());
			stmt.setString(i++, f.getDesignation());
			stmt.setString(i++, f.getEmail());
			stmt.setLong(i++, f.getMno());
			stmt.setString(i++, f.getPassword());
			int n=stmt.executeUpdate();
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			closeCon(con);
		}
		
	}
	
	public static void update(Faculty f){
		Connection con = null;
		try {
			con = getCon();
			String sql = "UPDATE faculty SET fname=?, lname=?, department=?, designation=?, email=?, mno=?, password=? WHERE eid=?";
			PreparedStatement stmt = con.prepareStatement(sql);
			int i =1;
			stmt.setString(i++, f.getFname());
			stmt.setString(i++, f.getLname());
			stmt.setString(i++, f.getDepartment());
			stmt.setString(i++, f.getDesignation());
			stmt.setString(i++, f.getEmail());
			stmt.setLong(i++, f.getMno());
			stmt.setString(i++, f.getPassword());
			stmt.setString(i++, f.getEid());
			int n=stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			closeCon(con);
		}	
	}
	
	public static Faculty validateUser(String eid, String pass){
		Faculty f = null;
		Connection con=null;
		try{
			con = getCon();
			String sql = "select * from faculty where eid = ? and password = ?";
			PreparedStatement stmt = con.prepareStatement(sql);
			int i=1;
			stmt.setString(i++, eid);
			stmt.setString(i++, pass);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				f = new Faculty();
				f.setEid(rs.getString("eid"));
				f.setFname(rs.getString("fname"));
				f.setLname(rs.getString("lname"));
				f.setEmail(rs.getString("email"));
				f.setDesignation(rs.getString("designation"));
				f.setDepartment(rs.getString("department"));
				f.setMno(rs.getLong("mno"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeCon(con);
		}
		return f;
	}
	
	public static int facultyRank(String str){
		int res=0;
		switch(str)
		{
		case "Faculty":
		case "Administrative Staff":
			res = 1;
			break;
		case "HOD":
		case "Registrar":
		case "Director":
			res = 2;
			break;
		}
		
		return res;
	}

	public static boolean isUserAlready(String eid) {
		boolean tof=false;
		Connection con=null;
		try{
			con = getCon();
			String sql = "select * from faculty where eid = ?";
			PreparedStatement stmt = con.prepareStatement(sql);
			int i=1;
			stmt.setString(i++, eid);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				tof=true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeCon(con);
		}
		return tof;
	}

	public static String getRegistrar() {
		String str = null;
		Connection con=null;
		try {
			con = getCon();
			String sql = "SELECT eid FROM faculty WHERE designation = ?";
			PreparedStatement stmt;
			stmt = con.prepareStatement(sql);
			stmt.setString(1, "Registrar");
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				str = rs.getString("eid");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			closeCon(con);
		}
		return str;
	}
	
	public static String getDirector() {
		String str = null;
		Connection con=null;
		try {
			con = getCon();
			String sql = "SELECT eid FROM faculty WHERE designation = ?";
			PreparedStatement stmt;
			stmt = con.prepareStatement(sql);
			stmt.setString(1, "Director");
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				str = rs.getString("eid");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			closeCon(con);
		}
		return str;
	}

	public static Faculty getHODByDepartment(String department) {
		Faculty f =null;
		Connection con=null;
		try {
			con = getCon();
			String sql = "SELECT eid, fname, lname FROM faculty WHERE designation = ? AND department = ?";
			PreparedStatement stmt;
			stmt = con.prepareStatement(sql);
			stmt.setString(1, "HOD");
			stmt.setString(2, department);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				f= new Faculty();
				f.setEid(rs.getString("eid"));
				f.setFname(rs.getString("fname"));
				f.setLname(rs.getString("lname"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			closeCon(con);
		}
		return f;
	}

	public static LinkedList<Faculty> getFacultyByDepartment(String eid) {
		LinkedList<Faculty> res = new LinkedList<Faculty>();
		Connection con=null;
		try {
			con = getCon();
			String sql = "SELECT eid, fname, lname FROM (SELECT designation FROM faculty  WHERE eid=?) AS xyz, faculty WHERE faculty.designation = xyz.designation";
			PreparedStatement stmt;
			stmt = con.prepareStatement(sql);
			stmt.setString(1, eid);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				Faculty f = new Faculty();
				f.setEid(rs.getString("eid"));
				f.setFname(rs.getString("fname"));
				f.setLname(rs.getString("lname"));
				res.add(f);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			closeCon(con);
		}
		return res;
	}
}
