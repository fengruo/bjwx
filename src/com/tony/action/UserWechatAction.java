package com.tony.action;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.tony.impl.LuckyWechatActionServiceImpl;
import com.tony.method.UserWechatActionService;
import com.tony.method.UsualStrMethod;
import com.tony.wechat.pojo.WeixinOauth2Token;
import com.tony.wechat.utils.AdvancedUtil;

public class UserWechatAction extends ActionSupport{

	/**
	 * 微信用户信息录入
	 */
	private static final long serialVersionUID = 1L;
	private UsualStrMethod usualStrMethod;
	private UserWechatActionService userWechatActionService;
	private InputStream textStream;
	//==========================百家APPID:wx89852f794d41fc27===========================
	private String APPID="wx5b347fe4bd7a2398";
	private String APPSECRET="fb0096140d503f8d014f671d706156b7";
	
	//用户获取网页授权信息
	public String getwechatuserinfo(){
		try{
			HttpServletRequest request = ServletActionContext.getRequest();
			String code = request.getParameter("code");
			int state =Integer.parseInt(request.getParameter("state"));
			System.out.println(code);
			// 用户同意授权
			if (!"authdeny".equals(code)) {
				// 获取网页授权access_token,应用的appid和appsecret
				WeixinOauth2Token weixinOauth2Token = AdvancedUtil.getOauth2AccessToken(APPID, APPSECRET, code);
				// 网页授权接口访问凭证
				String accessToken = weixinOauth2Token.getAccessToken();
				// 用户标识
				String openId = weixinOauth2Token.getOpenId();
				/* 获取用户信息
				SNSUserInfo snsUserInfo = AdvancedUtil.getSNSUserInfo(accessToken,openId);
				插入用户记录
				String addOpenid=userWechatActionService.addUser(snsUserInfo);
				if(addOpenid==""||addOpenid.equals("")){
					return "Oauth2AccessTokenFalse";
				}*/
				HttpSession session=request.getSession();
				session.setAttribute("openid", openId);
				if(state==1){
					return "toGetVoucherSuccess";
				}
				if(state==2){
					return "toMyVoucherSuccess";
				}
				if(state==3){
					return "Oauth2AccessTokenSuccess";
				}
				if(state==4){
					//return "toLuckGetSuccess";
					//===判断是否有抽奖活动
					boolean flag=userWechatActionService.istimeLuckyimpl(openId);
					if(flag){
						return "yesLucky";
					}else{
						return "noLucky";
					}
				}
				if(state==5){
					return "toLuckMySuccess";
				}
				
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "Oauth2AccessTokenFalse";
	}
	
	public String flagWechatLuck(){
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		//测试
		//session.setAttribute("openid", "oFbUQ03SVA64c8OfOM0mynR_s8_4");
		String openid=session.getAttribute("openid").toString();
		boolean flag=userWechatActionService.istimeLuckyimpl(openid);
		if(flag){
			return "yesLucky";
		}else{
			return "noLucky";
		}
	}
	//=====================================================
	public UsualStrMethod getUsualStrMethod() {
		return usualStrMethod;
	}
	public void setUsualStrMethod(UsualStrMethod usualStrMethod) {
		this.usualStrMethod = usualStrMethod;
	}
	public UserWechatActionService getUserWechatActionService() {
		return userWechatActionService;
	}
	public void setUserWechatActionService(
			UserWechatActionService userWechatActionService) {
		this.userWechatActionService = userWechatActionService;
	}
	public InputStream getTextStream() {
		return textStream;
	}
	public void setTextStream(InputStream textStream) {
		this.textStream = textStream;
	}
}
