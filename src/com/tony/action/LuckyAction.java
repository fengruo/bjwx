package com.tony.action;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.ApplicationContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.tony.method.LuckyActionService;
import com.tony.method.UserActionService;
import com.tony.method.UsualStrMethod;

public class LuckyAction extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6223212371859095141L;
	private UsualStrMethod usualStrMethod;
	private LuckyActionService luckyActionService;
	private InputStream textStream;
	private File zfile;
	private String zfileFileName;  
	private String PATH = "/imgfile/";
	//========================================================
	public String saveLucky(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String startdate=usualStrMethod.crsSql(request, "startdate");
		String enddate = usualStrMethod.crsSql(request, "enddate");
		String zname = usualStrMethod.crsSql(request, "zname");
		String zid=usualStrMethod.crsSql(request, "zid");
		String zscript = usualStrMethod.crsSql(request, "zscript");
		String ztimes = usualStrMethod.crsSql(request, "ztimes");
		String zstate = usualStrMethod.crsSql(request, "zstate");		
		String luckystart = usualStrMethod.crsSql(request, "luckystart");
		String luckyend = usualStrMethod.crsSql(request, "luckyend");
		String ztype = usualStrMethod.crsSql(request, "ztype");	
		String zremark=usualStrMethod.crsSql(request, "zremark");
		String usepass=usualStrMethod.crsSql(request, "usepass");
		try{
			String back=luckyActionService.saveLuckyImpl( zid, startdate, enddate, zname, zscript, ztimes, zstate,luckystart,luckyend,ztype,zremark,usepass);
			textStream = new ByteArrayInputStream(back.getBytes("UTF-8"));
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
		return SUCCESS;
	}
	public String savePrize(){
		HttpServletRequest request = ServletActionContext.getRequest();
		try{
			String prizename=usualStrMethod.crsSql(request, "prizename");
			String prizemoney = usualStrMethod.crsSql(request, "prizemoney");
			String percent = usualStrMethod.crsSql(request, "percent");
			String znum=usualStrMethod.crsSql(request, "znum");
			String prizezid = usualStrMethod.crsSql(request, "prizezid");
			String luckyzid = usualStrMethod.crsSql(request, "luckyzid");
			String istrue=usualStrMethod.crsSql(request, "istrue");
			String filename=this.getZfileFileName();
			if(filename!=null && !filename.equals("")){
				String phoName = ServletActionContext.getServletContext().getRealPath("/") + PATH+"/";			
				File dstFile=new File(phoName+filename);
				usualStrMethod.copy(zfile, dstFile, (int)(zfile.length()));
			}else{
				filename="";
			}
			String back=luckyActionService.savePrizeImpl(prizename,prizemoney,percent,znum,prizezid,luckyzid,filename,istrue);
			textStream = new ByteArrayInputStream(back.getBytes("UTF-8"));
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
		return SUCCESS;
	}
	public String saveNotime(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String back="";
		try{
			String notime=usualStrMethod.crsSql(request, "notime");
			Map application=ActionContext.getContext().getApplication();
			application.put("notime", notime);
			back="1";
			textStream = new ByteArrayInputStream(back.getBytes("UTF-8"));
		}catch(Exception e){
			e.printStackTrace();
		}
		return SUCCESS;
	}
	//========================================================
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
	public String getZfileFileName() {
		return zfileFileName;
	}
	public void setZfileFileName(String zfileFileName) {
		this.zfileFileName = zfileFileName;
	}

	public LuckyActionService getLuckyActionService() {
		return luckyActionService;
	}
	public void setLuckyActionService(LuckyActionService luckyActionService) {
		this.luckyActionService = luckyActionService;
	}
	public File getZfile() {
		return zfile;
	}
	public void setZfile(File zfile) {
		this.zfile = zfile;
	}
}
