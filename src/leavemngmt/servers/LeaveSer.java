package leavemngmt.servers;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import leavemngmt.DAO.FacultyDAO;
import leavemngmt.DAO.LeaveDAO;
import leavemngmt.beans.Leave;
import leavemngmt.beans.ResponseObject;
import lmUtils.LoginUtil;

@WebServlet("/leave/*")
public class LeaveSer extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if(LoginUtil.isLogin(request, response)){
			
			String eid = request.getPathInfo();
			eid=eid.substring(1);                       // send eid
			if(eid.indexOf("/")!=-1){ 
				eid=eid.substring(0, eid.indexOf("/"));
			}
			LeaveDAO.checkLeaveCycle(eid);
			
			if(FacultyDAO.facultyRank(LoginUtil.getDesignation())==2 || eid.equals(LoginUtil.getEid())){
				LinkedList<Leave> res = LeaveDAO.getLeaveDetails(eid);
				new ResponseObject().sendResponse("success","",res,response);
			}else{
				response.setStatus(401);
				new ResponseObject().sendResponse("error","Unuthorized","",response);
			}
		}else{
			response.setStatus(403);
			new ResponseObject().sendResponse("error","Login Failed","",response);
		}
	}
	
	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(LoginUtil.isLogin(request, response)){
			
			BufferedReader reader = request.getReader();
			Gson gson = new Gson();
			Leave leave = gson.fromJson(reader, Leave.class);
			leave.setEid(LoginUtil.getEid());
			LeaveDAO.checkLeaveCycle(leave.getEid());
			LeaveDAO.update(leave);
			
			new ResponseObject().sendResponse("success","","",response);
		}else{
			response.setStatus(403);
			new ResponseObject().sendResponse("error","Login Failed","",response);
		}
	}

}
