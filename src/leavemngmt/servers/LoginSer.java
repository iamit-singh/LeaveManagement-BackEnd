package leavemngmt.servers;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import leavemngmt.DAO.FacultyDAO;
import leavemngmt.beans.Faculty;
import leavemngmt.beans.ResponseObject;
import lmUtils.MyJWT;

@WebServlet("/auth/login/")
public class LoginSer extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		
		BufferedReader reader = request.getReader();
		Gson gson = new Gson();
		Faculty f = gson.fromJson(reader, Faculty.class);
		f = FacultyDAO.validateUser(f.getEid(), f.getPassword());
		if(f!=null){
			String jwt = MyJWT.getJWT(f.getEid(), f.getDesignation());
			
			JsonObject jo1 = new JsonObject();
			jo1.addProperty("token",jwt);
			jo1.addProperty("designation",f.getDesignation());
			jo1.addProperty("fRank",FacultyDAO.facultyRank(f.getDesignation()));
			new ResponseObject().sendResponse("success","",jo1,response);
			
		}else{
			response.setStatus(401);
			new ResponseObject().sendResponse("error","Invalid username or password","",response);
		}
		
	}
}
