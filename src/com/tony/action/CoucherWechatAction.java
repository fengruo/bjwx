package com.tony.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.registry.infomodel.User;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.tony.method.CoucherWechatActionService;
import com.tony.method.UsualStrMethod;

public class CoucherWechatAction extends ActionSupport{

	/**
	 * 微信用户代金券领取
	 */
	private static final long serialVersionUID = 1L;
	private UsualStrMethod usualStrMethod;
	private CoucherWechatActionService coucherWechatActionService;
	private InputStream textStream;
	private String[] stringVoucher;
	//=====================================================
	//判断当前日期是否在活动开放时间,生成代金券信息
	public String checkCoucherIshave(){
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		String openid=session.getAttribute("openid").toString();
		stringVoucher=coucherWechatActionService.voucherIstime(openid);
		if(stringVoucher==null||stringVoucher.equals(null)){
			return "CoucherNotime";//代金券活动没开始
		}else if(stringVoucher[0]=="nohave"||stringVoucher[0].equals("nohave")){
			return "CoucherNohave";//代金券发完了
		}else if(stringVoucher[0]=="ishave"||stringVoucher[0].equals("ishave")){
			return "Coucherishave";//代金券发完了
		}else{
			return "CoucherShow";//代金券领取展示
		}
	}
	//根据代金券活动id查询代金券信息
	public String coucherInfo(){
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			int voucherid=Integer.parseInt(usualStrMethod.checkRequestStr(request, "voucherid"));
			String[] voucherstrings=coucherWechatActionService.coucherInfo(voucherid);
			JSONArray json=JSONArray.fromObject(voucherstrings);
			//System.out.println(voucherstrings);
			textStream=new ByteArrayInputStream(json.toString().getBytes("utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	//插入使用记录
	public String addUserInfo(){
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			int useid=Integer.parseInt(usualStrMethod.checkRequestStr(request, "useid"));
			String shopid=usualStrMethod.crsSql(request, "shopid");
			String ticknum=usualStrMethod.crsSql(request, "ticknum");
			String zmoney=usualStrMethod.crsSql(request, "zmoney");
			String usepass=usualStrMethod.crsSql(request, "usepass");
			int voucherInfo_style=Integer.parseInt(usualStrMethod.checkRequestStr(request, "voucherInfo_style"));
			String json=coucherWechatActionService.coucherUse(useid, shopid, ticknum, zmoney, voucherInfo_style,usepass);
			textStream=new ByteArrayInputStream(json.getBytes("utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	
	//根据openid查询我的代金券
	public String queryMyCoucherList(){
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpSession session=request.getSession();
			String openid=session.getAttribute("openid").toString();
			List<String[]> strings=coucherWechatActionService.queryMyCoucherList(openid);
			if(strings.size()==0){
				String flag="";
				textStream=new ByteArrayInputStream(flag.getBytes("utf-8"));
			}else{
				JSONArray json = JSONArray.fromObject(strings);
				//System.out.println(voucherstrings);
				textStream=new ByteArrayInputStream(json.toString().getBytes("utf-8"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	//根据代金券id查询代金券详情
	public String queryMyCoucherOne(){
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			int zid=Integer.parseInt(usualStrMethod.checkRequestStr(request, "zid"));
			String[] strings=coucherWechatActionService.queryMyCoucherOne(zid);
			stringVoucher=strings;
			//System.out.println(voucherstrings);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "CoucherShow";
	}
	//=====================================================
	public UsualStrMethod getUsualStrMethod() {
		return usualStrMethod;
	}
	public void setUsualStrMethod(UsualStrMethod usualStrMethod) {
		this.usualStrMethod = usualStrMethod;
	}
	public InputStream getTextStream() {
		return textStream;
	}
	public void setTextStream(InputStream textStream) {
		this.textStream = textStream;
	}
	public CoucherWechatActionService getCoucherWechatActionService() {
		return coucherWechatActionService;
	}
	public void setCoucherWechatActionService(
			CoucherWechatActionService coucherWechatActionService) {
		this.coucherWechatActionService = coucherWechatActionService;
	}


	public String[] getStringVoucher() {
		return stringVoucher;
	}


	public void setStringVoucher(String[] stringVoucher) {
		this.stringVoucher = stringVoucher;
	}
	
}
