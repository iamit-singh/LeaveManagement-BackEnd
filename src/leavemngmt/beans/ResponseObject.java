package leavemngmt.beans;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;

public class ResponseObject {
	private String status;
	private String msg;
	private Object resp;

	public void sendResponse(String status, String msg, Object resp, HttpServletResponse response) throws IOException {
		this.status = status;
		this.msg = msg;
		this.resp = resp;
		Gson gson = new Gson();
		PrintWriter pw = response.getWriter();
		pw.println(gson.toJson(this));
	}
	
}
