package leavemngmt.servers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import leavemngmt.DAO.FacultyDAO;
import leavemngmt.beans.Faculty;
import leavemngmt.beans.ResponseObject;
import lmUtils.LoginUtil;

@WebServlet("/profile/*")
public class ProfileSer extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(LoginUtil.isLogin(request, response)){	
			
			String eid = request.getPathInfo();
			eid =eid.substring(1);  
			
//			String dep=eid.substring(eid.indexOf("/")+1);	
																			
			if(eid.indexOf("/")!=-1){ 
				eid =eid.substring(0, eid.indexOf("/"));				// send eid to get profile
			}															// send /profile/all to get all
																		// faculty of department or Ad Staff
			
			if("all".equals(eid)){
//				if(dep.indexOf("/")!=-1){ 
//					dep=dep.substring(0, eid.indexOf("/"));             // send /profile/allFaculty/departmentName to get
//				}														//all faculty of that department
//				LinkedList<Faculty> res = FacultyDAO.getFacultyByDepartment("Faculty",eid); 
//				new ResponseObject().sendResponse("success","",res,response);
//			}else if("allAdStaff".equals(eid)){
//				LinkedList<Faculty> res = FacultyDAO.getFacultyByDepartment("Administrative Staff",""); 
//				new ResponseObject().sendResponse("success","",res,response);    //send /profile/allAdStaff
				
				LinkedList<Faculty> res = FacultyDAO.getFacultyByDepartment(LoginUtil.getEid());
				new ResponseObject().sendResponse("success","",res,response);
				
				
			}else{
				if(FacultyDAO.facultyRank(LoginUtil.getDesignation())==2 || eid.equals(LoginUtil.getEid())){
					Faculty f = FacultyDAO.getFaculty(eid);
					new ResponseObject().sendResponse("success","",f,response);
				}else{
					response.setStatus(401);
					new ResponseObject().sendResponse("error","Unauthorized","",response);
				}
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
			Faculty f = gson.fromJson(reader, Faculty.class);
			f.setEid(LoginUtil.getEid());
			FacultyDAO.update(f);
			
			new ResponseObject().sendResponse("success","","",response);
		}else{
			response.setStatus(403);
			new ResponseObject().sendResponse("error","Login Failed","",response);
		}
	}

}
