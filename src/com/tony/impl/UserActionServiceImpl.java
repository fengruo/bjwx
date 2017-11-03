package com.tony.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.tony.database.ConnData;
import com.tony.method.UserActionService;
import com.tony.method.UsualStrMethod;

public class UserActionServiceImpl implements UserActionService{
	private ConnData connData;
	private UsualStrMethod usualStrMethod;
	public String[] loginImpl(String user,String zpass,String ipaddress){
		String[] ss={"0","","",""};
		try{
			String sql="select zid,ztype from tbuser where zuser='"+user+"' and zpass='"+zpass+"'";
			List<String[]> lt=connData.findResult(sql, 2);
			
			if(lt.size()>0){			
				String[] ts=lt.get(0);
				ss[0]=ts[0];
				ss[2]=ts[1];
				sql="select zname,ztype from tbuserinfo where userid="+ts[0];  //2离职，1在职，3冻结
				List<String[]> list=connData.findResult(sql, 2);
				if(list.size()>0){
					String[] back=list.get(0);
					ss[3]=back[1];
					String zname=back[0];
					ss[1]=zname;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return ss;
	}
	
	public String cmsloginImpl(String zuser,String zpass){
		String back="";
		String sql="select zid from tbcmsuser where zuser='"+zuser+"' and zpass='"+usualStrMethod.encryptMD5(zpass)+"'";
		List<String[]> list=connData.findResult(sql, 1);
		if(list.size()>0){
			back=list.get(0)[0];			
		}
		return back;
	}
	//===================================================

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
