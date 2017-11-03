package com.tony.impl;

import java.io.File;
import java.util.List;

import com.tony.database.ConnData;
import com.tony.method.CoucherActionService;
import com.tony.method.UsualStrMethod;

public class CoucherActionServiceImpl implements CoucherActionService {
	private ConnData connData;
	private UsualStrMethod usualStrMethod;
	//============================================================
	public String saveCoucherImpl(String zid,String startdate,String enddate,String zname,String zcount,String minmoney,String maxmoney,String zstyle,String zcontent,String filename ,String zstate,String usewhere,String usepass ){
		String back="",sql="";
		if("".equals(zid)){
			sql="insert into tbvoucher(startdate,enddate,zname,zcontent,maxmoney,minmoney,zcount,zstyle,zimage,zstate,usewhere,usepass) values('"+startdate+"','"
					+enddate+"','"+zname+"','"+zcontent+"',"+maxmoney+","+minmoney+","+zcount+","+zstyle+",'"+filename+"',"+zstate+","+usewhere+",'"+usepass+"')";
		}else{
			sql="update tbvoucher set startdate='"+startdate+"',enddate='"+enddate+"',zname='"+zname+"',zcontent='"+zcontent+"',maxmoney="
					+maxmoney+",minmoney="+minmoney+",zcount="+zcount+",zstyle="+zstyle+",zimage='"+filename+"',zstate="+zstate+",usewhere="+usewhere+",usepass='"+usepass+"' where zid="+zid;
		}
		if(connData.insertDataOne(sql)>-1){
			back="1";
		}
		return back;
	}
	public String[] modifyCoucherImpl(String zid){
		String back[]=null;
		String sql="select startdate,enddate,zname,zcontent,maxmoney,minmoney,zcount,zstyle,zstate,usewhere from tbvoucher where zid="+zid;
		List<String[]> list=connData.findResult(sql, 10);
		if(list.size()>0){
			back=list.get(0);
		}
		return back;
	}
	public String tjGetVocherImpl(String zid){
		String back="";
		String sql="SELECT zstate,COUNT(*) AS num,SUM(zmoney) AS money FROM tbvoucherget WHERE voucherid="+zid+" GROUP BY zstate";
		List<String[]> list=connData.findResult(sql,3);
		int all=0;
		for(String[] str:list){
			String zstate=str[0].equals("1")?"有效":"无效";
			int num=usualStrMethod.getmyfloat(str[1]);
			int money=usualStrMethod.getmyfloat(str[2]);
			all=all+money;
			back=back+zstate+"：数量（"+num+"）金额：（"+money+"）；";
		}
		back=back+"总金额："+all;
		return back;
	}
	public String shopTjImpl(String zid){
		StringBuffer sb=new StringBuffer("<table><tr class='headTitle'><td width=20%>店铺编号</td><td width=40%>小票总金额</td><td width=40%>代金券总金额</td></tr>");
		String sql="SELECT C.shopid,SUM(C.zmoney),SUM(C.mo) FROM (SELECT A.shopid,A.zmoney,B.zmoney AS mo FROM tbvoucheruse A,tbvoucherget B WHERE A.useid=B.zid AND B.voucherid="+zid
				+" ) C GROUP BY C.shopid";
		int two=0,three=0;
		List<String[]> list=connData.findResult(sql, 3);
		for(String[] str:list){
			sb.append("<tr ondblclick='showShopInfo("+str[0]+")'><td>"+str[0]+"</td><td>"+str[1]+"</td><td>"+str[2]+"</td></tr>");
			two=two+usualStrMethod.getmyfloat(str[1]);
			three=three+usualStrMethod.getmyfloat(str[2]);
		}
		sb.append("<tr><td>总计("+list.size()+")：</td><td>"+two+"</td><td>"+three+"</td></tr>");
		sb.append("</table>");
		return sb.toString();
	}
	//=====================================================
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
