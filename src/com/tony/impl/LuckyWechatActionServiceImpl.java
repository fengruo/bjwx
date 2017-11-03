package com.tony.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionContext;
import com.tony.database.ConnData;
import com.tony.method.LuckyWechatActionService;
import com.tony.method.UsualStrMethod;

public class LuckyWechatActionServiceImpl implements LuckyWechatActionService {
	private ConnData connData;
	private UsualStrMethod usualStrMethod;
	public static int LUCKGETID;
	private String notime=" 00:00:01";
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
	//===========================================================
	//检查目前是否符合抽奖条件
	public String[] checkLuck(String openid) {
			String nowdate=usualStrMethod.getDateFormatAll();
			String sql="select zid,zname,zscript,ztimes,ztype,zremark  from tblucky where zstate=1 and '"+nowdate+"' between startdate and enddate";
			List<String[]> ls=connData.findResult(sql, 6);
			if(ls.size()==0){
				return null;//没有符合条件的抽奖活动
			}else{
				//有符合条件的抽奖活动
				String[] luckstring=ls.get(0);
				//查询当前用户抽奖次数，是否已经达到三次。返回    zid 轮次,zname 名称,zscript 解说,ztimes抽奖次数
				int lucktimes=queryLuckuser(openid,luckstring[0],luckstring[4]);
				if((Integer.parseInt(luckstring[3])-lucktimes)<=0){
					//此用户已经没有抽奖次数
					return null;
				}else{
					//此用户可以抽奖 可抽奖次数为luckstring[3] -  lucktimes
					//string[]   
					String[] luckinfo={luckstring[0],luckstring[1],luckstring[2],luckstring[3],(Integer.parseInt(luckstring[3])-lucktimes)+"",luckstring[4],luckstring[5]};
					return luckinfo;
				}
			}
	}
	//zid,zname,zscript,ztimes,ztype,luckystart,luckyend
	/*private int pdsfncj(String openid,String[] luckstring){
		int times=0;
		String sql="";
		if("2".equals(luckstring[4])){  //每天可以抽多少次
			String now=usualStrMethod.getDateFormat("yyyy-MM-dd");
			sql="select count(*) from tbluckyget where getzuser='"+openid+"' and luckyid="+luckstring[0]+" and getztime like '"+now+"%'  ";
		}else{
			sql="select count(*) from tbluckyget where getzuser='"+openid+"' and luckyid="+luckstring[0];
		}
		
		int flag=connData.findOneResult(sql);
		return times;
	}*/
	private int queryLuckuser(String openid,String luckyid,String ztype) {
		// TODO Auto-generated method stub
		/*String sql="select count(*) from tbluckyget where getzuser='"+openid+"' and luckyid="+luckyid;
		int flag=connData.findOneResult(sql);*/
		String sql="";
		if("2".equals(ztype)){  //每天可以抽多少次
			String now=usualStrMethod.getDateFormat("yyyy-MM-dd");
			sql="select count(*) from tbluckyget where getzuser='"+openid+"' and luckyid="+luckyid+" and getztime like '"+now+"%'  ";
		}else{
			sql="select count(*) from tbluckyget where getzuser='"+openid+"' and luckyid="+luckyid;
		}
		int flag=connData.findOneResult(sql);
		return flag;
	}
	public List<String[]> queryLuckyprize(int luckyid) {
		//返回奖品信息
		//INSERT INTO `bjwx`.`tbluckyprize` (`zid`, `luckyid`, `prizename`, `prizeimg`, `prizemoney`, `percent`, `znum`)
		String sql="select zid,prizename,percent,znum from tbluckyprize where luckyid="+luckyid;
		List<String[]> ls=connData.findResult(sql, 4);
		if(ls.size()==0){
			return null;
		}else{
			return ls;
		}
	}
	@SuppressWarnings("null")
	public String[] queryLuckyprizeOne(String openid,int luckyid) {
		//用户点击抽奖      给出抽奖信息返回  并重定向到 奖品详细信息页面，详细页面展示页，底部核销        右侧浮动到重新抽奖
		//查询抽奖奖品信息 奖品id和概率
		List<String[]> luckyInfos=queryLuckyprize(luckyid);
		//将奖品id和概率配对为二维数组
		String[][] luckyInfo=new String[3][luckyInfos.size()];
		for(int i=0;i<luckyInfos.size();i++){
			String[] strings=luckyInfos.get(i);
			luckyInfo[0][i]=strings[0];//奖品id
			luckyInfo[1][i]=strings[2];//奖品概率
			luckyInfo[2][i]=strings[3];//奖品数量
		}
		//奖品id
		int[] luckyPrizeId=luckyLuck(luckyInfo);
		//插入抽奖信息表  
		//INSERT INTO `bjwx`.`tbluckyget` (`zid`, `luckyid`, `prizeid`, `getztime`, `getzuser`, `exchangetime`, `shopid`, `zremark`) 
		if(addLuck(luckyid, luckyPrizeId[0], openid)){
			return queryOneLuck(luckyPrizeId);
		}
		return null;
	}
	public boolean checkLuckyIshave(int luckpriceid,int znum) {
		//查询奖品是否还有剩余
		String sql="select count(*) from tbluckyget where prizeid="+luckpriceid;
		int flag=connData.findOneResult(sql);
		if(flag<znum){
			return true;
		}
		return false;
	}
	@SuppressWarnings("null")
	public int[] luckyLuck(String[][] luckyInfo) {
		//开始抽奖  返回奖品id
		/*
		 *luckyInfo[0][i]=strings[0];//奖品id
		 *luckyInfo[1][i]=strings[2];//奖品概率
		 *luckyInfo[2][i]=strings[3];//奖品数量
		 * 
		 */
		double[] luck_item=new double[luckyInfo[1].length];
		for(int i=0;i<luckyInfo[1].length;i++){
			luck_item[i]=Double.parseDouble(luckyInfo[1][i]);
		}
		double sum=0;
		int flag=0;
		double side=(Math.random());
		for(int i=0;i<luck_item.length;i++){
			sum=sum+luck_item[i];
			if(sum>=side){
				flag=i;
				break;
			}
		}
		if(checkLuckyIshave(Integer.parseInt(luckyInfo[0][flag]),Integer.parseInt(luckyInfo[2][flag]))){
			int[] num={Integer.parseInt(luckyInfo[0][flag]),flag};
			return num;
		}else{
			return luckyLuck(luckyInfo);
		}
		// flag 即为奖品数组下标 System.out.print(flag+"*");
	}
	public boolean addLuck(int luckyid, int luckpriceid, String openid) {
		//插入抽奖信息
		String nowdate=usualStrMethod.getDateFormatAll();
		int rs=connData.findOneResult("select istrue from tbluckyprize where zid="+luckpriceid);
		String sql="";
		if(1==rs){
			sql="INSERT INTO tbluckyget (luckyid,prizeid,getztime,getzuser,zstate) VALUES ("+luckyid+","+luckpriceid+",'"+nowdate+"','"+openid+"',1)";			
		}else{
			sql="INSERT INTO tbluckyget (luckyid,prizeid,getztime,getzuser,zstate) VALUES ("+luckyid+","+luckpriceid+",'"+nowdate+"','"+openid+"',3)";
		}
		int flag=connData.insertDataOne(sql);
		LUCKGETID=queryLuckyGetId(openid, nowdate, luckyid);
		return true;		
	}
	public String[] queryOneLuck(int[] luckyprizeid) {
		//查询出该奖品信息，跳转页面用
		//INSERT INTO `bjwx`.`tbluckyprize` (`zid`, `luckyid`, `prizename`, `prizeimg`, `prizemoney`, `percent`, `znum`) 
		String sql="select zid,prizename,prizeimg,prizemoney,"+luckyprizeid[1]+" from tbluckyprize where zid="+luckyprizeid[0];
		//System.out.println(sql);
		List<String[]> ls=connData.findResult(sql, 5);
		return ls.get(0);
	}
	//插入中奖信息时候返回该中奖信息id
	public int queryLuckyGetId(String openid, String nowdate,int luckyid) {
		String sql="select zid from tbluckyget where luckyid="+luckyid+" and getzuser='"+openid+"' and getztime='"+nowdate+"'";
		//System.out.println(sql);
		List<String[]> ls=connData.findResult(sql, 1);
		String[] str=ls.get(0);
		return Integer.parseInt(str[0]);
	}
	public List<String[]> queryMyLucky(String openid) {
		//先得处理我的奖品
		Map application=ActionContext.getContext().getApplication();
		String temptime=usualStrMethod.isNull(application.get("notime"));
		if(!"".equals(temptime)){
			notime=" "+temptime;
		}
		String now=usualStrMethod.getDateFormat("yyyy-MM-dd");
		String zids="";
		List<String[]> tt=connData.findResult("SELECT A.zid FROM tbluckyget A,tblucky B WHERE A.luckyid=B.zid AND B.ztype=2 AND A.zstate=1 "
				+" AND A.getzuser='"+openid+"' AND (A.exchangetime='' OR A.exchangetime IS NULL)  AND A.getztime<'"+now+notime+"'", 1);
		for(String[] ss:tt){
			zids=zids+ss[0]+",";
		}
		if(!"".equals(zids)){
			zids=usualStrMethod.getDHstr(zids);
			String mql="UPDATE tbluckyget SET zstate=0 WHERE zid IN ("+zids+")";
			connData.insertDataOne(mql);
		}
		//根据openid查询奖品list
			//String sql="select tbluckyget.zid,prizeid,prizename,getztime,shopid,tbluckyget.zstate from tblucky,tbluckyget,tbluckyprize where tblucky.zid=tbluckyget.luckyid and tblucky.zstate=1 and tbluckyprize.zid=tbluckyget.prizeid and getzuser='"+openid+"' and datediff(now(),getztime)<31";
		String sql="select tbluckyget.zid,prizeid,prizename,getztime,shopid,tbluckyget.zstate from tblucky,tbluckyget,tbluckyprize where tblucky.zid=tbluckyget.luckyid and tblucky.zstate=1 and tbluckyprize.zid=tbluckyget.prizeid and getzuser='"
				+openid+"' and tbluckyget.zstate<>3 AND '"+usualStrMethod.getDateFormat("yyyy-MM-dd HH:mm:ss")+"' BETWEEN tblucky.luckystart AND tblucky.luckyend";
			List<String[]> ls=connData.findResult(sql, 6);
			return ls;
	}
	public String[] queryLuckyprizeOne(int luckprizeid) {
		String sql="select prizename,prizeimg from tbluckyprize where zid="+luckprizeid;
		List<String[]> ls=connData.findResult(sql, 2);
		return ls.get(0);
	}
	//奖品兑奖成功
	public boolean userLuck(int jpgetid, String shopid, String zremark,String usepass) {
		int flag=-1;
		String now=usualStrMethod.getDateFormat("yyyy-MM-dd HH:mm:ss");
		List lt=connData.findResult("SELECT zid FROM tblucky WHERE usepass='"+usepass+"' and zid=(select luckyid from tbluckyget where zid="+jpgetid+") AND zstate=1 AND '"+now+"' BETWEEN luckystart AND luckyend", 1);
		if(lt.size()>0){
			String sql = "update tbluckyget set shopid='"+shopid+"',zremark='"+zremark+"',exchangetime='"+usualStrMethod.getDateFormatAll()+"',zstate=2 where zid="+jpgetid;
			flag=connData.insertDataOne(sql);
		}
		if(flag>-1){
			return true;
		}else{
			return false;
		}
	}
	//查询当前奖品截至日期
	public String endLuckDate(){
		String nowdate=usualStrMethod.getDateFormatAll();
		String sql="select luckyend  from tblucky where zstate=1 and '"+nowdate+"' between startdate and enddate";
		List<String[]> ls=connData.findResult(sql, 1);
		String[] endtime=ls.get(0);
		return endtime[0];
	}
}
