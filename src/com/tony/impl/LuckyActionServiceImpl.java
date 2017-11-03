package com.tony.impl;

import com.tony.database.ConnData;
import com.tony.method.LuckyActionService;
import com.tony.method.UsualStrMethod;

public class LuckyActionServiceImpl implements LuckyActionService {
	private ConnData connData;
	private UsualStrMethod usualStrMethod;
	
	public String saveLuckyImpl(String zid,String startdate,String enddate,String zname,String zscript,String ztimes,String zstate,String luckystart,String luckyend,String ztype,String zremark,String usepass){
		String back="",sql="";
		if("".equals(zid)){
			sql="insert into tblucky(startdate,enddate,zname,zscript,ztimes,zstate,luckystart,luckyend,ztype,zremark,usepass) values('"
					+startdate+"','"+enddate+"','"+zname+"','"+zscript+"',"+ztimes+","+zstate+",'"+luckystart+"','"+luckyend+"',"+ztype+",'"+zremark+"','"+usepass+"')";
		}else{
			sql="update tblucky set startdate='"+startdate+"',enddate='"+enddate+"',zname='"+zname+"',zscript='"+zscript+"',ztimes="+ztimes+",zstate="+zstate
					+",luckystart='"+luckystart+"',luckyend='"+luckyend+"',ztype="+ztype+",zremark='"+zremark+"',usepass='"+usepass+"' where zid="+zid;
		}
		if(connData.insertDataOne(sql)>-1){
			back="1";
		}
		return back;
	}
	
	public String savePrizeImpl(String prizename,String prizemoney,String percent,String znum,String prizezid,String luckyzid,String filename,String istrue){
		String back="",sql="";
		if("".equals(prizezid)){
			sql="insert into tbluckyprize(luckyid,prizename,prizemoney,percent,znum,prizeimg,istrue) values("+luckyzid+",'"+prizename+"',"+prizemoney+","+percent+","+znum+",'"+filename+"',"+istrue+")";
		}else{
			sql="update tbluckyprize set prizename='"+prizename+"',prizemoney="+prizemoney+",percent="+percent+",znum="+znum+",prizeimg='"+filename+"',istrue="+istrue+" where zid="+prizezid;
		}
		if(connData.insertDataOne(sql)>-1){
			back="1";
		}
		return back;
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
