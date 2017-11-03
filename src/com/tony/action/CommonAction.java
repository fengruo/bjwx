package com.tony.action;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.common.util.ZipUtils;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.tony.database.ConnData;
import com.tony.method.UserActionService;
import com.tony.method.UsualStrMethod;
import com.tony.page.PageMethod;

public class CommonAction  extends ActionSupport{
	private UsualStrMethod usualStrMethod;
	private InputStream textStream;
	private PageMethod pageMethod;
	private String disposition;
	private ConnData connData;
	private String tempPATH="/tempPATH/";
	//分页方法
	public String getMyService(){
		HttpServletRequest request = ServletActionContext.getRequest();
		/*String userid=usualStrMethod.isNull(request.getSession().getAttribute("userid"));
		if(userid.equals("")){
			return "logout";
		}*/
		String start=usualStrMethod.checkRequestStr(request, "start");
		String pagelen=usualStrMethod.checkRequestStr(request, "pagelen");
		String title=usualStrMethod.checkRequestStr(request, "title");
		String width=usualStrMethod.checkRequestStr(request, "width");
		String dic=usualStrMethod.checkRequestStr(request, "dic");
		String sql=usualStrMethod.checkRequestStr(request, "sql");
		String index=usualStrMethod.checkRequestStr(request, "index");
		String[] page=pageMethod.firstPage(sql, start, pagelen, title, width, dic,index);
		try{
			JSONObject json = new JSONObject(); 
			 json.accumulate("page", page[0]); 
			 json.accumulate("htmlpage",page[1]); 
			textStream = new ByteArrayInputStream(json.toString().getBytes("UTF-8"));
		}catch(Exception e){
			e.printStackTrace();
		}
		return SUCCESS;
	}
	//===取字典
	public String getDicBySel(){
		HttpServletRequest request = ServletActionContext.getRequest();
		/*String userid=usualStrMethod.isNull(request.getSession().getAttribute("userid"));
		if(userid.equals("")){
			return "logout";
		}*/
		String ids=usualStrMethod.checkRequestStr(request, "ids");
		String zds=usualStrMethod.checkRequestStr(request, "zds");
		String[] id=ids.split(",");
		String[] zd=zds.split(",");
		try{
			JSONObject json = new JSONObject(); 
			for(int i=0;i<id.length;i++){
				json.accumulate(id[i], pageMethod.getDicBySelImpl(zd[i])); 
			}
			textStream = new ByteArrayInputStream(json.toString().getBytes("UTF-8"));
		}catch(Exception e){
			e.printStackTrace();
		}
		return SUCCESS;		
	}
	//===取字典勾选====xiao=================
		public String getDgz(){
			HttpServletRequest request = ServletActionContext.getRequest();
			/*String userid=usualStrMethod.isNull(request.getSession().getAttribute("userid"));
			if(userid.equals("")){
				return "logout";
			}*/
			String ids=usualStrMethod.checkRequestStr(request, "ids");
			String zds=usualStrMethod.checkRequestStr(request, "zds");
			String[] id=ids.split(",");
			String[] zd=zds.split(",");
			try{
				JSONObject json = new JSONObject(); 
				for(int i=0;i<id.length;i++){
					json.accumulate(id[i], pageMethod.getDgzImpl(zd[i])); 
				}
				textStream = new ByteArrayInputStream(json.toString().getBytes("UTF-8"));
			}catch(Exception e){
				e.printStackTrace();
			}
			return SUCCESS;		
		}
		public String freshApplication(){
			Map application=ActionContext.getContext().getApplication();
			try{
				if(!"".equals(application.get("golbString"))){
					String str=pageMethod.freshApplicationImpl();
					application.put("golbString", str);
				}
				textStream = new ByteArrayInputStream("1".getBytes("UTF-8"));
			}catch(Exception e){
				e.printStackTrace();
			}
			return SUCCESS;	
		}
		//取字典组成 BOX
		public String getDicZinp(){
			HttpServletRequest request = ServletActionContext.getRequest();
			String eng=usualStrMethod.checkRequestStr(request, "eng");
			try{
				String str=pageMethod.getDicZinpImpl(eng);
				textStream = new ByteArrayInputStream(str.getBytes("UTF-8"));
			}catch(Exception e){
				e.printStackTrace();
			}
			return SUCCESS;	
		}
		//====导出XLS
				public String exportXls(){
					HttpServletRequest request = ServletActionContext.getRequest();
					String sql=usualStrMethod.checkRequestStr(request, "sql");
					String headCn=usualStrMethod.checkRequestStr(request, "headCn");
					String filename=usualStrMethod.getNameByDate()+".xls";
					String dics=usualStrMethod.checkRequestStr(request, "dics");
					String zdvl="";
					String zden="";
					try{				
						String file = ServletActionContext.getServletContext().getRealPath("/") + tempPATH + filename;
						//=====================????=====================================================================================================================
						//String zdvl="3,6,7,8,13";
						//String zden="zxb,zpost,zw,zwhcd,zhf";
						//"3@zxb#6@zpost#7@zw#8@zwhcd#13@zhf#"
						if(!"".equals(dics)){
							String temp[]=dics.split("#");
							for(int i=0;i<temp.length;i++){
								if(!"".equals(temp[i])){
									String ts[]=temp[i].split("@");
									zdvl=zdvl+ts[0]+",";
									zden=zden+ts[1]+",";
								}
							}
							zdvl=usualStrMethod.getDHstr(zdvl);
							zden=usualStrMethod.getDHstr(zden);
						}
						if(!pageMethod.exportExcel(sql, "ID号#"+headCn,file,zdvl,zden)){
							filename="";
						}
					}catch(Exception e){
						e.printStackTrace();
						filename="";
					}			
					try{
						textStream = new ByteArrayInputStream(filename.getBytes("UTF-8"));
					}catch(Exception e){
						e.printStackTrace();
					}
					return SUCCESS;	
				}
				public String exportXlsFile(){
					HttpServletRequest request = ServletActionContext.getRequest();
					String file=usualStrMethod.checkRequestStr(request, "file");			
					try{
						disposition = file;
						String filepath = ServletActionContext.getServletContext().getRealPath("/") + tempPATH + file;
						File newfile=new File(filepath);
						InputStream stream = new FileInputStream(newfile);
						setTextStream(stream);
					}catch(Exception e){
						e.printStackTrace();
					}			
					return "file";
				}
				
				
				//数据备份
				public String startBackups(){
					HttpServletRequest request = ServletActionContext.getRequest();
					String putpath=usualStrMethod.checkRequestStr(request, "putpath");   //folder
					String userid=usualStrMethod.isNull(request.getSession().getAttribute("userid"));
					//==============压缩成ZIP文件===============
					String ph=ServletActionContext.getServletContext().getRealPath("/")+ tempPATH+usualStrMethod.getNameByDate()+"/";
					File file = new File(ph);
					file.mkdir();
					ZipUtils zip=new ZipUtils();			
					//==================================================
					String zname=usualStrMethod.getDateFormat("yyyyMMddHHmmss");
						String file1=zname+"_数据库备份.zip";  //11111111  name
						String filename=ph+file1;   //1111111111
							try{	
								String tt="";
								if(new File(putpath).exists()){
									tt=putpath;
								}else if(new File("E:\\web\\mysql\\MySQL Server 5.6\\data").exists()){
									tt="E:\\web\\mysql\\MySQL Server 5.6\\data";
								}
								zip.changeFolderZip(tt, filename);
							}catch(Exception e){
								e.printStackTrace();
							}
					try{		
						String sql="insert into tbdata_backups(ztime,zname,zuserid) values('"+usualStrMethod.getDateFormat("yyyy-MM-dd HH:mm")+"','"+file1+"',"+userid+")";
						connData.insertDataOne(sql);
						File newfile=new File(filename);
						InputStream stream = new FileInputStream(newfile);
						disposition = new String(file1.getBytes(),"ISO8859-1");
						setTextStream(stream);
					}catch(Exception e){
						e.printStackTrace();
						request.setAttribute("message", "对不起，你的备份出错，请重试！<br/>请检查是否有未收卷的考生！");
						return "message";
					}
					return "file";
				
				}
				public String getBackupsList(){
					StringBuffer sb=new StringBuffer("");
					String sql="select ztime,zname from tbdata_backups order by ztime desc";
					List<String[]> list=connData.findResult(sql, 2);
					for(String[] str:list){
						sb.append("<tr><td>"+str[0]+"</td><td>"+str[1]+"</td></tr>");
					}
					try{
						textStream = new ByteArrayInputStream(sb.toString().getBytes("UTF-8"));
					}catch(Exception e){
						e.printStackTrace();
					}
					return SUCCESS;	
				}
	//===========================================================
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

	public PageMethod getPageMethod() {
		return pageMethod;
	}

	public void setPageMethod(PageMethod pageMethod) {
		this.pageMethod = pageMethod;
	}
	public String getDisposition() {
		return disposition;
	}
	public void setDisposition(String disposition) {
		this.disposition = disposition;
	}
	public ConnData getConnData() {
		return connData;
	}
	public void setConnData(ConnData connData) {
		this.connData = connData;
	}
}
