package com.tony.impl;

import java.util.ArrayList;
import java.util.List;

import com.tony.database.ConnData;
import com.tony.method.CoucherWechatActionService;
import com.tony.method.UsualStrMethod;

public class CoucherWechatActionServiceImpl implements
		CoucherWechatActionService {
	private ConnData connData;
	private UsualStrMethod usualStrMethod;
	//==================================================
	
	//获取用户openid  代金券金额       当前日期        
	public String addCoucherOne(String openid) {
		
		
		
		return null;
	}
	//判断当前时间是否有代金券发放,用户是否满足领取条件
	public String[] voucherIstime(String openid) {
		String nowdate=usualStrMethod.getDateFormatAll();
		String sql="select zid,zname,zcontent,maxmoney,minmoney,zcount,zstyle,zimage,usewhere from tbvoucher where zstate=1 and '"+nowdate+"' between startdate and enddate";
		List<String[]> ls=connData.findResult(sql, 9);
		if(ls.size()==0){
			return null;//没有代金券发放
		}else{
			String[] strings=ls.get(0);//获取代金券设置信息
			//获取用户领取代金券信息
			String[] stringCoucher=coucherUserIshave(Integer.parseInt(strings[0]),openid,strings[8]);
			//用户未领取过当前轮次的代金券
			if(stringCoucher==null||stringCoucher.equals(null)){
				//判断是否有余量
				if(coucherIshava(Integer.parseInt(strings[0]), Integer.parseInt(strings[5]))==1){
						//组装代金券信息
					//INSERT INTO `bjwx`.`tbvoucherget` `zid`, `voucherid`, `openid`, `getdate`, `zstate`, `zmoney`
					//代金券
					String[] addstringCoucher={"","","","",""};
					addstringCoucher[0]=strings[0];//代金券轮次
					addstringCoucher[1]=openid;//代金券领取人openid
					addstringCoucher[2]=usualStrMethod.getDateFormatAll();//代金券获取日期
					addstringCoucher[3]=(int)usualStrMethod.getRandom(Integer.parseInt(strings[4]), Integer.parseInt(strings[3]))+"";//代金券金额
					int flag=addCoucherOne(addstringCoucher);
					if(flag>0){
						stringCoucher=coucherUserIshave(Integer.parseInt(strings[0]),openid,strings[8]);
						return stringCoucher;
					}else{
						return null;//没有代金券发放
					}
				}else{
						//代金券发放完毕
					String[] strinsnohava={""};
					strinsnohava[0]="nohave";
					return strinsnohava;
				}
			}else{
				//用户已经领取过当前批次的代金券
				String[] strinsishava={""};
				strinsishava[0]="ishave";
				return strinsishava;
			}
		}
	}
	//判断当前轮次代金券是否还有余量
	public int coucherIshava(int voucherid,int zcount) {
		//INSERT INTO `bjwx`.`tbvoucherget` (`zid`, `voucherid`, `openid`, `getdate`, `zstate`, `zmoney`
		String sql="select count(*) from tbvoucherget where voucherid="+voucherid;
		int count=connData.findOneResult(sql);
		if(zcount>count){
			return 1;
		}else{
			return 0;
		}
	}
	//判断当前用户是否已领过当前轮次代金券
	public String[] coucherUserIshave(int voucherid,String openid,String usewhere) {
				String sql="select zid,getdate,zstate,zmoney,voucherid,'"+usewhere+"' from tbvoucherget where voucherid="+voucherid+" and openid='"+openid+"'";
				List<String[]> ls=connData.findResult(sql,6);
				if(ls.size()==0){
					return null;
				}else{
					String[] stringCoucher=ls.get(0);
					return stringCoucher;
				}
				
	}
	//插入代金券信息
	public int addCoucherOne(String[] strings) {
		String sql="INSERT INTO `bjwx`.`tbvoucherget` (`voucherid`, `openid`, `getdate`, `zmoney`) VALUES ("+Integer.parseInt(strings[0])+",'"+strings[1]+"','"+strings[2]+"',"+Integer.parseInt(strings[3])+")";
		int flag=connData.insertDataOne(sql);
		return flag;
	}
	//根据代金券活动id查询代金券活动信息
	public String[] coucherInfo(int coucherid) {
		//INSERT INTO `bjwx`.`tbvoucher` (`zid`, `startdate`, `enddate`, `zname`, `zcontent`, `maxmoney`, `minmoney`, `zcount`, `zstyle`, `zimage`)
		String sql="select zname,zcontent,zimage,zstyle from tbvoucher where zid="+coucherid;
		List<String[]> ls=connData.findResult(sql, 4);
		if(ls.size()>0){
			String[] strings = ls.get(0);
			return strings;
		}
		return null;
	}
	//使用信息录入
	public String coucherUse(int useid, String shopid, String ticknum,String zmoney,int voucherInfo_style,String usepass) {
		List list=connData.findResult("select zid from tbvoucher where usepass='"+usepass+"'", 1);
		if(list.size()>0){
			String sql="INSERT INTO `bjwx`.`tbvoucheruse` ( `useid`, `usedate`, `shopid`, `ticknum`, `zmoney`) VALUES ("+useid+", '"+usualStrMethod.getDateFormatAll()+"', '"+shopid+"', '"+ticknum+"', '"+zmoney+"')";
			int flag=connData.insertDataOne(sql);
			if(voucherInfo_style!=1){
				String sql2="UPDATE tbvoucherget SET zstate=2 WHERE zid='"+useid+"'";
				connData.insertDataOne(sql2);
			}
			if(flag>-1){
				return "ok";
			}
		}else{
			return "1";
		}
		return null;
	}
	//根据openid查询代金券list
	public List<String[]> queryMyCoucherList(String openid) {
		String sql="select tbvoucherget.zid,voucherid,getdate,tbvoucherget.zstate,zmoney from tbvoucher,tbvoucherget where tbvoucher.zid=tbvoucherget.voucherid and tbvoucher.zstate=1 and openid='"+openid+"' and datediff(now(),getdate)<31 and now() between startdate and enddate";
		List<String[]> ls=connData.findResult(sql, 5);
		return ls;
	}
	//根据代金券id查询代金券one
	public String[] queryMyCoucherOne(int zid) {
		String sql="select tbvoucherget.zid,tbvoucherget.getdate,tbvoucherget.zstate,tbvoucherget.zmoney," +
				"tbvoucherget.voucherid,tbvoucher.usewhere FROM tbvoucherget,tbvoucher WHERE tbvoucherget.voucherid=tbvoucher.zid AND tbvoucherget.zid="+zid;
		List<String[]> ls=connData.findResult(sql, 6);
		String[] strings=ls.get(0);
		return strings;
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
