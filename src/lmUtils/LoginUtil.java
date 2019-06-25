package lmUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import leavemngmt.DAO.FacultyDAO;

public class LoginUtil {
	private static String jwt;
	private static String eid;
	private static String designation;
	
	public static boolean isLogin(HttpServletRequest request, HttpServletResponse response){
		updateUserInfo(request, response);
		boolean res=false;
		res = (jwt!=null && eid!=null);
		return res;
	}
	
	public static String getEid(){
		return eid;
	}
	
	public static String getDesignation(){
		return designation;
	}
	
	private static void updateUserInfo(HttpServletRequest request, HttpServletResponse response){
		String auth = request.getHeader("Authorization");
		if(auth==null || "".equalsIgnoreCase(auth) || !auth.startsWith("Bearer ")){
			jwt = null;
			eid = null;
			designation =null;
		}else{
			jwt = auth.substring("Bearer".length()).trim();
			eid = MyJWT.getClaimByName("eid", jwt);
			designation = MyJWT.getClaimByName("designation", jwt);
		}
	}
}
