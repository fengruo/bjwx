package com.tony.impl;

import java.util.List;

import com.tony.database.ConnData;
import com.tony.method.UserWechatActionService;
import com.tony.method.UsualStrMethod;
import com.tony.wechat.pojo.SNSUserInfo;

public class UserWechatActionServiceImpl implements UserWechatActionService {
	private ConnData connData;
	private UsualStrMethod usualStrMethod;
	
	//==================================================

	public int checkUserHas(String openid) {
		int flag=0;
		try{
			String sql="select count(*) from tbwechatuser where openid='"+openid+"'";
			//System.out.println(sql);
			flag=connData.findOneResult(sql);
		}catch(Exception e){
			e.printStackTrace();
		}
		return flag;
	}
	public String addUser(SNSUserInfo snsUserInfo) {
		String openid="";
		try{
			int checkFlag=0;
			int addFlag=0;
			checkFlag=checkUserHas(snsUserInfo.getOpenId());
			if(checkFlag==1){
				openid=snsUserInfo.getOpenId();
			}else{
				String sql="insert into tbwechatuser(openid,nickname,sex,country,province,city,imgurl) value(" +
						"'" +snsUserInfo.getOpenId()+"',"+
						"'" +usualStrMethod.replaceStringByData(snsUserInfo.getNickname())+"',"+
						"" +snsUserInfo.getSex()+","+
						"'" +snsUserInfo.getCountry()+"',"+
						"'" +snsUserInfo.getProvince()+"',"+
						"'" +snsUserInfo.getCity()+"',"+
						"'" +snsUserInfo.getHeadImgUrl()+
						"')";
				addFlag=connData.insertDataOne(sql);
				if(addFlag>0){
					openid=snsUserInfo.getOpenId();
				}else{
					openid="";
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return openid;
	}
	public boolean istimeLuckyimpl(String openId){
		boolean flag=false;
		String nowdate=usualStrMethod.getDateFormatAll();
		String sql="select zid,ztimes from tblucky where zstate=1 and '"+nowdate+"' between startdate and enddate";
		List<String[]> ls=connData.findResult(sql, 2);
		if(ls==null || ls.size()==0){
			flag=false;
		}else{
			String[] luckstring=ls.get(0);
			//查询当前用户抽奖次数，是否已经达到三次。返回    zid 轮次,zname 名称,zscript 解说,ztimes抽奖次数
			String sql2="select count(*) from tbluckyget where getzuser='"+openId+"' and luckyid="+luckstring[0];
			int lucktimes=connData.findOneResult(sql2);
			if((Integer.parseInt(luckstring[1])-lucktimes)==0){
				//此用户已经没有抽奖次数
				flag=false;
			}else{
				flag=true;
			}			
		}
		return flag;
	}
	//==================================================
	public ConnData getConnData() {
		return connData;
	}
	public void setConnData(ConnData connData) {
		this.connData = connData;
	}
	public UsualStrMethod getUsualStrMethod() {
		return usualStrMethod;
	}
	public void setUsualStrMethod(UsualStrMethod usualStrMethod) {
		this.usualStrMethod = usualStrMethod;
	}
}
