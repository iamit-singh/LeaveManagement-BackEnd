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
import leavemngmt.DAO.NotificationDAO;
import leavemngmt.DAO.ReplacementDAO;
import leavemngmt.beans.Leave;
import leavemngmt.beans.MergedNotification;
import leavemngmt.beans.Notification;
import leavemngmt.beans.Replacement;
import leavemngmt.beans.ResponseObject;
import lmUtils.LoginUtil;

@WebServlet("/notification/")
public class NotificationSer extends HttpServlet{
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		if(LoginUtil.isLogin(request, response)){
			
			LinkedList<MergedNotification> res;
			if(FacultyDAO.facultyRank(LoginUtil.getDesignation())==2){
				res = NotificationDAO.getNotification("recipient_eid",LoginUtil.getEid());
			}else{
				res = NotificationDAO.getNotification("sender_eid",LoginUtil.getEid());
			}
			
			new ResponseObject().sendResponse("success","",res,response);
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
			Notification ntf = gson.fromJson(reader, Notification.class);
			NotificationDAO.update(ntf);
			
			new ResponseObject().sendResponse("success","","",response);
		}else{
			response.setStatus(403);
			new ResponseObject().sendResponse("error","Login Failed","",response);
		}
	}
//	
//	@Override
//	protected void dodelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//
//		if(LoginUtil.isLogin(request, response)){
//			
//			BufferedReader reader = request.getReader();
//			Gson gson = new Gson();
//			Notification ntf = gson.fromJson(reader, Notification.class);
//			NotificationDAO.update(ntf);
//			
//			new ResponseObject().sendResponse("success","","",response);
//		}else{
//			response.setStatus(403);
//			new ResponseObject().sendResponse("error","Login Failed","",response);
//		}
//		
//	}
}