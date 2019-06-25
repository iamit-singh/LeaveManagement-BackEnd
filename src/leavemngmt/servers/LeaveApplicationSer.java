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
import com.google.gson.JsonObject;

import leavemngmt.DAO.ApplicationDAO;
import leavemngmt.DAO.FacultyDAO;
import leavemngmt.beans.Application;
import leavemngmt.beans.ResponseObject;
import lmUtils.LoginUtil;

@WebServlet("/application/*")
public class LeaveApplicationSer extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(LoginUtil.isLogin(request, response)){
			
			String ch = request.getPathInfo();
			ch=ch.substring(1);
			
			String id = ch.substring(ch.indexOf("/")+1);// /application/appId/25    DON'T PUT / AT LAST
			                                            // /application/eid/amit123      DON'T PUT / AT LAST
			if(ch.indexOf("/")!=-1){
				ch=ch.substring(0, ch.indexOf("/"));
			}
			
			if(("appId").equals(ch)){
				Application app = ApplicationDAO.getLeaveApplicationByAppId(Integer.parseInt(id));
				new ResponseObject().sendResponse("success","",app,response);
			}else if(("eid").equals(ch)){
				LinkedList<Application> res = ApplicationDAO.getLeaveApplicationByEid(id);
				new ResponseObject().sendResponse("success","",res,response);
			}
			
		}else{
			response.setStatus(403);
			new ResponseObject().sendResponse("error","Login Failed","",response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(LoginUtil.isLogin(request, response)){
			BufferedReader reader = request.getReader();
			Gson gson = new Gson();
			Application app = gson.fromJson(reader, Application.class);
			app.setEid(LoginUtil.getEid());			
			
			if(ApplicationDAO.checkAvailability(app)){
				int res = ApplicationDAO.insert(app);				
				JsonObject jo1 = new JsonObject();
				jo1.addProperty("application_id",res);
				
				new ResponseObject().sendResponse("success","Your "+app.getDays()+" days leave application has successfully submitted.",jo1,response);
			}else{
				response.setStatus(400);
				new ResponseObject().sendResponse("error","Already applied for leave in between these dates","",response);
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
			Application app = gson.fromJson(reader, Application.class);
			
			String appId = request.getPathInfo();
			appId=appId.substring(1);                       // send appId    /application/25
			if(appId.indexOf("/")!=-1){ 
				appId=appId.substring(0, appId.indexOf("/"));
			}		
			app.setApplication_id(Integer.parseInt(appId));
			
			if(FacultyDAO.facultyRank(LoginUtil.getDesignation())==2){
				ApplicationDAO.updateStatus(app);
				new ResponseObject().sendResponse("success","","",response);
			}else{
				response.setStatus(401);
				new ResponseObject().sendResponse("error","Unauthorized","",response);
			}
			
		}else{
			response.setStatus(403);
			new ResponseObject().sendResponse("error","Login Failed","",response);
		}
	}


}
