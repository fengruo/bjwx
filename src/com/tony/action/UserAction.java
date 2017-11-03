package com.tony.action;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.tony.database.ConnData;
import com.tony.method.UserActionService;
import com.tony.method.UsualStrMethod;

/*
 * 用户处理类，用户注册，修改，查询
 * author:Tony.Lee
 * */
public class UserAction extends ActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UsualStrMethod usualStrMethod;
	private UserActionService userActionService;
	private InputStream textStream;
	private String disposition;
	//==========================================================================
	
	//:1--------用户登陆----
	public String login(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String email=usualStrMethod.checkRequestStr(request, "email");
		String password=usualStrMethod.checkRequestStr(request, "password");
		String[] ss=userActionService.loginImpl(email, usualStrMethod.encryptMD5(password),usualStrMethod.getIpAddr(request));
		if(ss[0].equals("0")){
			request.setAttribute("message","登陆失败，用户名或密码错误");
			return "logout";
		}else{
			if("2".equals(ss[3])){//2离职，1在职，3冻结
				request.setAttribute("message","对不起，用户 "+ss[1]+" 已离职！");
				return "logout";
			}else if("3".equals(ss[3])){
				request.setAttribute("message","对不起，用户 "+ss[1]+" 已冻结！");
				return "logout";
			}else{
				HttpSession session=request.getSession();
				session.setAttribute("userid", ss[0]);
				session.setAttribute("username", ss[1]);
				session.setAttribute("userType", ss[2]);
				return "login";
			}
		}
	}
	
	//===CMS用记登录 ==================
	public String cmslogin(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String zuser=usualStrMethod.checkRequestStr(request, "zuser");
		String zpass=usualStrMethod.checkRequestStr(request, "zpass");
		String back=userActionService.cmsloginImpl(zuser,zpass);
		if("".equals(back)){
			request.setAttribute("message", "对不起，帐号密码不对，请重新登录！");
			return "cmslogout";
		}else{
			request.getSession().setAttribute("cmsuserid", back);
			return "cmslogin";
		}
	}
	
	//=========================================================================
	public UsualStrMethod getUsualStrMethod() {
		return usualStrMethod;
	}
	public void setUsualStrMethod(UsualStrMethod usualStrMethod) {
		this.usualStrMethod = usualStrMethod;
	}

	public UserActionService getUserActionService() {
		return userActionService;
	}
	public void setUserActionService(UserActionService userActionService) {
		this.userActionService = userActionService;
	}
	public InputStream getTextStream() {
		return textStream;
	}
	public void setTextStream(InputStream textStream) {
		this.textStream = textStream;
	}

	public String getDisposition() {
		return disposition;
	}
	public void setDisposition(String disposition) {
		this.disposition = disposition;
	}
}
