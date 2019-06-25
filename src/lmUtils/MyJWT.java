package lmUtils;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
public class MyJWT {	
public static String mySecKey = "thisissecret&2#5!8";
public static int expInMillis = 24*60*60*1000;
public static String getJWT(String eid, String designation){
	String jwt=null;
	try {
				jwt = Jwts.builder()
				  .setSubject("MyRestSubject")
				  .setExpiration(new Date(System.currentTimeMillis()+expInMillis))
				  .claim("eid", eid)
				  .claim("designation", designation)
				  .signWith(
				    SignatureAlgorithm.HS256,
				    mySecKey.getBytes("UTF-8")
				  )
				  .compact();		
	} catch (Exception e) {
		e.printStackTrace();
	}
	return jwt;
}
public static String getClaimByName(String name, String jwt){
	String res = null;
	try {
		Jws<Claims> claims = Jwts.parser().setSigningKey(mySecKey.getBytes("UTF-8")).parseClaimsJws(jwt);
		if(claims.getBody().getExpiration().getTime()>new Date().getTime()){
			res = (String)claims.getBody().get(name);	
		}
	} catch (Exception e) {
		//e.printStackTrace();
	}
	return res;
}
}