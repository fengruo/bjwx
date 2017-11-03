package com.tony.method;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public interface UsualStrMethod {

	//根据日期取文件名
	public String getNameByDate();
	// 接收对象并检查对象
	public String checkRequestStr(HttpServletRequest request, String str);
	public String crsSql(HttpServletRequest request, String str);
	//得当前时间格式
	public String getDateFormat(String fm);
	//得全部当前时间格式
	public String getDateFormatAll();
	// 调用MD5： Java代码
	public String encryptMD5(String password);
	//判断是否为实数
	public boolean isNumeric(String str);
	//格式化字符串,不够的补０
	public String formatStr(String str,int ln);
	
	//判断对象是否为NULL
	public String isNull(Object obj);
	//根据传来文本，取对应长度，其余用省略号
	public String getLongStr(String str,int ln);
	//去，号
	public String getDHstr(String resouse);
	//去除文本里HTML代码
	public String removeTagFromText(String content);
	//上传文件
	public void copy(File src, File dst,int SIZE);
	//判断传来的日期是星期几
	public int getWeekInfo(String date);
	public String getWeekInfoCn(String date);
	//通过字典代码，取中文字典  value:缓存中的字典值 ；vl:字典代码
	public String getZDvalue(List<String[]> diclist,String vl);
	//在指定的日期基础上,增加指定的月
	public String addMonthByDate(Date a,int month);
	
	//计算数学表达式
	public  String cacComplex(String str) throws Exception ;
	//插入数据库前，进行数据替换
	public String replaceStringByData(String str);
	public List<String[]> getXlsValue(int lieNum,int line,File file,int num);
	//====获取客户端IP
    public  String getIpAddr(HttpServletRequest request);
	//取导航
	public String getDaoHang(String menuid);	
	//===导出EXCEL
		public boolean exportXlsFile(String[] headCn,List list,String file,String zdvl,String zden);
		public int getmyfloat(String str);
		
		//产生两个数之间的随机数
		public double getRandom(int begin,int end);
}
