package com.tony.action;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.tony.method.CoucherActionService;
import com.tony.method.UserActionService;
import com.tony.method.UsualStrMethod;

public class CoucherAction  extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2603113833786033255L;
	private UsualStrMethod usualStrMethod;
	private CoucherActionService coucherActionService;
	private InputStream textStream;
	private File zfile;
	private String zfileFileName;  
	private String PATH = "/imgfile/";
	//=====================================================
	public String saveCoucher(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String startdate=usualStrMethod.crsSql(request, "startdate");
		String enddate = usualStrMethod.crsSql(request, "enddate");
		String zname = usualStrMethod.crsSql(request, "zname");
		String zcount = usualStrMethod.crsSql(request, "zcount");
		String minmoney = usualStrMethod.crsSql(request, "minmoney");
		String maxmoney = usualStrMethod.crsSql(request, "maxmoney");
		String zstyle = usualStrMethod.crsSql(request, "zstyle");
		String zcontent = usualStrMethod.crsSql(request, "zcontent");
		String zid = usualStrMethod.crsSql(request, "zid");
		String zstate= usualStrMethod.crsSql(request, "zstate");
		String usewhere=usualStrMethod.crsSql(request, "usewhere");
		String usepass=usualStrMethod.crsSql(request, "usepass");
		if(!usualStrMethod.isNumeric(usewhere)){
			usewhere="1";
		}
		String filename=this.getZfileFileName();
		if(filename!=null && !filename.equals("")){
			String phoName = ServletActionContext.getServletContext().getRealPath("/") + PATH+"/";			
			File dstFile=new File(phoName+filename);
			usualStrMethod.copy(zfile, dstFile, (int)(zfile.length()));
		}else{
			filename="";
		}
		String back=coucherActionService.saveCoucherImpl(zid, startdate, enddate, zname, zcount, minmoney, maxmoney, zstyle, zcontent, filename,zstate,usewhere,usepass);
		if("".equals(back)){
			request.setAttribute("message", "对不起，保存失败！");
		}
		return "save";
	}
	public String modifyCoucher(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String zid=usualStrMethod.crsSql(request, "zid");//startdate,enddate,zname,zcontent,maxmoney,minmoney,zcount,zstyle,zstate,zid
		String[] back=coucherActionService.modifyCoucherImpl(zid);
		if(back!=null){
			String str=back[0]+"`"+back[1]+"`"+back[2]+"`"+back[3]+"`"+back[4]+"`"+back[5]+"`"+back[6]+"`"+back[7]+"`"+back[8]+"`"+zid+"`"+back[9];
			request.setAttribute("pagestr", str);
		}else{
			request.setAttribute("message", "对不起，修改查询失败！");
		}
		return "save";
	}
	public String tjGetVocher(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String zid=usualStrMethod.crsSql(request, "zid");		
		try{
			String back=coucherActionService.tjGetVocherImpl(zid);
			textStream = new ByteArrayInputStream(back.getBytes("UTF-8"));
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return SUCCESS;
	}
	public String shopTj(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String zid=usualStrMethod.crsSql(request, "zid");		
		try{
			String back=coucherActionService.shopTjImpl(zid);
			textStream = new ByteArrayInputStream(back.getBytes("UTF-8"));
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return SUCCESS;
		
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
	public File getZfile() {
		return zfile;
	}
	public void setZfile(File zfile) {
		this.zfile = zfile;
	}

	public CoucherActionService getCoucherActionService() {
		return coucherActionService;
	}
	public void setCoucherActionService(CoucherActionService coucherActionService) {
		this.coucherActionService = coucherActionService;
	}

	public String getZfileFileName() {
		return zfileFileName;
	}
	public void setZfileFileName(String zfileFileName) {
		this.zfileFileName = zfileFileName;
	}
}
