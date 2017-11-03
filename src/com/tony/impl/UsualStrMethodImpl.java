package com.tony.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import jxl.Cell;
import jxl.Workbook;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import ognl.Ognl;
import ognl.OgnlContext;

import com.tony.database.ConnData;
import com.tony.method.UsualStrMethod;

public class UsualStrMethodImpl implements UsualStrMethod {
	private ConnData connData;
	public ConnData getConnData() {
		return connData;
	}

	public void setConnData(ConnData connData) {
		this.connData = connData;
	}
	// *************************常用的一些方法************************************************************
	//取日期文件名
	public String getNameByDate(){
		Date date = new Date();
		int year = date.getYear();
		int month = date.getMonth();
		int day = date.getDay();
		int hour = date.getHours();
		int minu = date.getMinutes();
		int second = date.getSeconds();
		int min = date.getMinutes();
		String tempStr = String.valueOf(year) + String.valueOf(month)
				+ String.valueOf(day) + String.valueOf(hour)
				+ String.valueOf(minu) + String.valueOf(second)
				+ String.valueOf(min);
		return tempStr;
	}
	// 接收对象并检查对象
	 
	public String checkRequestStr(HttpServletRequest request, String str) {
		String temp = request.getParameter(str) == null ? "" : (request
				.getParameter(str).trim());
		return temp;
	}
	//得时间格式
	 
	public String getDateFormat(String fm){
		SimpleDateFormat sdf=new SimpleDateFormat(fm);
		return sdf.format(new Date());
	}
	public String getWeekInfoCn(String date){
		int week = -1;
		String temp="";
		try{
			SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
			Date d=dateFormat.parse(date);		
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(d);
			week=calendar.get(Calendar.DAY_OF_WEEK);
			int t=week-1;
			if(t==1){
				temp="一";
			}else if(t==2){
				temp="二";
			}else if(t==3){
				temp="三";
			}else if(t==4){
				temp="四";
			}else if(t==5){
				temp="五";
			}else if(t==6){
				temp="六";
			}else if(t==0){
				temp="日";
			}
			temp="星期"+temp;
		}catch(Exception e){
			e.printStackTrace();
		}
		return temp;
	}
	//得时间格式完整
	public String getDateFormatAll(){
		String fm = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf=new SimpleDateFormat(fm);
		return sdf.format(new Date());
	}
	
	// 调用MD5： Java代码
	public String encryptMD5(String password) {
		InputStream is = new ByteArrayInputStream(password.getBytes());
		String res = this.getMD5(is);
		return res;
	}
	//判断是否为实数
	 
	public boolean isNumeric(String str){ 
	    Pattern pattern = Pattern.compile("^(-?\\d+)(\\.\\d+)?$"); 
	    return pattern.matcher(str).matches();    
	}
	//格式化字符串
	 
	public String formatStr(String str,int ln){
		int ca=ln-str.length();
		String temp="";//8*0+str
		for(int i=0;i<ca;i++){
			temp=temp+"0";
		}
		return temp+str;
	}
	//判断对象是否为NULL
	 
	public String isNull(Object obj){
		String str="";
		if(obj==null){
			str="";
		}else{
			str=(obj.toString()).trim();
		}
		return str;
	}
	//根据传来文本，取对应长度，其余用省略号
	 
	public String getLongStr(String str,int ln){
		if (str==null) return "";
		if(str.length()>ln)
			str=str.substring(0,ln)+"...";
		return str;
	}
	//去，号
	public String getDHstr(String resouse){
		if(resouse != null && resouse.length()>0){
			if(resouse.substring(resouse.length()-1,resouse.length()).equals(","))
				resouse=resouse.substring(0,resouse.length()-1);
		}
		return resouse;
	}
	//去除文本里HTML代码
	 
	public String removeTagFromText(String content) {
		Pattern p = null;
		Matcher m = null;
		String value = null;
		// 去掉<>标签
		p = Pattern.compile("(<[^>]*>)");
		m = p.matcher(content);
		String temp = content;
		while (m.find()) {
			value = m.group(0);
			temp = temp.replace(value, "");		
		}
		return temp;
	}
	//上传文件
	 
	public void copy(File src, File dst,int SIZE) {
		try {
			InputStream in = null;
			OutputStream out = null;
			try {
				in = new BufferedInputStream(new FileInputStream(src), SIZE);
				out = new BufferedOutputStream(new FileOutputStream(dst), SIZE);
				byte[] buffer = new byte[SIZE];
				while (in.read(buffer) > 0) {
					out.write(buffer);
				}
			} finally {
				if (null != in) {
					in.close();
				}
				if (null != out) {
					out.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * MD5
	 * 
	 * @param fis
	 * @return String
	 */
	private String getMD5(InputStream fis) {

		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] buffer = new byte[2048];
			int length = -1;
			while ((length = fis.read(buffer)) != -1) {
				md.update(buffer, 0, length);
			}
			return bytesToString(md.digest());
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			try {
				fis.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	/**
	 * 字节2字符串
	 * 
	 * @param data
	 * @return String
	 */
	private static String bytesToString(byte[] data) {
		char hexDigits[] = { '9', '8', '7', '6', '5', '4', '3', '2', '1', '0',
				'a', 'b', 'c', 'd', 'e', 'f' };
		char[] temp = new char[data.length * 2];
		for (int i = 0; i < data.length; i++) {
			byte b = data[i];
			temp[i * 2] = hexDigits[b >>> 4 & 0x0f];
			temp[i * 2 + 1] = hexDigits[b & 0x0f];
		}
		return new String(temp);
	}
	//判断是一年的第几周，星期几
	public int getWeekInfo(String date){
		int week = -1;
		try{
			SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
			Date d=dateFormat.parse(date);		
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(d);
			week=calendar.get(Calendar.DAY_OF_WEEK);
		}catch(Exception e){
			e.printStackTrace();
		}
		return week-1;
	}
	//通过字典代码，取中文字典  value:缓存中的字典值 ；vl:字典代码
	public String getZDvalue(List<String[]> diclist,String vl){
		String value="";
		for(String[] ss:diclist){
			if(ss[0].equals(vl)){
				value=ss[1];
				break;
			}
		}
		return value;
	}
	//在指定的日期基础上,增加指定的月
	public String addMonthByDate(Date a,int month){
		Calendar   c   =   Calendar.getInstance();//获得一个日历的实例 
        SimpleDateFormat   sdf   =   new   SimpleDateFormat( "yyyy-MM-dd "); 
        c.setTime(a);//设置日历时间 
        c.add(Calendar.MONTH,month);//在日历的月份上增加6个月 
        return sdf.format(c.getTime());
	}

	//运行数学表达式
	OgnlContext context = new OgnlContext(); 
	public  String cacComplex(String str) throws Exception {
         // Test to get value(parameter) from context  
		Object contextValue = Ognl.getValue(str, context);  
		return contextValue.toString();
	}
	
	private static String formatDate(String borndate){
	    int byear =Integer.parseInt(borndate.substring(0,4));
	    int bmon = Integer.parseInt(borndate.substring(4,6));
	    int bday = Integer.parseInt(borndate.substring(6));
        if(byear<1900||byear>2200){
            return "-4";
        }
	    if(bmon>=1&&bmon<=12){
	        if(bday>=1&&bday<=31){
	           // if(((!((byear%4==0&&byear%100!=0)||(byear%400==0)))&&bmon==2&&bday>29)){
	            if (((byear%4==0&&byear%100!=0)||(byear%400==0))){
	                if(bmon==2&&bday>29){
	                return "-52";
	                }
	                
	            }else
	                
	                
	            if(bmon==2&&bday>=29){
	                return "-52";
	            }else if((bmon==4||bmon==6||bmon==9||bmon==11)&&bday>=31){
	                return "-53";
	            }
	        }else{
	            return "-51";
	        }
	    }else{
	        return "-5";
	    }
	    return "0";
	    
	}
	
	//插入数据库前，进行数据替换
		public String replaceStringByData(String str){
			if(str==null || "".equals(str)){
				return "";
			}
			str=str.replace("'", "''");
			str=str.replace("\\", "\\\\");  
			return str.trim();
		}
		public String crsSql(HttpServletRequest request, String str){
			String temp = request.getParameter(str) == null ? "" : (request.getParameter(str).trim());
			temp=this.replaceStringByData(temp);
			return temp;
		}
		//取导航
		public String getDaoHang(String menuid){
			StringBuffer sb=new StringBuffer("");
			try{
				String sql="select zfatherid,zname,zhref,ztarget,zsonid,zid from tbpower where zid="+menuid;
				List<String[]> list=connData.findResult(sql, 6);
				String[] str=list.get(0);
				if(!"1000".equals(str[4])){
					sql="select zname from tbpower where zfatherid="+str[4];
					List<String[]> lt=connData.findResult(sql, 1);
					String[] two=lt.get(0);
					sb.append("<span>"+two[0]+"</span><img src='/hair/images/point.png'>");
					sb.append("<span><a href='"+str[2]+"?menuid="+str[5]+"' target='"+str[3]+"'>"+str[1]+"</a></span>");				
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			return sb.toString();
		}
		//===导出EXCEL
		public boolean exportXlsFile(String[] headCn,List list,String file,String zdvl,String zden){
			boolean flag=false;
			// 声明一个工作薄  
	        HSSFWorkbook workbook = new HSSFWorkbook();  
	        // 生成一个表格  
	        HSSFSheet sheet = workbook.createSheet("bj");  
	        try{
	        // 设置表格默认列宽度为15个字节  
	        sheet.setDefaultColumnWidth((short) 15);  
	        // 生成一个样式  
	        HSSFCellStyle style = workbook.createCellStyle();  
	        // 设置这些样式  
	        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);  
	        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
	        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
	        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
	        style.setBorderRight(HSSFCellStyle.BORDER_THIN);  
	        style.setBorderTop(HSSFCellStyle.BORDER_THIN);  
	        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
	        // 生成一个字体  
	        HSSFFont font = workbook.createFont();  
	        font.setColor(HSSFColor.VIOLET.index);  
	        font.setFontHeightInPoints((short) 12);  
	        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
	        // 把字体应用到当前的样式  
	        style.setFont(font);  
	        // 生成并设置另一个样式  
	        HSSFCellStyle style2 = workbook.createCellStyle();  
	       style2.setFillForegroundColor(HSSFColor.WHITE.index);  
	        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
	        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
	        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
	        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);  
	        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);  
	        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
	        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);  
	        // 生成另一个字体  
	        HSSFFont font2 = workbook.createFont();  
	        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);  
	        // 把字体应用到当前的样式  
	        style2.setFont(font2);  
	     // 产生表格标题行  
	        HSSFRow row = sheet.createRow(0);  
	        for (short i = 0; i < headCn.length; i++)   {  
	            HSSFCell cell = row.createCell(i);  
	            cell.setCellStyle(style);  
	            HSSFRichTextString text = new HSSFRichTextString(headCn[i]);  
	            cell.setCellValue(text);  
	        }  
	        String[] vs=zdvl.split(",");
	        String[] es=zden.split(",");
	        for(int i=0;i<list.size();i++){
	        	row=sheet.createRow(i+1);
	        	String[] vls=(String[])list.get(i);
	        	for(short m=0;m<vls.length;m++){
	        		 HSSFCell cell = row.createCell(m);  
			         cell.setCellStyle(style2);  
			         HSSFRichTextString text = null;  
			       	 String cl=vls[m];
			       	 if(!"".equals(cl))
			       		 cl=this.getZDvl(vs, es, cl, m);					        
			        text = new HSSFRichTextString(cl);  
			        cell.setCellValue(text);  
	        	}
	        }
	        }catch(Exception e){
	        	e.printStackTrace();
	        	flag=false;
	        }
	        OutputStream out=null;
	        try{
	        	out = new FileOutputStream(file);
	        	workbook.write(out); 
	        	flag=true;
	        }catch(Exception e){
	        	e.printStackTrace();
	        	flag=false;
	        }finally{
	        	if(out!=null){
	        		try {
						out.close();
						workbook=null;
					} catch (IOException e) {
						e.printStackTrace();
					}
	        	}
	        }
	        return flag;
		}
		//String zdvl="3,6,7,8,13";
		//String zden="zxb,zpost,zw,zwhcd,zhf";
		private String getZDvl(String[] vs,String[] es,String cl,int m){
			String back=cl,sql="";
			try{
				for(int i=0;i<vs.length;i++){
					int v=Integer.parseInt(vs[i]);
					if(v==m){
						sql="select cnvalue from tbdictionary where enname='"+es[i]+"' and codepa="+cl;
						List<String[]> list=connData.findResult(sql, 1);
						back=list.get(0)[0].toString();
						break;
					}
				}
			}catch(Exception e){
				System.out.println(sql);
				e.printStackTrace();
			}
			return back;
		}
		//按照列号取EXCEL表的值 
		public List<String[]> getXlsValue(int lieNum,int line,File file,int num){   //列的数量，列头行号，EXCEL文件,num为特殊列（比如身份证）			
			  			List<String[]> list=new ArrayList();
						InputStream is=null;
						try{	
							is=new FileInputStream(file);
							jxl.Workbook wb = Workbook.getWorkbook(is);///得到工作薄
						    jxl.Sheet st = wb.getSheet(0);///实例化sheet
						    int allRow = st.getRows();    //得到该sheet的列行数
							for(int i=line;i<allRow;i++){
								String[] ss=new String[lieNum];
								 //sb.append(st.getCell(num,i).getContents().trim()+",");//身份证
								for(int j=0;j<lieNum;j++){
									Cell c00= st.getCell(j,i);///i是行数,ss是列集合
									ss[j]=c00.getContents().trim();
								}
								list.add(ss);
							}
						}catch(Exception e){
							e.printStackTrace();
						}finally{
							if(is != null){
								try {
									is.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
						return list;
					
		}
		//====获取客户端IP
	     public  String getIpAddr(HttpServletRequest request)  {
	           String ip  =  request.getHeader( " x-forwarded-for " );
	            if (ip  ==   null   ||  ip.length()  ==   0   ||   " unknown " .equalsIgnoreCase(ip))  {
	               ip  =  request.getHeader( " Proxy-Client-IP " );
	           } 
	            if (ip  ==   null   ||  ip.length()  ==   0   ||   " unknown " .equalsIgnoreCase(ip))  {
	               ip  =  request.getHeader( " WL-Proxy-Client-IP " );
	           } 
	            if (ip  ==   null   ||  ip.length()  ==   0   ||   " unknown " .equalsIgnoreCase(ip))  {
	              ip  =  request.getRemoteAddr();
	          } 
	           return  ip;
	      }
	     public int getmyfloat(String str){
				int tt=0;
				try{
				if(str==null || "null".equals(str) || "".equals(str)){
					tt=0;
				}else{
					Float ft=Float.parseFloat(str);
					tt=ft.intValue();
				}
				}catch(Exception e){
					e.printStackTrace();
					tt=0;
				}
				return tt;
			}
	   //产生两个数之间的随机数
	 	public double getRandom(int begin,int end){
	 		double random = Math.random();
	 		return random*(end - begin)+begin;
	 	} 
}
