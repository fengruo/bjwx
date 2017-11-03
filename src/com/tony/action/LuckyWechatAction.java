package com.tony.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.tony.impl.LuckyWechatActionServiceImpl;
import com.tony.method.LuckyWechatActionService;
import com.tony.method.UsualStrMethod;

public class LuckyWechatAction extends ActionSupport{
	private UsualStrMethod usualStrMethod;
	private InputStream textStream;
	private LuckyWechatActionService luckyWechatActionService;
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
	public LuckyWechatActionService getLuckyWechatActionService() {
		return luckyWechatActionService;
	}
	public void setLuckyWechatActionService(
			LuckyWechatActionService luckyWechatActionService) {
		this.luckyWechatActionService = luckyWechatActionService;
	}
	//==================================================================
	//检查目前是否符合抽奖
	public String luckyIstime(){
		try{
			HttpServletRequest request=ServletActionContext.getRequest();
			HttpSession session=request.getSession();
			//测试
			String openid=session.getAttribute("openid").toString();
			//返回    zid 0轮次,zname 1名称,zscript 2解说,ztimes3抽奖次数
			String[] luckystrings=luckyWechatActionService.checkLuck(openid);
			if(luckystrings==null){
				String flag="";
				textStream=new ByteArrayInputStream(flag.getBytes("utf-8"));
			}else{
				JSONArray json=JSONArray.fromObject(luckystrings);
				textStream=new ByteArrayInputStream(json.toString().getBytes("utf-8"));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	public String luckyPrizeIshave(){
		try{
			HttpServletRequest request=ServletActionContext.getRequest();
			int luckyid=Integer.parseInt(usualStrMethod.checkRequestStr(request, "luckyid"));
			//返回    zid,prizename,percent,znum
			List<String[]> luckyPrizestrings=luckyWechatActionService.queryLuckyprize(luckyid);
			if(luckyPrizestrings.size()==0){
				String flag="";
				textStream=new ByteArrayInputStream(flag.getBytes("utf-8"));
			}else{
				JSONArray json=JSONArray.fromObject(luckyPrizestrings);
				//System.out.println(voucherstrings);
				textStream=new ByteArrayInputStream(json.toString().getBytes("utf-8"));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	public String luckyLuck(){
		try{
			HttpServletRequest request=ServletActionContext.getRequest();
			HttpSession session=request.getSession();
			String openid=session.getAttribute("openid").toString();
			int luckyid=Integer.parseInt(usualStrMethod.checkRequestStr(request, "luckyid"));
			//返回    zid,prizename,percent,znum
			String[] luckyPrizestring=luckyWechatActionService.queryLuckyprizeOne(openid, luckyid);
			String[] returnstring=new String[luckyPrizestring.length+1];
			for(int i=0;i<luckyPrizestring.length;i++){
				returnstring[i]=luckyPrizestring[i];
			}
			returnstring[luckyPrizestring.length]=LuckyWechatActionServiceImpl.LUCKGETID+"";
			System.out.println(LuckyWechatActionServiceImpl.LUCKGETID+"中奖id");
			//luckyPrizestring[luckyPrizestring.length]=LuckyWechatActionServiceImpl.LUCKGETID+"";
			if(luckyPrizestring.length==0){
				String flag="";
				textStream=new ByteArrayInputStream(flag.getBytes("utf-8"));
			}else{
				JSONArray json=JSONArray.fromObject(returnstring);
				//System.out.println(voucherstrings);
				textStream=new ByteArrayInputStream(json.toString().getBytes("utf-8"));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	//根据openid查询我的抽奖信息
		public String queryMyLuckList(){
			try {
				HttpServletRequest request = ServletActionContext.getRequest();
				HttpSession session=request.getSession();
				String openid=session.getAttribute("openid").toString();
				List<String[]> strings=luckyWechatActionService.queryMyLucky(openid);
				if(strings.size()==0){
					String flag="";
					textStream=new ByteArrayInputStream(flag.getBytes("utf-8"));
				}else{
					JSONArray json = JSONArray.fromObject(strings);
					textStream=new ByteArrayInputStream(json.toString().getBytes("utf-8"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return SUCCESS;
		}
		//根据luckyprizeid查询一条奖品信息
				public String queryMyLuckOne(){
					try {
						HttpServletRequest request = ServletActionContext.getRequest();
						int luckyprizeid=Integer.parseInt(usualStrMethod.checkRequestStr(request, "luckyprizeid"));
						String[] strings=luckyWechatActionService.queryLuckyprizeOne(luckyprizeid);
						if(strings.length==0){
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
				//核销奖品
				public String addUserInfo(){
					try{
						HttpServletRequest request = ServletActionContext.getRequest();
						int jpgetid=Integer.parseInt(usualStrMethod.checkRequestStr(request, "jpgetid"));
						String shopid=usualStrMethod.crsSql(request, "shopid");
						String zremark=usualStrMethod.crsSql(request, "zremark");
						String usepass=usualStrMethod.crsSql(request, "usepass");
						String info="";
						if(luckyWechatActionService.userLuck(jpgetid, shopid, zremark,usepass)){
							info="y";
						}else{
							info="n";
						}
						textStream=new ByteArrayInputStream(info.toString().getBytes("utf-8"));
					} catch (Exception e) {
						e.printStackTrace();
					}
					return SUCCESS;
				}
				public String endLuckDate(){				
					try{
						textStream=new ByteArrayInputStream(luckyWechatActionService.endLuckDate().toString().getBytes("utf-8"));
					} catch (Exception e) {
						e.printStackTrace();
					}
					return SUCCESS;
				}
				/*//测试抽奖概率
				public String testaction(){
					HttpServletRequest request = ServletActionContext.getRequest();
					String openid=usualStrMethod.checkRequestStr(request, "openid");
					String[] strings=luckyWechatActionService.queryLuckyprizeOne(openid,3);
					return null;
				}*/
}
