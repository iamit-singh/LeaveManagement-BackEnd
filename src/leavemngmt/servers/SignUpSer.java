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
import leavemngmt.beans.Faculty;
import leavemngmt.beans.ResponseObject;

@WebServlet("/auth/signup/")
public class SignUpSer extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		BufferedReader reader = request.getReader();
		Gson gson = new Gson();
		Faculty f = gson.fromJson(reader, Faculty.class);
		
		if(!FacultyDAO.isUserAlready(f.getEid())){
			
			if(f.getDesignation().equals("Director")){
				String str = FacultyDAO.getDirector();
				if(str!=null){
					response.setStatus(400);
					new ResponseObject().sendResponse("error","Director already registered","",response);
					return;
				}
			}else if(f.getDesignation().equals("Registrar")){
				String str = FacultyDAO.getRegistrar();
				if(str!=null){
					response.setStatus(400);
					new ResponseObject().sendResponse("error","Registrar already registered","",response);
					return;
				}
			}else if("".equals(f.getDepartment()) && (f.getDesignation().equals("HOD") || f.getDesignation().equals("Faculty"))){
				response.setStatus(400);
				new ResponseObject().sendResponse("error","Department name required","",response);
				return;
			}else if(f.getDesignation().equals("HOD")){
				Faculty res = FacultyDAO.getHODByDepartment(f.getDepartment());
				if(res!=null){
					response.setStatus(400);
					new ResponseObject().sendResponse("error","HOD of "+ f.getDepartment() +" department already registered","",response);
					return;
				}
			}
			
			
			FacultyDAO.insert(f);
			if(FacultyDAO.facultyRank(f.getDesignation())==1){
				LeaveDAO.initialize(f.getEid());
			}
			new ResponseObject().sendResponse("success","You have successfully registered.","",response);
		}else{
			response.setStatus(400);
			new ResponseObject().sendResponse("error","EmployeeID Already Registered","",response);
		}
		
	}
}
