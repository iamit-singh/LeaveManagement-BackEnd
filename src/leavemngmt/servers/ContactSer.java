package leavemngmt.servers;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import leavemngmt.DAO.ContactDAO;
import leavemngmt.beans.Contact;
import leavemngmt.beans.ResponseObject;

@WebServlet("/contact/")
public class ContactSer extends HttpServlet {
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		BufferedReader reader = request.getReader();
		Gson gson = new Gson();
		
		Contact ct = gson.fromJson(reader, Contact.class);
		
		ContactDAO.insert(ct);
		
		new ResponseObject().sendResponse("success","Your form has submitted successfully.","",response);
	}

}
