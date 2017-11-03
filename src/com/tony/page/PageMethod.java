package com.tony.page;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.tony.database.ConnData;
import com.tony.method.UsualStrMethod;

public class PageMethod {
	private UsualStrMethod usualStrMethod;
	private ConnData connData;
	private InputStream textStream;
	//=====1===传SQL语句和长度、及开始页数及长度  
	/* SQL语句，第一个必须是ZID
	 * String sql（SQL数据库语句）,String start（开始页）,String pagelen(页面长度),String title（页头）,String width（单元格宽）,String dic（字典值）)
	 * title:页头1#页头2#
	 * width：30#60   or   30%#50%
	 * dic: dic: 1@ztype#2@dic   第一个是对象数组的第几个，第二个是字典英文名
	 */
	public String[] firstPage(String sql,String start,String pagelen,String title,String width,String dic,String index){
		String ts[]=title.split("#");
		String ws[]=width.split("#");
		List<Object[]> list=connData.pageFenYe(sql, ts.length+1,Integer.parseInt(start)-1,Integer.parseInt(pagelen));
		StringBuffer sb=new StringBuffer("<table><tr class='headTitle'>");
		try{
			List<Integer> emplist=new ArrayList();
			for(int i=0;i<ts.length;i++){
				if("0".equals(ts[i]))
					emplist.add(i);					
				else
					sb.append("<td width='"+ws[i]+"'>"+ts[i]+"</td>");
			}
			sb.append("</tr>");
			if("".equals(dic)){  //无字典
				for(Object[] obj:list){
					sb.append("<tr id='"+obj[0]+"'>");
					int rel=obj.length-emplist.size();
					String temp="";
					if(emplist.size()>0){
						for(int i=0;i<emplist.size();i++){
							temp=temp+"<input type='hidden' name='yc' value='"+obj[emplist.get(i)+1]+"'/>";
						}
					}
					
					for(int i=1;i<rel;i++){
						if(i==(rel-1)){
							sb.append("<td>"+obj[i]+temp+"</td>");
						}else{
							sb.append("<td>"+obj[i]+"</td>");
						}
					}
					
					sb.append("</tr>");				
				}
			}else{  //有字典
				String[] dics=dic.split("#");
				for(int m=0;m<list.size();m++){
					Object[] obj=(Object[])list.get(m);
					sb.append("<tr id='"+obj[0]+"'>");
					int rel=obj.length-emplist.size();
					String temp="";
					if(emplist.size()>0){
						for(int i=0;i<emplist.size();i++){
							temp=temp+"<input type='hidden' name='yc' value='"+obj[emplist.get(i)+1]+"'/>";
						}
					}
					
				//	for(int i=1;i<obj.length;i++){
					for(int i=1;i<rel;i++){
						if(i==(rel-1)){
							sb.append("<td>"+this.getDictionaryVal(obj[i].toString(), dics, i)+temp+"</td>");
						}else{
							sb.append("<td>"+this.getDictionaryVal(obj[i].toString(), dics, i)+"</td>");
						}						
					}
					sb.append("</tr>");				
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		sb.append("</table>");
		String pagefy=this.getFenYeString(sql, Integer.parseInt(pagelen), Integer.parseInt(start), index);
		return new String[]{sb.toString(),pagefy};
	}
	
	//~~~~~~~~~~~~~~取字典dic: 1@ztype#2@dic   第一个是对象数组的第几个，第二个是字典英文名
	private String getDictionaryVal(String obj,String[] dics,int m){
		String value="";
		try{
			for(int i=0;i<dics.length;i++){
				String ss[]=dics[i].split("@");
				if(m==Integer.parseInt(ss[0])){
					String sql="select cnvalue from tbdictionary where enname='"+ss[1]+"' and codepa='"+obj+"'";
					List<String[]> list=connData.findResult(sql, 1);
					if(list.size()>0)
						value=list.get(0)[0]+"<input type='hidden' value='"+obj+"'>";
					break;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			value="";
		}
		if(value.equals("")){
			value=obj;
		}
		return value;
		
	}
	//~~~~~~~~~~~~1:sql====2:每页显示数量====3:开始页
	private String getFenYeString(String sql,int pagelen,int start,String index){
		StringBuffer sb=new StringBuffer("<div class='pageString'>");
		try{
			//String newSql="select count(*) "+(sql.substring(sql.indexOf("from"),sql.length()));
			String newSql="select count(*) from  ("+sql+") as Z";
			Object obj[]=(Object[])(connData.findResult(newSql, 1).get(0));
			int allcount=Integer.parseInt(obj[0].toString());                                               //总记录数
			
			int pagecount=allcount/pagelen;                                                                                                                                              //总页数
			if(allcount%pagelen>0){
				pagecount=pagecount+1;
			}
			if(pagecount>1){
				if(start<2){
					sb.append("<span>首页</span><span>上一页</span>");
				}else{
					sb.append("<a onclick=\"getClickPage(1,"+ index+")\">首页</a><a onclick=\"getClickPage("+(start-1)+","+ index+")\">上一页</a>");
				}
			}
			
			sb.append("&nbsp;【共有"+allcount+"条记录，共有"+pagecount+"页，当前第"+start+"页】&nbsp;&nbsp;");
			if(pagecount>1){
				if(pagecount==start){
					sb.append("<span>下一页</span><span>末页</span>");
				}else{
					sb.append("<a onclick=\"getClickPage("+(start+1)+","+ index+")\">下一页</a><a onclick=\"getClickPage("+pagecount+","+ index+")\">末页</a>");
				}
			}
			sb.append("</div>");
		}catch(Exception e){
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	//取字典到 SELECT
	public String getDicBySelImpl(String zd){
		String sql="select codepa,cnvalue from tbdictionary where enname='"+zd+"' order by codepa";
		List<String[]> list=connData.findResult(sql, 2);
		StringBuffer sb=new StringBuffer("");
		for(int i=0;i<list.size();i++){
			String[] obj=list.get(i);
			sb.append("<option value='"+obj[0]+"'>"+obj[1]+"</option>");
		}
		return sb.toString();
	}
	//取字典勾选到 SELECT============xiao=========
		public String getDgzImpl(String zd){
			String sql="select codepa,cnvalue from tbdictionary where enname='"+zd+"' order by codepa";
			List<String[]> list=connData.findResult(sql, 2);
			StringBuffer sb=new StringBuffer("");
			for(int i=0;i<list.size();i++){
				String[] obj=list.get(i);
				sb.append("<input value='"+obj[0]+"' type='Checkbox'>"+obj[1]+"</input><input />件");
			}
			return sb.toString();
		}
		//==刷新缓存
		public String freshApplicationImpl(){
			String sql="select zid,zname from tbshop where ztype=1 order by znum";
			StringBuffer sb=new StringBuffer("");
			List<String[]> list=connData.findResult(sql, 2);
			for(String[] obj:list){
				sb.append("<option  value='"+obj[0]+"'>"+obj[1]);
			}
			return sb.toString();
		}
		public String getDicZinpImpl(String eng){
			String sql="select codepa,cnvalue from tbdictionary where enname='"+eng+"' order by codepa*1";
			List<String[]> list=connData.findResult(sql, 2);
			StringBuffer sb=new StringBuffer("");
			for(int i=0;i<list.size();i++){
				String[] obj=list.get(i);
				String id="box"+i;
				int st=Integer.parseInt(obj[0].toString());
				if(st<100){
					sb.append("<input type='checkbox' name='zdbox' value='"+obj[0]+"' id='"+id+"' onclick='addXz(this)'><label for='"+id+"'>"+obj[1]+"</label>&nbsp;&nbsp;");
				}else{
					sb.append("<input type='radio' name='zcbox' value='"+obj[0]+"' id='"+id+"' onclick='addtype(this)'><label for='"+id+"'>"+obj[1]+"</label>&nbsp;&nbsp;");
				}
			}
			return sb.toString();
		}
		//====导出EXCEL表
		public boolean exportExcel(String sql,String head,String file,String zdvl,String zden){
			String ss[]=head.split("#");
			List list=connData.findResult(sql, ss.length);
			boolean filename=usualStrMethod.exportXlsFile(ss, list,file,zdvl,zden);
			return filename;
		}
//=======================================================================================
	public UsualStrMethod getUsualStrMethod() {
		return usualStrMethod;
	}
	public void setUsualStrMethod(UsualStrMethod usualStrMethod) {
		this.usualStrMethod = usualStrMethod;
	}
	public ConnData getConnData() {
		return connData;
	}
	public void setConnData(ConnData connData) {
		this.connData = connData;
	}
	public InputStream getTextStream() {
		return textStream;
	}
	public void setTextStream(InputStream textStream) {
		this.textStream = textStream;
	}
}
