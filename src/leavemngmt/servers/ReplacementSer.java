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
import leavemngmt.DAO.ReplacementDAO;
import leavemngmt.beans.Faculty;
import leavemngmt.beans.Replacement;
import leavemngmt.beans.ResponseObject;
import lmUtils.LoginUtil;

@WebServlet("/replacement/*")

public class ReplacementSer extends HttpServlet{
		
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		if(LoginUtil.isLogin(request, response)){
			
			String appId = request.getPathInfo();
			appId=appId.substring(1);                       // send appId
			if(appId.indexOf("/")!=-1){ 
				appId=appId.substring(0, appId.indexOf("/"));
			}
			LinkedList<Replacement> res = ReplacementDAO.getAllReplacements(appId);
			
			new ResponseObject().sendResponse("success","",res,response);
		}else{
			response.setStatus(403);
			new ResponseObject().sendResponse("error","Login Failed","",response);
		}
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		if(LoginUtil.isLogin(request, response)){
			BufferedReader reader = request.getReader();
			Gson gson = new Gson();
			
			Replacement rep = gson.fromJson(reader, Replacement.class);
			
			if(ReplacementDAO.checkAvailability(rep)){
				ReplacementDAO.insert(rep);
				Faculty f = FacultyDAO.getFaculty(rep.getSurrogate_eid());
				String str = "Submitted "+ f.getFname() + " " + f.getLname() + ", For "+rep.getDays()+" days, between " + rep.getFrom_date() +" and "+ rep.getTo_date()+".";
				new ResponseObject().sendResponse("success",str,"",response);
			}else{
				response.setStatus(400);
				new ResponseObject().sendResponse("error","Already submitted replacement in between these dates","",response);
			}
			
			
		}else{
			response.setStatus(403);
			new ResponseObject().sendResponse("error","Login Failed","",response);
		}
		
	}
}