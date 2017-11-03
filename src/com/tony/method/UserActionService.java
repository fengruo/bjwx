
package com.tony.method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface UserActionService {
	public String[] loginImpl(String user,String zpass,String ipaddress);
	public String cmsloginImpl(String zuser,String zpass);
}
